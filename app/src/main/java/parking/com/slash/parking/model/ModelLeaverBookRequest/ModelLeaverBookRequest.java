package parking.com.slash.parking.model.ModelLeaverBookRequest;

import com.google.gson.annotations.SerializedName;

public class ModelLeaverBookRequest {


    @SerializedName("Fees")
    private int fees;
    @SerializedName("Longitude")
    private String longitude;
    @SerializedName("Latitude")
    private String latitude;
    @SerializedName("Address")
    private String address;
    @SerializedName("Area")
    private String Area;
    @SerializedName("LeavingTime")
    private String leavingtime;
    @SerializedName("Type")
    private int type;

    public void setArea(String area) {
        Area = area;
    }

    public String getArea() {
        return Area;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLeavingtime() {
        return leavingtime;
    }

    public void setLeavingtime(String leavingtime) {
        this.leavingtime = leavingtime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
