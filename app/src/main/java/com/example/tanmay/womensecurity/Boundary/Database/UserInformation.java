package com.example.tanmay.womensecurity.Boundary.Database;

import org.json.JSONArray;

/**
 * Created by tanmay on 4/3/17.
 */

public class UserInformation {
    public static String FirstName="'",LastName="";
    public static String emergencyContactName="",emergencyContactPhoneNo="",emergencyContactEmail="";
    public static String[] latitudes=new String[]{"0","0","0","0","0","0","0","0","0"};
    public static String[] longitudes=new String[]{"0","0","0","0","0","0","0","0","0"};


    public static void setEmergencyContactName(String emergencyContactName) {
        UserInformation.emergencyContactName = emergencyContactName;
    }

    public static void setEmergencyContactPhoneNo(String emergencyContactPhoneNo) {
        UserInformation.emergencyContactPhoneNo = emergencyContactPhoneNo;
    }

    public static void setEmergencyContactEmail(String emergencyContactEmail) {
        UserInformation.emergencyContactEmail = emergencyContactEmail;
    }

    public static String getEmergencyContactName() {

        return emergencyContactName;
    }

    public static String getEmergencyContactPhoneNo() {
        return emergencyContactPhoneNo;
    }

    public static String getEmergencyContactEmail() {
        return emergencyContactEmail;
    }

    public static void setSosRequestArray(JSONArray sosRequestArray) {
        UserInformation.sosRequestArray = sosRequestArray;
    }

    public static JSONArray getSosRequestArray() {

        return sosRequestArray;
    }

    public static JSONArray sosRequestArray;

    public UserInformation() {
    }

    public static void setFirstName(String firstName) {

        FirstName = firstName;
    }

    public static void setLastName(String lastName) {
        LastName = lastName;
    }

    public static void setEmail(String email) {
        UserInformation.email = email;
    }

    public static String getEmail() {

        return email;
    }

    public static String getLastName() {
        return LastName;
    }

    public static String getFirstName() {
        return FirstName;
    }

    public static String email="vinitbodhwani123@gmail.com";

}
