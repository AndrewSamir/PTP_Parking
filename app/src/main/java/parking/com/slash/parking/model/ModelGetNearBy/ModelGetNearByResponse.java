package parking.com.slash.parking.model.ModelGetNearBy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ModelGetNearByResponse implements Serializable
{


    @Expose
    @SerializedName("model")
    private List<Model> model;

    public List<Model> getModel()
    {
        return model;
    }

    public void setModel(List<Model> model)
    {
        this.model = model;
    }
}
