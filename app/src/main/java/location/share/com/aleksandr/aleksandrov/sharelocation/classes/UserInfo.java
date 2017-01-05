package location.share.com.aleksandr.aleksandrov.sharelocation.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;

/**
 * Created by Aleksandr on 10/27/2016.
 */

public class UserInfo implements Parcelable {
    private int mId;
    private String mName = null;
    private String mFio;
    private String mPhoneNumber;

    private LatLng mLatLng;
    private double mLatitude;
    private double mLongitude;
    private double mSpeed;
    private Marker mMarker;
    private MarkerOptions mMarkerOptions;

    public UserInfo() {

    }

    public UserInfo(int id, String name, String fio, String phoneNumber) {
        mId = id;
        mName = name;
        mFio = fio;
        mPhoneNumber = phoneNumber;
    }

    public String getFio() {
        return mFio;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public void setFio(String fio) {
        this.mFio = fio;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public double getSpeed() {
        return mSpeed;
    }

    public Marker getMarker() {
        mMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.gt_2_42px));
        return mMarker;
    }

    public MarkerOptions getMarkerOptions() {
        return mMarkerOptions;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setLatLng(LatLng latLng) {
        this.mLatLng = latLng;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public void setSpeed(double speed) {
        this.mSpeed = speed;
    }

    public void setMarker(Marker marker) {
        this.mMarker = marker;
    }

    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.mMarkerOptions = markerOptions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mName);
        parcel.writeString(mFio);
        parcel.writeString(mPhoneNumber);
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {

        @Override
        public UserInfo createFromParcel(Parcel parcel) {
            return new UserInfo(parcel);
        }

        @Override
        public UserInfo[] newArray(int i) {
            return new UserInfo[0];
        }
    };

    private UserInfo(Parcel parcel) {
        mId = parcel.readInt();
        mName = parcel.readString();
        mFio = parcel.readString();
        mPhoneNumber = parcel.readString();
    }
}
