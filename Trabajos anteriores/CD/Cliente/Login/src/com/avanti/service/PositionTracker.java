package com.avanti.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;



public class PositionTracker extends Service {

    private LocationManager lm;
    private LocationListener locationListener;

    public IBinder onBind(Intent intent) {
    	  lm = (LocationManager) 
          getSystemService(Context.LOCATION_SERVICE);    
      
      locationListener = new MyLocationListener();
      
      lm.requestLocationUpdates(
          LocationManager.GPS_PROVIDER, 
          0, 
          0, 
          locationListener);        
    	  return null;
    }
    
    
    private class MyLocationListener implements LocationListener 
    {
        //@Override
        public void onLocationChanged(Location loc) {
            if (loc != null) {
                Toast.makeText(getBaseContext(), 
                    "Location changed : Lat: " + loc.getLatitude() + 
                    " Lng: " + loc.getLongitude(), 
                    Toast.LENGTH_SHORT).show();
            }
        }

        //@Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        //@Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        //@Override
        public void onStatusChanged(String provider, int status, 
            Bundle extras) {
            // TODO Auto-generated method stub
        }
    }        
 
}



