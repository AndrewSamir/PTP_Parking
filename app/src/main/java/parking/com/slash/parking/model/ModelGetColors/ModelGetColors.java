package parking.com.slash.parking.model.ModelGetColors;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelGetColors {


    @SerializedName("model")
    private List<Model> model;

    public List<Model> getModel() {
        return model;
    }

    public void setModel(List<Model> model) {
        this.model = model;
    }
}
