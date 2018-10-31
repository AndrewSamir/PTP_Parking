package parking.com.slash.parking.model.ModelNotifications;

import com.google.gson.annotations.SerializedName;

public class Model {
    @SerializedName("notificationID")
    private String notificationid;
    @SerializedName("notificationTitle")
    private String notificationtitle;
    @SerializedName("notificationBody")
    private String notificationbody;
    @SerializedName("notificationDate")
    private String notificationdate;

    public String getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(String notificationid) {
        this.notificationid = notificationid;
    }

    public String getNotificationtitle() {
        return notificationtitle;
    }

    public void setNotificationtitle(String notificationtitle) {
        this.notificationtitle = notificationtitle;
    }

    public String getNotificationbody() {
        return notificationbody;
    }

    public void setNotificationbody(String notificationbody) {
        this.notificationbody = notificationbody;
    }

    public String getNotificationdate() {
        return notificationdate;
    }

    public void setNotificationdate(String notificationdate) {
        this.notificationdate = notificationdate;
    }
}
