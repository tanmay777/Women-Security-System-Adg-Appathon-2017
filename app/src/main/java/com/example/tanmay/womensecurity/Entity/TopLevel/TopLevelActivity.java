package com.example.tanmay.womensecurity.Entity.TopLevel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tanmay.womensecurity.Boundary.Database.UserInformation;
import com.example.tanmay.womensecurity.Entity.Login.LoginActivity;
import com.example.tanmay.womensecurity.Entity.Settings.SettingsActivity;
import com.example.tanmay.womensecurity.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TopLevelActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback {
    private GoogleMap mMap;
    LocationManager locationManager;
    android.location.LocationListener locationListener;
    String formattedDate;
    String formattedTime;
    double latitude,longitude;
    JSONArray jsonArray;
    UserInformation userInformation=new UserInformation();
    JSONArray sosRequest;
    TextView activateSos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocation();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        activateSos=(TextView)findViewById(R.id.Activate_sos);
        activateSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ){
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 10);
                    }
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+userInformation.getEmergencyContactEmail()));
                startActivity(callIntent);
                Toast.makeText(TopLevelActivity.this, "Location is being sent to nearby people.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_level, menu);
        return true;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //this will be called everytime the location changes
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("You"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),16));
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
                formattedDate = df.format(c.getTime());
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
                formattedTime = tf.format(c.getTime());

                String url = "http://adghack.herokuapp.com/sos/create";

                JSONObject obj=new JSONObject();
                try {
                    obj.put("email", userInformation.getEmail());
                    obj.put("latitude", String.valueOf(latitude));
                    obj.put("longitude", String.valueOf(longitude));
                    obj.put("time",formattedTime);
                    obj.put("date",formattedDate);
                    //obj.put("date", "2007-12-13");
                } catch (JSONException error) {
                    error.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url,obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
                        /*for(int i=0;i<jsonArray.length();i++)
                        {
                            try {
                                userInformation.latitudes[i]=jsonArray.getString(1);
                                userInformation.longitudes[i]=jsonArray.getString(2);
                            }catch (JSONException error)
                            {
                                error.printStackTrace();
                            }
                        }
                        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();*/
                        //startActivity(new Intent(getApplicationContext(),TopLevelActivity.class));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Response not obtained",Toast.LENGTH_SHORT).show();
                    }
                });
                Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        //if(userInformation.latitudes!=null)
        {
         //   for(int i=0;i<userInformation.latitudes.length;i++)
            {
  //              mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(userInformation.latitudes[i]),Double.parseDouble(userInformation.longitudes[i]))));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void findLocation() {
        Toast.makeText(getApplicationContext(),"Wait, getting your location",Toast.LENGTH_SHORT).show();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
        }
        Log.v("Maps Activity","Check1");
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 5, locationListener);
        // 0 for min distance doesn't means the location manger will
        // request for location all the time. Instead it means the
        // that it will not only consider secs parameter. Note seconds are in miniseconds
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(28.7041,77.1025);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Delhi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,16));
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
    }


}