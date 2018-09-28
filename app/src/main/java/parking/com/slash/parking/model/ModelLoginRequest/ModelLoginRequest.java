package parking.com.slash.parking.model.ModelLoginRequest;

import com.google.gson.annotations.SerializedName;

public class ModelLoginRequest {


    @SerializedName("Username")
    private String username;
    @SerializedName("Password")
    private String password;
    @SerializedName("DeviceToken")
    private String devicetoken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }
}
