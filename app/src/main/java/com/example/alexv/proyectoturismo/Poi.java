package com.example.alexv.proyectoturismo;

import com.google.gson.annotations.SerializedName;

public class Poi {

    @SerializedName("id")
    public String id;
    @SerializedName("name")
    
    public String name;
    @SerializedName("latitude")
    
    public String latitude;
    @SerializedName("longitude")
    
    public String longitude;
    @SerializedName("country_id")
    
    public String countryId;
    @SerializedName("zone_id")
    
    public String zoneId;
    @SerializedName("city_id")
    
    public String cityId;
    @SerializedName("subcategory_id")
    
    public String subcategoryId;
    @SerializedName("distance")
    
    public String distance;

}