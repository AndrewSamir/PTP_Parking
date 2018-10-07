package parking.com.slash.parking.model.ModelGetNearBy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelGetNearByRequest
{

    @Expose
    @SerializedName("Price")
    private int price;
    @Expose
    @SerializedName("Type")
    private int type;
    @Expose
    @SerializedName("Radius")
    private String radius;
    @Expose
    @SerializedName("Latitude")
    private String latitude;
    @Expose
    @SerializedName("Longitude")
    private String longitude;

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getRadius()
    {
        return radius;
    }

    public void setRadius(String radius)
    {
        this.radius = radius;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }
}
