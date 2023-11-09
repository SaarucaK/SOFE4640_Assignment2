package com.example.mobileassignment2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mobileassignment2.MainActivity;

public class UpdateActivity extends AppCompatActivity {

    EditText oldLatitude;
    EditText oldLongitude;
    EditText oldAddress;
    EditText newLatitude;
    EditText newLongitude;
    EditText newAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        setTitle("Update Location");

        //back button - go back to main activity
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //database connection
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        //retrieve all user input
        oldLatitude = (EditText) findViewById(R.id.oldLatitude);
        oldLongitude = (EditText) findViewById(R.id.oldLongitude);
        oldAddress = (EditText) findViewById(R.id.oldAddress);
        newLatitude = (EditText) findViewById(R.id.newLatitude);
        newLongitude = (EditText) findViewById(R.id.newLongitude);
        newAddress = (EditText) findViewById(R.id.newAddress);

        //update database when update button clicked
        Button buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //only complete operation if input given for all fields
                if (confirmAllFieldInput()){
                    //get all input var double for lat and long, string for address
                    double oldLat = Double.parseDouble(oldLatitude.getText().toString());
                    double oldLon = Double.parseDouble(oldLongitude.getText().toString());
                    String oldAdd = oldAddress.getText().toString();
                    double newLat = Double.parseDouble(newLatitude.getText().toString());
                    double newLon = Double.parseDouble(newLongitude.getText().toString());
                    String newAdd = newAddress.getText().toString();
                    int id = dbHandler.getIdByAll(oldLat,oldLon,oldAdd);
                    //update entry at that column id if an id is found in the table
                    if (id<0){
                        //id not found, show notification to inform user
                        Toast.makeText(getApplicationContext(),"This entry does not exist!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //id found, do database operation
                        dbHandler.updateLocation(id, newAdd, newLat, newLon);
                        //reset all edittext fields
                        oldLatitude.setText("");
                        oldLongitude.setText("");
                        oldAddress.setText("");
                        newLatitude.setText("");
                        newLongitude.setText("");
                        newAddress.setText("");
                        //notify user
                        Toast.makeText(getApplicationContext(),"Update Success!",Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

    }

    //confirm some user input given to all edittext field
    public boolean confirmAllFieldInput() {
        int inputLength = 1; //minimum input requirement is 1 character long
        //check input is less than 1 character
        if (oldLatitude.getText().length() < inputLength) {
            oldLatitude.setError("Invalid Input: Please Enter a Latitude."); //error message if no input
            return false;
        }
        else if (oldLongitude.getText().length() < inputLength){
            oldLongitude.setError("Invalid Input: Please Enter a Longitude."); //error message if no input
            return false;
        }
        else if (oldAddress.getText().length() < inputLength){
            oldAddress.setError("Invalid Input: Please Enter a Address."); //error message if no input
            return false;
        }
        else if (newLatitude.getText().length() < inputLength){
            newLatitude.setError("Invalid Input: Please Enter a Latitude."); //error message if no input
            return false;
        }
        else if (newLongitude.getText().length() < inputLength){
            newLongitude.setError("Invalid Input: Please Enter a Longitude."); //error message if no input
            return false;
        }
        else if (newAddress.getText().length() < inputLength){
            newAddress.setError("Invalid Input: Please Enter a Address."); //error message if no input
            return false;
        }
        //return true is all fields have user input
        else{
            return true;
        }
    }
}