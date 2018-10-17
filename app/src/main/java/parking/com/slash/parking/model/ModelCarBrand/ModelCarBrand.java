package parking.com.slash.parking.model.ModelCarBrand;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelCarBrand
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
