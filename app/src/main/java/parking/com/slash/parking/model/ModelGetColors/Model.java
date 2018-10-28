package parking.com.slash.parking.model.ModelGetColors;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Model {
    @SerializedName("users")
    private List<Users> users;
    @SerializedName("carColorID")
    private String carcolorid;
    @SerializedName("colorName")
    private String colorname;

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public String getCarcolorid() {
        return carcolorid;
    }

    public void setCarcolorid(String carcolorid) {
        this.carcolorid = carcolorid;
    }

    public String getColorname() {
        return colorname;
    }

    public void setColorname(String colorname) {
        this.colorname = colorname;
    }
}
