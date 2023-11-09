package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //get items from activity
    EditText inputAddress;
    TextView displayLatitude;
    TextView displayLongitude;
    EditText inputLatitude;
    EditText inputLongitude;

    //create activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //database connection
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        //location services: geocoder to find address using latitude and longitude
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        double latitude;
        double longitude;

        //populate database if empty, using input file
        if (dbHandler.isEmpty()){
            //if empty fill with textfile input and address output
            //read through input file
            try(BufferedReader in = new BufferedReader(new FileReader("lat_long.txt"))) {
                //get each line
                String str;
                while ((str = in.readLine()) != null) {
                    //use comma as token to divide latitude and longitude, retrieve both values
                    String[] tokens = str.split(",");
                    latitude = Double.parseDouble(tokens[0]);
                    longitude = Double.parseDouble(tokens[1]);
                    //get address given lat and long
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    String address = addresses.get(0).getAddressLine(0); //retrieve addressline
                    //add address, lat and long to database
                    dbHandler.addLocation(address,latitude,longitude);
                }
            }
            //catch error
            catch (IOException e) {
                e.printStackTrace();
            }

            //navigate to update table activity
            Button buttonUpdateNav = (Button) findViewById(R.id.buttonUpdateNav);
            buttonUpdateNav.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    Intent i = new Intent(view.getContext(), UpdateActivity.class);
                    startActivity(i);
                }
            });

            //display latitude and longitude of address submitted ie button clicked
            Button findLatLon = (Button) findViewById(R.id.submitAddress);
            findLatLon.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    //get address input editview
                    inputAddress = (EditText) findViewById(R.id.searchAddress);

                    //get textview to display lat and lon
                    displayLatitude = (TextView) findViewById(R.id.displayLatitude);
                    displayLongitude = (TextView) findViewById(R.id.displayLongitude);
                    String lat, lon = null;

                    //confirm input is provided for address editview field
                    if (confirmInputAddress()){
                        //dbhandler operations to find latitude and longitude of address in database
                        lat = dbHandler.getLatitude(inputAddress.getText().toString());
                        lon = dbHandler.getLongitude(inputAddress.getText().toString());

                        //display results in textview
                        displayLatitude.setText(lat);
                        displayLongitude.setText(lon);
                    }
                }
            });

            //add location entry: use longitude and latitude to search address location and input 3 fields into table
            Button addEntry = (Button) findViewById(R.id.addEntry);
            addEntry.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    inputLatitude = (EditText) findViewById(R.id.inputLatitude);
                    inputLongitude = (EditText) findViewById(R.id.inputLongitude);
                    //confirm some input provided
                    if (confirmLatLon()){
                        //get input as double
                        double latitude = Double.parseDouble(inputLatitude.getText().toString());
                        double longitude = Double.parseDouble(inputLatitude.getText().toString());
                        //user geocoder to get address from lat and lon
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String address = addresses.get(0).getAddressLine(0); //retrieve addressline
                        //add address, lat and long to database
                        dbHandler.addLocation(address,latitude,longitude);
                        //clear input field
                        inputAddress.setText("");
                        Toast.makeText(getApplicationContext(),"Entry Added!",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //delete location entry: use longitude and latitude to search address location and input 3 fields into table
            Button deleteEntry = (Button) findViewById(R.id.deleteEntry);
            addEntry.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    inputLatitude = (EditText) findViewById(R.id.inputLatitude);
                    inputLongitude = (EditText) findViewById(R.id.inputLongitude);
                    //confirm some input provided
                    if (confirmLatLon()){
                        //get input as double
                        double latitude = Double.parseDouble(inputLatitude.getText().toString());
                        double longitude = Double.parseDouble(inputLatitude.getText().toString());
                        //get column id where lat and lon match
                        int id = dbHandler.getID(latitude,longitude);

                        //delete entry at that column id if an id is found in the table
                        if(id<0){
                            //id not found, show notification
                            Toast.makeText(getApplicationContext(),"This entry does not exist!",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //id found, do database operation to delete entry
                            dbHandler.deleteLocation(id);
                            //clear input field
                            inputLatitude.setText("");
                            inputLongitude.setText("");
                            Toast.makeText(getApplicationContext(),"Entry Deleted!",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });




        }

    }
    //confirm some user input given to address editview field
    public boolean confirmInputAddress() {
        int inputLength = 1; //minimum input requirement is 1 character long
        //check input is less than 1 character
        if (inputAddress.getText().length() < inputLength) {
            inputAddress.setError("Invalid Input: Please Enter a Address."); //error message if no input
            return false;
        }
        //return true is all fields have user input
        else{
            return true;
        }
    }

    //confirm some user input given to latitude and longitude editview field
    public boolean confirmLatLon() {
        int inputLength = 1; //minimum input requirement is 1 character long
        //check input is less than 1 character
        if (inputLatitude.getText().length() < inputLength) {
            inputLatitude.setError("Invalid Input: Please Enter a Latitude."); //error message if no input
            return false;
        }
        else if (inputLongitude.getText().length() < inputLength){
            inputLongitude.setError("Invalid Input: Please Enter a Longitude."); //error message if no input
            return false;
        }
        //return true is all fields have user input
        else{
            return true;
        }
    }


}