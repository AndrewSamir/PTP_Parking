package parking.com.slash.parking.model.ModelUserProfile;

import com.google.gson.annotations.SerializedName;

public class Model {
    @SerializedName("carBrand")
    private String carbrand;
    @SerializedName("carBrandID")
    private String carbrandid;
    @SerializedName("carColor")
    private String carcolor;
    @SerializedName("carImage")
    private String carimage;
    @SerializedName("carModel")
    private String carmodel;
    @SerializedName("carModelID")
    private String carmodelid;
    @SerializedName("carNo")
    private String carno;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("name")
    private String name;
    @SerializedName("wallet")
    private int wallet;

    public String getCarbrand() {
        return carbrand;
    }

    public void setCarbrand(String carbrand) {
        this.carbrand = carbrand;
    }

    public String getCarbrandid() {
        return carbrandid;
    }

    public void setCarbrandid(String carbrandid) {
        this.carbrandid = carbrandid;
    }

    public String getCarcolor() {
        return carcolor;
    }

    public void setCarcolor(String carcolor) {
        this.carcolor = carcolor;
    }

    public String getCarimage() {
        return carimage;
    }

    public void setCarimage(String carimage) {
        this.carimage = carimage;
    }

    public String getCarmodel() {
        return carmodel;
    }

    public void setCarmodel(String carmodel) {
        this.carmodel = carmodel;
    }

    public String getCarmodelid() {
        return carmodelid;
    }

    public void setCarmodelid(String carmodelid) {
        this.carmodelid = carmodelid;
    }

    public String getCarno() {
        return carno;
    }

    public void setCarno(String carno) {
        this.carno = carno;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }
}
