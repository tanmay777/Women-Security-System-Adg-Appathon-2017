package com.example.tanmay.womensecurity.Entity.Login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tanmay.womensecurity.Boundary.Database.UserInformation;
import com.example.tanmay.womensecurity.Entity.TopLevel.TopLevelActivity;
import com.example.tanmay.womensecurity.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,android.location.LocationListener {
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    TextView login,skipThis;
    TextInputLayout emailEditTextLayout,passwordEditTextLayout;
    UserInformation userInformation=new UserInformation();
    String[] parts;
    EditText emailEditText,passwordEditText;
    LocationManager locationManager;
    android.location.LocationListener locationListener;
    double latitude,longitude;
    String formattedDate;
    JSONArray jsonArray;
    String formattedTime;

    private SignInButton googleSignInButton;
    private static final String TAG=LoginActivity.class.getSimpleName(); //returns name of the class
    private static final int RC_SIGN_IN=007;
    //RC stands for RequestCode


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        skipThis=(TextView)findViewById(R.id.login_skip_this);
        skipThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),TopLevelActivity.class));
            }
        });
        googleSignInButton=(SignInButton)findViewById(R.id.button_sign_in);
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setScopes(gso.getScopeArray());
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        emailEditText=(EditText)findViewById(R.id.email);
        passwordEditText=(EditText)findViewById(R.id.password);
        emailEditTextLayout=(TextInputLayout)findViewById(R.id.input_email);
        passwordEditTextLayout=(TextInputLayout)findViewById(R.id.input_password);
        login=(TextView)findViewById(R.id.login_sign_in);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
        }
        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude=location.getLatitude();
                longitude=location.getLongitude();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
                formattedDate = df.format(c.getTime());
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
                formattedTime = tf.format(c.getTime());
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 5, locationListener);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEditText.getText().toString().isEmpty()) {
                    emailEditTextLayout.setErrorEnabled(true);
                    emailEditTextLayout.setError("Enter the email first");
                } else if (passwordEditText.getText().toString().isEmpty()) {
                    passwordEditTextLayout.setErrorEnabled(true);
                    passwordEditTextLayout.setError("Enter password first");
                } else {
                    emailEditTextLayout.setErrorEnabled(false);
                    passwordEditTextLayout.setErrorEnabled(false);
                    String url = "http://adghack.herokuapp.com/user/create";

                    JSONObject obj=new JSONObject();
                    try {
                        obj.put("email", emailEditText.getText().toString());
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
                            Toast.makeText(getApplicationContext(),"Some error came",Toast.LENGTH_SHORT).show();
/*                                               try {
                               jsonArray=response.getJSONArray("response");
                           }catch (JSONException e)
                           {
                               e.printStackTrace();
                           }
        for(int i=0;i<jsonArray.length();i++)
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
                            startActivity(new Intent(getApplicationContext(),TopLevelActivity.class));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Response not obtained",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),TopLevelActivity.class));
                        }
                    });
                    Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
                   /* JSONArray jsonArray = new JSONArray();
                    JSONObject obj=new JSONObject();
                    try {
                        obj.put("email", emailEditText.getText().toString());
                        obj.put("latitude", String.valueOf(latitude));
                        obj.put("longitude", String.valueOf(longitude));
                        obj.put("time",formattedTime);
                        obj.put("date",formattedDate);
                        //obj.put("date", "2007-12-13");
                    } catch (JSONException error) {
                        error.printStackTrace();
                    }
                    jsonArray.put(obj);

                    JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,url,jsonArray, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            userInformation.setSosRequestArray(response);
                            startActivity(new Intent());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"Response not obtained",Toast.LENGTH_SHORT).show();
                        }
                    });
                    Volley.newRequestQueue(getApplicationContext()).add(request);
                    */
                }
            }
        });
    }

    /*JsonObjectRequest jsonPostRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String data = "Name is" + response.getString("name");
                                data.concat("\n");
                                data.concat("Registration no:" + response.getInt("regno"));
                                data.concat("\n");
                                data.concat("Email" + response.getString("email"));
                                data.concat("\n");
                                data.concat("Team" + response.getString("team"));
                                data.concat("\n");
                                data.concat("Hacker Rank" + response.getString("hackerrank"));
                            } catch (Exception error) {
                                error.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("email","tanmay.jha1@gmail.com");
                            params.put("latitude","012.453");
                            params.put("longitude","023.534");
                            params.put("time","23:32");
                            params.put("date","2007-12-13");
                            return params;
                        }
                    };

                    Volley.newRequestQueue(getApplicationContext()).add(jsonPostRequest);*/

    @Override
    public void onStart()
    {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr=Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if(opr.isDone()){
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG,"Got cached sign-in");
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }
        else{
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG,"HandleSignInResult:"+result.isSuccess());
        if(result.isSuccess()) {
            //Signed in succesfilly, show authenticated UI
            GoogleSignInAccount acct = result.getSignInAccount();
            parts=acct.getDisplayName().trim().split(" ");
            userInformation.setFirstName(parts[0]);
            userInformation.setLastName(acct.getFamilyName());
            userInformation.setLastName(acct.getEmail());
            startActivity(new Intent(getApplicationContext(), TopLevelActivity.class));
        }

    }

    private void signIn(){
        //Maybe here signInIntent will get intent data from mGoogleApiClient
        Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
        //Starting the intent prompts the user to select a Google account to sign in with.
        // If you requested scopes beyond profile, email, and openid,
        // the user is also prompted to grant access to the requested resources.
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }

    private void showProgressDialog(){
        if(mProgressDialog==null){
            mProgressDialog=new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog(){
        if(mProgressDialog!=null && mProgressDialog.isShowing()){
            mProgressDialog.hide();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
        formattedDate = df.format(c.getTime());
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        formattedTime = tf.format(c.getTime());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
}
