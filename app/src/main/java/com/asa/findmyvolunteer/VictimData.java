package com.asa.findmyvolunteer;

import android.os.Parcel;
import android.os.Parcelable;

import com.backendless.geo.GeoPoint;

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

    protected VictimData(Parcel in) {
        objectId = in.readString();
        name = in.readString();
        phone = in.readString();
        sit = in.readString();
        req = in.readString();
    }

    public static final Creator<VictimData> CREATOR = new Creator<VictimData>() {
        @Override
        public VictimData createFromParcel(Parcel in) {
            return new VictimData(in);
        }

        @Override
        public VictimData[] newArray(int size) {
            return new VictimData[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(objectId);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(sit);
        dest.writeString(req);
    }
}
