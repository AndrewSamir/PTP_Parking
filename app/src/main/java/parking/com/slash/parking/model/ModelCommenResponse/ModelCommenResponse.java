package parking.com.slash.parking.model.ModelCommenResponse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andre on 21-Jan-18.
 */

public class ModelCommenResponse
{

    @SerializedName("status")
    private String Status;
    @SerializedName("message")
    private String ResponseMessage;

    @SerializedName("responseObj")
    private Object Data;

    public String getStatus()
    {
        return Status;
    }

    public void setStatus(String Status)
    {
        this.Status = Status;
    }

    public String getResponseMessage()
    {
        return ResponseMessage;
    }

    public void setResponseMessage(String ResponseMessage)
    {
        this.ResponseMessage = ResponseMessage;
    }

    public Object getData()
    {
        return Data;
    }

    public void setData(Object data)
    {
        Data = data;
    }
}
