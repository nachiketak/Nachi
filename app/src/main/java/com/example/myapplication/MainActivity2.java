package com.example.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {

    private static final String NAME = "Nachiketa";
    private static final String EMAIL_ID = "dk+nachiketa@clevertap.com";
    private static final int PRODUCT_ID = 1;
    private static final String PRODUCT_IMAGE_URL = "https://d35fo82fjcw0y8.cloudfront.net/2018/07/26020307/customer-success-clevertap.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call CleverTap API
                CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
                HashMap<String, Object> profileUpdate = new HashMap<String, Object>();

                //Update pre-defined profile properties
                profileUpdate.put("Name", NAME);
                profileUpdate.put("Email", EMAIL_ID);
                //Update custom profile properties
                profileUpdate.put("Product ID", PRODUCT_ID);
                profileUpdate.put("Product Image", PRODUCT_IMAGE_URL);
                profileUpdate.put("MSG-push", true);
                profileUpdate.put("MSG-email", true);

                Snackbar.make(view, "Product view event triggered", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                clevertapDefaultInstance.pushProfile(profileUpdate);

                BufferedReader br = null;
                String line = "";
                final String cvsSplitBy = ",";

                try {
                    CleverTapAPI newClevertapDefaultInstance = null;
                    InputStream is = getResources().openRawResource(R.raw.data);
                    br = new BufferedReader(new InputStreamReader(is));
                    int counter = 0;
                    while ((line = br.readLine()) != null) {
                        counter++;
                        if (counter == 1) continue;
                        // use comma as separator
                        String[] data = line.split(cvsSplitBy);
                        String userEmail = "user_" + String.valueOf(counter-1)+"@clevertap.com";
                        //System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");
                        HashMap<String,Object> userData = new HashMap<String,Object>();
                        userData.put("Email",userEmail);
                        userData.put("User ID",data[0]);
                        userData.put("Location",data[1]);
                        userData.put("Customer Type",data[2]);
                        userData.put("Subscription ID",data[3]);
                        userData.put("Gender",data[4]);
                        newClevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
                        newClevertapDefaultInstance.pushProfile(userData);
                    }

                } catch (FileNotFoundException e) {
                    Logger.v(e.getMessage());
                } catch (IOException e) {
                    Logger.v(e.getMessage());
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            Logger.v(e.getMessage());
                        }
                    }
                }

            }
        });
    }
}