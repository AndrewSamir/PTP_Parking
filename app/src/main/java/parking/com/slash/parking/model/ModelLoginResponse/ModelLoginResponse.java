package parking.com.slash.parking.model.ModelLoginResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelLoginResponse
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
