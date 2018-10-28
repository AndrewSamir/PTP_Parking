package parking.com.slash.parking.model.ModelHistory;

import com.google.gson.annotations.SerializedName;

public class Model {
    @SerializedName("carImage")
    private String carimage;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("area")
    private String area;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("requestID")
    private String requestid;
    @SerializedName("fees")
    private int fees;
    @SerializedName("userID")
    private String userid;
    @SerializedName("type")
    private int type;
    @SerializedName("requestDate")
    private String requestdate;

    public String getCarimage() {
        return carimage;
    }

    public void setCarimage(String carimage) {
        this.carimage = carimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(String requestdate) {
        this.requestdate = requestdate;
    }
}
