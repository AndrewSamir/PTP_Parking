package parking.com.slash.parking.model.ModelGetNearBy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelGetNearByRequest
{


    @Expose
    @SerializedName("PriceFrom")
    private int PriceFrom;
    @Expose
    @SerializedName("PriceTo")
    private int PriceTo;
    @Expose
    @SerializedName("Type")
    private int type;
    @Expose
    @SerializedName("Time")
    private int Time;
    @Expose
    @SerializedName("Radius")
    private String radius;
    @Expose
    @SerializedName("Latitude")
    private String latitude;
    @Expose
    @SerializedName("Longitude")
    private String longitude;


    public int getPriceFrom()
    {
        return PriceFrom;
    }

    public void setPriceFrom(int priceFrom)
    {
        PriceFrom = priceFrom;
    }

    public int getPriceTo()
    {
        return PriceTo;
    }

    public void setPriceTo(int priceTo)
    {
        PriceTo = priceTo;
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

    public int getTime()
    {
        return Time;
    }

    public void setTime(int time)
    {
        Time = time;
    }
}
