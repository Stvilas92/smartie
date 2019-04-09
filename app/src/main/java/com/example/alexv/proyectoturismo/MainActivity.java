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
import com.here.android.mpa.common.GeoPosition;
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

    private AutoCompleteTextView textBoxLocation,textBoxSaveLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getJSON();
        initialize();
        textBoxLocation = findViewById(R.id.TextBoxLocation);
        textBoxSaveLocation = findViewById(R.id.textBoxSaveLocation);

        Button buttonLocate = findViewById(R.id.btnLocate);
        buttonLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLocation();
            }
        });

        Button buttonSave = findViewById(R.id.btnSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewGeoCoordinate();
            }
        });


        package com.example;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

        public class NuevaRuta {

            ArrayList<String> places = new ArrayList<String>();
            places.add("id");
            places.add("name");
            places.add("latitude");
            places.add("Longitude");
            places.add("contry_id");
            places.add("zone_id");
            places.add("city_id");
            places.add("subcategory_id");
            places.add("distance");
        }

            @SerializedName("id":"2662972")
            @Expose
            public String id;
            @SerializedName("name":"Porto 7 Coctelería")
            @Expose
            public String name;
            @SerializedName("latitude":"42.238276769706")
            @Expose
            public String latitude;
            @SerializedName("longitude":"-8.716521528884")
            @Expose
            public String longitude;
            @SerializedName("country_id":"63")
            @Expose
            public String countryId;
            @SerializedName("zone_id":"1025")
            @Expose
            public String zoneId;
            @SerializedName("city_id":"1268")
            @Expose
            public String cityId;
            @SerializedName("subcategory_id":"0")
            @Expose
            public String subcategoryId;
            @SerializedName("distance")
            @Expose
            public String distance;

        for (int i = 0; i < 4; i++) {
            List<String> places = Arrays.asList("id", "name", "latitude", "Longitude", "Country_id", "zone_id", "city_id", "subcategory_id", "distance");
        }   





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
        int index = -1;

        for (int i = 0 ; i< zoneList.size() ; i++){
            if(textBoxLocation.getText().toString().equals(zoneList.get(i))){
                index = i;
            }
        }

        if(index == -1){
            Toast.makeText(getApplicationContext(), "No existe esta localización \n Puedes guardar la ubicación actual o una que señales en el mapa", Toast.LENGTH_LONG);
            setNewGeoCoordinate();
            return;
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

    private void setNewGeoCoordinate(){
               // map.getPositionIndicator();
               zoneList.add(textBoxLocation.getText().toString());
               zoneLatitud.add(map.getCenter().getLatitude());
               zoneLongitud.add(map.getCenter().getLongitude());
               mapFragment.getMap().addMapObject(new MapMarker(new GeoCoordinate(zoneLatitud.get(zoneLatitud.size()-1),zoneLongitud.get(zoneLongitud.size()-1))));

    }

}
