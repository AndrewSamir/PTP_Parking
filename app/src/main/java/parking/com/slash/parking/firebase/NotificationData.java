package parking.com.slash.parking.firebase;

/**
 * Created by ksi on 31-Jul-17.
 */

public class NotificationData {

    private String title, body, sound, id, type;

    public NotificationData(String title, String body, String sound, String id, String type) {
        this.title = title;
        this.body = body;
        this.sound = sound;
        this.id = id;
        this.type = type;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
