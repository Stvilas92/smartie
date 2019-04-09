package com.example.alexv.proyectoturismo;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.internal.restrouting.Route;
import com.here.android.mpa.internal.restrouting.Waypoint;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.SupportMapFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> zoneList = new ArrayList<>();
    ArrayList<Double> zoneLatitud = new ArrayList<>();
    ArrayList<Double> zoneLongitud = new ArrayList<>();


    // map embedded in the map fragment
    private Map map = null;

    // map fragment embedded in this activity
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getJSON();
        initialize();

        Button button = findViewById(R.id.btnLocate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLocation();
            }
        });




    }

    public void getJSON(){
        String json;

        try{
            InputStream is = getAssets().open("location.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer,"UTF-8");

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0 ; i< jsonArray.length() ; i++){
               JSONObject object = jsonArray.getJSONObject(i);

               if(object.getString("name") != ""){
                    zoneList.add(object.getString("name"));
                    zoneLatitud.add(Double.parseDouble(object.getString("latitude")));
                    zoneLongitud.add(Double.parseDouble(object.getString("longitude")));
               }
            }

        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void goToLocation(){
        AutoCompleteTextView textBoxLocation = findViewById(R.id.TextBoxLocation);
        int index = -1;

        for (int i = 0 ; i< zoneList.size() ; i++){
            if(textBoxLocation.getText().toString().equals(zoneList.get(i))){
                index = i;
            }
        }

        if(index == -1){
            Toast.makeText(getApplicationContext(), "Unable to get the specified location", Toast.LENGTH_LONG);
        }

        map.setCenter(new GeoCoordinate(zoneLatitud.get(index),zoneLongitud.get(index)),Map.Animation.NONE);

    }

    private void initialize() {
        setContentView(R.layout.activity_main);
        // Search for the map fragment to finish setup by calling init().
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);

        // Set up disk cache path for the map service for this application
        // It is recommended to use a path under your application folder for storing the disk cache

        boolean success = true;
        /*boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
                getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps",
                "{YOUR_INTENT_NAME}");*/

        if (!success) {
            Toast.makeText(getApplicationContext(), "Unable to set isolated disk cache path.", Toast.LENGTH_LONG);
        } else {
            mapFragment.init(this.getApplicationContext(),new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                    if (error == OnEngineInitListener.Error.NONE) {
                        // retrieve a reference of the map from the map fragment
                        map = mapFragment.getMap();
                        // Set the map center
                        map.setCenter(new GeoCoordinate(zoneLatitud.get(0), zoneLongitud.get(0)),
                                Map.Animation.NONE);
                        // Set the zoom level to the average between min and max
                        map.setZoomLevel( map.getMaxZoomLevel());

                        //Set mark interest points
                        for (int i = 0 ; i< zoneList.size() ; i++){
                            map.addMapObject(new MapMarker(new GeoCoordinate(zoneLatitud.get(i),zoneLongitud.get(i))));
                        }
                    } else {
                        System.out.println("ERROR: Cannot initialize Map Fragment");
                    }
                }
            });
        }
    }
}
