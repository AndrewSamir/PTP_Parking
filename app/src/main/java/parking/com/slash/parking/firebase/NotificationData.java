package parking.com.slash.parking.firebase;

/**
 * Created by ksi on 31-Jul-17.
 */

public class NotificationData {

    private String title,body,sound,playgroundID,reservationID,playGroundName;

    public NotificationData(String title, String body, String sound, String playgroundID, String reservationID, String playGroundName)
    {
        this.title = title;
        this.body = body;
        this.sound = sound;
        this.playgroundID = playgroundID;
        this.reservationID = reservationID;
        this.playGroundName = playGroundName;
    }

    public String getPlaygroundID()
    {
        return playgroundID;
    }

    public void setPlaygroundID(String playgroundID)
    {
        this.playgroundID = playgroundID;
    }

    public String getReservationID()
    {
        return reservationID;
    }

    public void setReservationID(String reservationID)
    {
        this.reservationID = reservationID;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String getSound()
    {
        return sound;
    }

    public void setSound(String sound)
    {
        this.sound = sound;
    }

    public String getPlayGroundName()
    {
        return playGroundName;
    }

    public void setPlayGroundName(String playGroundName)
    {
        this.playGroundName = playGroundName;
    }
}
