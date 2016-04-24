package com.asa.findmyvolunteer;

import com.backendless.geo.GeoPoint;

/**
 * Created by Anush on 24-04-2016.
 */
public class VictimData {
    private String objectId;
    private String name;
    private String phone;
    public GeoPoint location;
    private String sit;
    private String req;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId( String objectId ) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
    }

    public String getSit() {
        return sit;
    }

    public void setSit( String title ) {
        this.sit = title;
    }
    public String getReq() {
        return req;
    }

    public void setReq( String req ) {
        this.req = req;
    }
    public GeoPoint getLocation()
    {
        return location;
    }

    public void setLocation( GeoPoint location )
    {
        this.location = location;
    }
}
