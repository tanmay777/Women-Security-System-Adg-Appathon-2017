package com.example.tanmay.womensecurity.Entity.EmergencyContact;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tanmay.womensecurity.Boundary.Database.UserInformation;
import com.example.tanmay.womensecurity.Entity.Settings.SettingsActivity;
import com.example.tanmay.womensecurity.R;

public class EmergencyContactActivity extends AppCompatActivity {
    EditText emergencyName,emergencyPhoneNo,emergencyEmail;
    UserInformation userInformation=new UserInformation();
    TextView saveContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        emergencyName=(EditText)findViewById(R.id.emergency_name);
        emergencyPhoneNo=(EditText)findViewById(R.id.emergency_phone_no);
        emergencyEmail=(EditText)findViewById(R.id.emergency_email);
        saveContact=(TextView)findViewById(R.id.save_contact);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(!userInformation.getEmergencyContactName().isEmpty())
            emergencyName.setText(userInformation.getEmergencyContactName());
        if(!userInformation.getEmergencyContactPhoneNo().isEmpty())
            emergencyPhoneNo.setText(userInformation.getEmergencyContactPhoneNo());
        if(!userInformation.getEmergencyContactEmail().isEmpty())
            emergencyEmail.setText(userInformation.getEmergencyContactEmail());
        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    userInformation.setEmergencyContactName(emergencyName.getText().toString());
                    userInformation.setEmergencyContactPhoneNo(emergencyPhoneNo.getText().toString());
                    userInformation.setEmergencyContactEmail(emergencyEmail.getText().toString());
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
