package parking.com.slash.parking.model.ModelConfirmRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelConfirmRequest implements Serializable
{

    @Expose
    @SerializedName("model")
    private Model model;

    public Model getModel()
    {
        return model;
    }

    public void setModel(Model model)
    {
        this.model = model;
    }
}
