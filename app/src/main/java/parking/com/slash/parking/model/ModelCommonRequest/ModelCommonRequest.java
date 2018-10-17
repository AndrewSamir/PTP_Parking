package parking.com.slash.parking.model.ModelCommonRequest;

import com.google.gson.annotations.SerializedName;

public class ModelCommonRequest
{


    @SerializedName("Email")
    private String email;
    @SerializedName("Mobile")
    private String mobile;
    @SerializedName("RequestID")
    private String RequestID;
    @SerializedName("IsSeeker")
    private Boolean IsSeeker;
    @SerializedName("PaymentMethod")
    private int PaymentMethod;

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getRequestID()
    {
        return RequestID;
    }

    public void setRequestID(String requestID)
    {
        RequestID = requestID;
    }

    public Boolean getSeeker()
    {
        return IsSeeker;
    }

    public void setSeeker(Boolean seeker)
    {
        IsSeeker = seeker;
    }

    public int getPaymentMethod()
    {
        return PaymentMethod;
    }

    public void setPaymentMethod(int paymentMethod)
    {
        PaymentMethod = paymentMethod;
    }
}
