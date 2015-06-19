package com.example.geoloc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class Geoloc extends Activity {

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 5; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 10000; // in Milliseconds
    
    protected LocationManager locationManager;
    
    protected Button retrieveSend;
    
    Button button;
	EditText editPhoneNum;
	EditText editSMS;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geoloc);
        
        //turnGPSOn();
        
        retrieveSend = (Button) findViewById(R.id.retrieveSend);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
		editPhoneNum = (EditText) findViewById(R.id.editPhoneNum);
		editSMS = (EditText) findViewById(R.id.editSMS);
        
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                MINIMUM_TIME_BETWEEN_UPDATES, 
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MyLocationListener()
        );
        
    retrieveSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //showCurrentLocation();
            	sendLocation();
            }
    });        
        
    }    
//    
//    @SuppressWarnings("deprecation")
//	public void turnGPSOn()
//    {
//         Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
//         intent.putExtra("enabled", true);
//         this.ctx.sendBroadcast(intent);
//
//        String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//        if(!provider.contains("gps")){ //if gps is disabled
//            final Intent poke = new Intent();
//            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
//            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//            poke.setData(Uri.parse("3")); 
//            this.ctx.sendBroadcast(poke);
//
//
//        }
//    }
//    // automatic turn off the gps
//    public void turnGPSOff()
//    {
//        String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//        if(provider.contains("gps")){ //if gps is enabled
//            final Intent poke = new Intent();
//            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//            poke.setData(Uri.parse("3")); 
//            this.ctx.sendBroadcast(poke);
//        }
//    }
    
    protected void sendLocation(){
    	
    	Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	
    	if (location != null) {
            String localization = String.format(
                    "Current Location \n Latitude: %1$s \n Longitude: %2$s",
                    location.getLatitude(),location.getLongitude()
            );
            sendEmergencySms(localization);
            //
        }else{
        	String message ="Current Location not found";
            Toast.makeText(Geoloc.this, message,
                    Toast.LENGTH_LONG).show();
        }
    }
    
    protected void sendEmergencySms(String message){
    	
    	String phoneNo = editPhoneNum.getText().toString();
		String sms = editSMS.getText().toString();
		
		sms=sms+"/t" + message;

		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNo, null, sms, null, null);
			Toast.makeText(getApplicationContext(), "SMS Sent!",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"SMS faild, please try again later!",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
    }
 
     protected void showCurrentLocation() {

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            String message = String.format(
            		"Current Location \n Latitude: %1$s \n Longitude: %2$s",
                    location.getLatitude(),location.getLongitude()
            );
            Toast.makeText(Geoloc.this, message,
                    Toast.LENGTH_LONG).show();
        }else{
        	String message ="Current Location not found";
            Toast.makeText(Geoloc.this, message,
                    Toast.LENGTH_LONG).show();
        }

    }   

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String message = String.format(
            		"Current Location \n Latitude: %1$s \n Longitude: %2$s",
                    location.getLatitude(),location.getLongitude()
            );
            Toast.makeText(Geoloc.this, message, Toast.LENGTH_LONG).show();
        }
        
        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(Geoloc.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(Geoloc.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(Geoloc.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }

    }
    
}

