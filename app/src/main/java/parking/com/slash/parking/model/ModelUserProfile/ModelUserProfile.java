package parking.com.slash.parking.model.ModelUserProfile;

import com.google.gson.annotations.SerializedName;

public class ModelUserProfile {


    @SerializedName("model")
    private Model model;

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
