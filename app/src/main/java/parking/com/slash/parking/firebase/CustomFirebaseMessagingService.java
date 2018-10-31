package parking.com.slash.parking.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.MainActivity;
import parking.com.slash.parking.utlities.DataEnum;


public class CustomFirebaseMessagingService extends FirebaseMessagingService
{

    private static final String TAG = CustomFirebaseMessagingService.class.getSimpleName();
    //private String endpageLink = "adverts/";
    private String message = "";
    private String title = "";
    // private String advert_id = "";
    private String type = "";
    private String pathientName = "";
    private String visitUuid = "";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {


        String reservationID = null;
        String playgroundID = null;
        String playGroundName = null;


        Log.d("notification", remoteMessage.getNotification().toString());
        if (remoteMessage.getData().size() > 0)
        {
          /*  reservationID = remoteMessage.getData().get("ReservationID");
            playgroundID = remoteMessage.getData().get("PlayGroundIDHashed");
            playGroundName = remoteMessage.getData().get("PlayGroundName");
*/
            NotificationData notificationData = new NotificationData(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), "default", playgroundID, reservationID, playGroundName);
            sendNotification(notificationData);

        }

        //=======================================================================//
      /*  Intent intentBroadCast = new Intent("order_chnaged_statuss");
        // intent.putExtra("value", "test");
        sendBroadcast(intentBroadCast);
*/
    }


//            this.sendNotification(new NotificationData(title, body, sound, notification_type_id, item_uuid));


    private void sendNotification(NotificationData notificationData)
    {

        PendingIntent pendingIntent;
        pendingIntent = intentPending(notificationData);


        NotificationCompat.Builder notificationBuilder = null;
        try
        {

            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(URLDecoder.decode(notificationData.getTitle(), "UTF-8"))
                    .setContentText(URLDecoder.decode(notificationData.getBody(), "UTF-8"))
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent);

        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        if (notificationBuilder != null)
        {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        } else
        {
            Log.d(TAG, "Não foi possível criar objeto notificationBuilder");
        }
    }

    private PendingIntent intentPending(NotificationData notificationData)
    {
        Intent intent = null;


        intent = new Intent(this, MainActivity.class);
//        intent.putExtra(DataEnum.extraReservationID.name(), notificationData.getReservationID());
//        intent.putExtra(DataEnum.extraPlaygroundID.name(), notificationData.getPlaygroundID());
//        SingletonMalaaby.getInstance().setMalaabyId(notificationData.getPlaygroundID());
//        SingletonMalaaby.getInstance().setMalaabyName(notificationData.getPlayGroundName());
        Log.d("notification", notificationData.getPlaygroundID());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        return pendingIntent;


    }


}
