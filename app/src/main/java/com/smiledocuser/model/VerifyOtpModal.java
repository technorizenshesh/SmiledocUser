package com.smiledocuser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpModal {

    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;


    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("register_id")
        @Expose
        public String registerId;
        @SerializedName("social_id")
        @Expose
        public String socialId;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("zipcode")
        @Expose
        public String zipcode;
        @SerializedName("date_of_birth")
        @Expose
        public String dateOfBirth;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("attachment1")
        @Expose
        public String attachment1;
        @SerializedName("attachment2")
        @Expose
        public String attachment2;
        @SerializedName("attachment3")
        @Expose
        public String attachment3;
        @SerializedName("otp")
        @Expose
        public String otp;


    }

}