package parking.com.slash.parking.model.ModelLoginResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model
{
    @Expose
    @SerializedName("isPaymentVerified")
    private boolean ispaymentverified;
    @Expose
    @SerializedName("carColor")
    private String carcolor;
    @Expose
    @SerializedName("carBrandID")
    private String carbrandid;
    @Expose
    @SerializedName("carModelID")
    private String carmodelid;
    @Expose
    @SerializedName("carBrand")
    private String carbrand;
    @Expose
    @SerializedName("carModel")
    private String carmodel;
    @Expose
    @SerializedName("carNumber")
    private String carnumber;
    @Expose
    @SerializedName("issued")
    private String issued;
    @Expose
    @SerializedName("userName")
    private String username;
    @Expose
    @SerializedName("clientId")
    private String clientid;
    @Expose
    @SerializedName("refresh_token")
    private String refreshToken;
    @Expose
    @SerializedName("expiresIn")
    private String expiresin;
    @Expose
    @SerializedName("tokenType")
    private String tokentype;
    @Expose
    @SerializedName("accessToken")
    private String accesstoken;
    @Expose
    @SerializedName("tokenExpiry")
    private String tokenexpiry;
    @Expose
    @SerializedName("carImage")
    private String carimage;
    @Expose
    @SerializedName("mobile")
    private String mobile;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("userID")
    private String userid;

    public boolean getIspaymentverified()
    {
        return ispaymentverified;
    }

    public void setIspaymentverified(boolean ispaymentverified)
    {
        this.ispaymentverified = ispaymentverified;
    }

    public String getCarcolor()
    {
        return carcolor;
    }

    public void setCarcolor(String carcolor)
    {
        this.carcolor = carcolor;
    }

    public String getCarbrandid()
    {
        return carbrandid;
    }

    public void setCarbrandid(String carbrandid)
    {
        this.carbrandid = carbrandid;
    }

    public String getCarmodelid()
    {
        return carmodelid;
    }

    public void setCarmodelid(String carmodelid)
    {
        this.carmodelid = carmodelid;
    }

    public String getCarbrand()
    {
        return carbrand;
    }

    public void setCarbrand(String carbrand)
    {
        this.carbrand = carbrand;
    }

    public String getCarmodel()
    {
        return carmodel;
    }

    public void setCarmodel(String carmodel)
    {
        this.carmodel = carmodel;
    }

    public String getCarnumber()
    {
        return carnumber;
    }

    public void setCarnumber(String carnumber)
    {
        this.carnumber = carnumber;
    }

    public String getIssued()
    {
        return issued;
    }

    public void setIssued(String issued)
    {
        this.issued = issued;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getClientid()
    {
        return clientid;
    }

    public void setClientid(String clientid)
    {
        this.clientid = clientid;
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken)
    {
        this.refreshToken = refreshToken;
    }

    public String getExpiresin()
    {
        return expiresin;
    }

    public void setExpiresin(String expiresin)
    {
        this.expiresin = expiresin;
    }

    public String getTokentype()
    {
        return tokentype;
    }

    public void setTokentype(String tokentype)
    {
        this.tokentype = tokentype;
    }

    public String getAccesstoken()
    {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken)
    {
        this.accesstoken = accesstoken;
    }

    public String getTokenexpiry()
    {
        return tokenexpiry;
    }

    public void setTokenexpiry(String tokenexpiry)
    {
        this.tokenexpiry = tokenexpiry;
    }

    public String getCarimage()
    {
        return carimage;
    }

    public void setCarimage(String carimage)
    {
        this.carimage = carimage;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUserid()
    {
        return userid;
    }

    public void setUserid(String userid)
    {
        this.userid = userid;
    }
}
