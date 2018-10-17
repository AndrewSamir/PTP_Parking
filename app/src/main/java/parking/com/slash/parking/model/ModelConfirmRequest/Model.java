package parking.com.slash.parking.model.ModelConfirmRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model
{
    @Expose
    @SerializedName("currentBalance")
    private String currentbalance;
    @Expose
    @SerializedName("fees")
    private String fees;
    @Expose
    @SerializedName("status")
    private int status;

    public String getCurrentbalance()
    {
        return currentbalance;
    }

    public void setCurrentbalance(String currentbalance)
    {
        this.currentbalance = currentbalance;
    }

    public String getFees()
    {
        return fees;
    }

    public void setFees(String fees)
    {
        this.fees = fees;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}
