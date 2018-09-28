package parking.com.slash.parking.model.ModelCommonRequest;

import com.google.gson.annotations.SerializedName;

public class ModelCommonRequest {


    @SerializedName("Email")
    private String email;
    @SerializedName("Mobile")
    private String mobile;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
