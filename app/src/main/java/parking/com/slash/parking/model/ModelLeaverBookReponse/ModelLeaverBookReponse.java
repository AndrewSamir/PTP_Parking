package parking.com.slash.parking.model.ModelLeaverBookReponse;

import com.google.gson.annotations.SerializedName;

public class ModelLeaverBookReponse {

    @SerializedName("model")
    private String model;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
