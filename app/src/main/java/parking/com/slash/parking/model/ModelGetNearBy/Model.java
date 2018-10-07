package parking.com.slash.parking.model.ModelGetNearBy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model
{
    @Expose
    @SerializedName("address")
    private String address;
    @Expose
    @SerializedName("leaverMobile")
    private String leavermobile;
    @Expose
    @SerializedName("leaverCarImage")
    private String leavercarimage;
    @Expose
    @SerializedName("leaverCarNo")
    private String leavercarno;
    @Expose
    @SerializedName("leaverCarColor")
    private String leavercarcolor;
    @Expose
    @SerializedName("leaverCarModel")
    private String leavercarmodel;
    @Expose
    @SerializedName("leaverCarBrand")
    private String leavercarbrand;
    @Expose
    @SerializedName("leaverName")
    private String leavername;
    @Expose
    @SerializedName("latitude")
    private double latitude;
    @Expose
    @SerializedName("longitude")
    private double longitude;
    @Expose
    @SerializedName("fees")
    private int fees;
    @Expose
    @SerializedName("leavingTime")
    private String leavingtime;
    @Expose
    @SerializedName("slotType")
    private int slottype;
    @Expose
    @SerializedName("requestID")
    private String requestid;

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getLeavermobile()
    {
        return leavermobile;
    }

    public void setLeavermobile(String leavermobile)
    {
        this.leavermobile = leavermobile;
    }

    public String getLeavercarimage()
    {
        return leavercarimage;
    }

    public void setLeavercarimage(String leavercarimage)
    {
        this.leavercarimage = leavercarimage;
    }

    public String getLeavercarno()
    {
        return leavercarno;
    }

    public void setLeavercarno(String leavercarno)
    {
        this.leavercarno = leavercarno;
    }

    public String getLeavercarcolor()
    {
        return leavercarcolor;
    }

    public void setLeavercarcolor(String leavercarcolor)
    {
        this.leavercarcolor = leavercarcolor;
    }

    public String getLeavercarmodel()
    {
        return leavercarmodel;
    }

    public void setLeavercarmodel(String leavercarmodel)
    {
        this.leavercarmodel = leavercarmodel;
    }

    public String getLeavercarbrand()
    {
        return leavercarbrand;
    }

    public void setLeavercarbrand(String leavercarbrand)
    {
        this.leavercarbrand = leavercarbrand;
    }

    public String getLeavername()
    {
        return leavername;
    }

    public void setLeavername(String leavername)
    {
        this.leavername = leavername;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public int getFees()
    {
        return fees;
    }

    public void setFees(int fees)
    {
        this.fees = fees;
    }

    public String getLeavingtime()
    {
        return leavingtime;
    }

    public void setLeavingtime(String leavingtime)
    {
        this.leavingtime = leavingtime;
    }

    public int getSlottype()
    {
        return slottype;
    }

    public void setSlottype(int slottype)
    {
        this.slottype = slottype;
    }

    public String getRequestid()
    {
        return requestid;
    }

    public void setRequestid(String requestid)
    {
        this.requestid = requestid;
    }
}
