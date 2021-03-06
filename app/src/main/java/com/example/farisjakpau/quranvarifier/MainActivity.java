package com.example.farisjakpau.quranvarifier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    LoginButton loginButton ;
    CallbackManager callbackManager;
    Profile profile;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Connnection connnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        keyHash();
        declaration();

        loginButton.setReadPermissions("public_profile","email","user_posts");
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("LOGIN SUCCESS");
                System.out.println("PROFILE FB ID -->"+ profile.getName());
                System.out.println("FACEBOOK ID -->"+ loginResult.getAccessToken().getUserId());

                String token = loginResult.getAccessToken().toString();
                String profileName = profile.getName();
                connnection.setToken(token,getApplicationContext());
                connnection.setUserName(profileName,getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), feedActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancel() {
                System.out.println("LOGIN CANCEL");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("LOGIN ERROR" + error);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    public void keyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.farisjakpau.fesbuk",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void declaration(){
        loginButton = (LoginButton)findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        profile = Profile.getCurrentProfile();

//        class declar
        connnection = new Connnection();
    }
}
