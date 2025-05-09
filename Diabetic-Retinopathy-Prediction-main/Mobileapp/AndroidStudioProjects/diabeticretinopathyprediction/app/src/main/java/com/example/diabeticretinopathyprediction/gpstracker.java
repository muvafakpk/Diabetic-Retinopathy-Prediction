package com.example.diabeticretinopathyprediction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
//import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class gpstracker extends Service implements LocationListener {



    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        hnd= new Handler();
        getLocation();
        hnd.post(rn);
    }

    Handler hnd;
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        hnd= new Handler();
        hnd.post(rn);
    }

    Runnable rn= new Runnable() {
        @Override
        public void run() {

            getLocation();

            hnd.postDelayed(rn,5000);
        }
    };
    public static String place="",lati="",longi="";

    public Location getLocation() {
        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            String loc = "";
                            String address = "";
                            Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
                            try {
                                List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (addresses.size() > 0) {
                                    for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex(); index++)
                                        address += addresses.get(0).getAddressLine(index) + " ";
                                    //Log.d("get loc...", address);

                                     place = addresses.get(0).getFeatureName().toString();
//                                    Toast.makeText(this, "Lattt"+latitude, Toast.LENGTH_SHORT).show();

//                                    String url="",url1="";
//                                    SharedPreferences sp;
//                                    sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                                    url=sp.getString("url","");
//                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                                    StringRequest postRequest = new StringRequest(Request.Method.POST, url+"/location_updation",
//                                            new Response.Listener<String>() {
//                                                @Override
//                                                public void onResponse(String response) {
//                                                    //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
//
//                                                    // response
//                                                    try {
//                                                        JSONObject jsonObj = new JSONObject(response);
//                                                        if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
//
////                                                            Toast.makeText(getApplicationContext(), "Successfully inserted.....", Toast.LENGTH_SHORT).show();
//
//
//                                                        }
//
//
//                                                        // }
//                                                        else {
////								Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
//                                                        }
//
//                                                    }    catch (Exception e) {
//                                                        Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            },
//                                            new Response.ErrorListener() {
//                                                @Override
//                                                public void onErrorResponse(VolleyError error) {
//                                                    // error
//                                                    Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                    ) {
//                                        @Override
//                                        protected Map<String, String> getParams() {
//                                            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                                            Map<String, String> params = new HashMap<String, String>();
//
//                                            params.put("lati",lati);
//                                            params.put("longi",longi);
//                                            params.put("lid",sh.getString("lid",""));
//                                            params.put("place",place);
//
//                                            return params;
//                                        }
//                                    };
//
//                                    int MY_SOCKET_TIMEOUT_MS=100000;
//
//                                    postRequest.setRetryPolicy(new DefaultRetryPolicy(
//                                            MY_SOCKET_TIMEOUT_MS,
//                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                                    requestQueue.add(postRequest);
//

                                    //	 loc= addresses.get(0).getLocality().toString();
//                                    Toast.makeText(getBaseContext(),place , Toast.LENGTH_SHORT).show();
                                    //	Toast.makeText(getBaseContext(),ff , Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(getBaseContext(), "noooooooo", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            lati= latitude+"";
                            longi=longitude+"";


//                            Toast.makeText(getApplicationContext(),"aaaaaa"+ latitude+"--"+longitude+"--"+ place,Toast.LENGTH_LONG).show();
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();


                                String loc = "";
                                String address = "";
                                Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
                                try {
                                    List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    if (addresses.size() > 0) {
                                        for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex(); index++)
                                            address += addresses.get(0).getAddressLine(index) + " ";
                                        //Log.d("get loc...", address);

                                        place = addresses.get(0).getFeatureName().toString();

                                        lati= latitude+"";
                                        longi=longitude+"";


                                    } else {
                                        //Toast.makeText(getBaseContext(), "noooooooo", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                Toast.makeText(getApplicationContext(),"aaaaaa"+ latitude+"--"+longitude+"--"+place,Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(gpstracker.this);
        }
    }

    /**
     * Function to get latitude
     * */

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}