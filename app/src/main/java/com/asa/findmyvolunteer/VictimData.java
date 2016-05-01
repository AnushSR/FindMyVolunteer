package com.asa.findmyvolunteer;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.backendless.geo.GeoPoint;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Anush on 24-04-2016.
 */
public class VictimData implements Parcelable {
    private String objectId;
    private String name;
    private String phone;
    public GeoPoint location;
    private String sit;
    private String req;
    public VictimData(){}


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

    public LatLng getLatLng(){
        return new LatLng(this.location.getLatitude(),this.location.getLongitude());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.objectId);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeDouble(location.getLatitude());
        dest.writeDouble(location.getLongitude());
        dest.writeString(this.sit);
        dest.writeString(this.req);
    }

    protected VictimData(Parcel in) {
        this.objectId = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.location=new GeoPoint(in.readDouble(),in.readDouble());
        this.sit = in.readString();
        this.req = in.readString();
    }

    public static final Parcelable.Creator<VictimData> CREATOR = new Parcelable.Creator<VictimData>() {
        @Override
        public VictimData createFromParcel(Parcel source) {
            return new VictimData(source);
        }

        @Override
        public VictimData[] newArray(int size) {
            return new VictimData[size];
        }
    };
}
