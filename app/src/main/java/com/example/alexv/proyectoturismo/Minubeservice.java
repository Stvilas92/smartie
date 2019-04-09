package com.example.alexv.proyectoturismo;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Minubeservice {

    @SerializedName("id":"2662972")
    @Expose
    public String id;
    @SerializedName("name":"Porto 7 Coctelería")
    @Expose
    public String name;*/
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

    for (string[ArrayList<pois>] = 0; [ArrayList<pois>pois.length]; [Arrraylist<pois>]pois++)
    {
        List<String> places = Arrays.asList("id", "name", "latitude", "Longitude", "Country_id", "zone_id", "city_id", "subcategory_id", "distance");

        if()
            @GET("pois")
            Call<NoticeList> getPois(@Query("longitud"));

            @GET("pois")
            Call<NoticeList> getPois(@Query("longitud"));
    }
}