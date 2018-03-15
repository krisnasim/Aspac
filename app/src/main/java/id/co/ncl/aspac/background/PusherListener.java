package id.co.ncl.aspac.background;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.ncl.aspac.R;

/**
 * Created by Jonathan Simananda on 02/03/2018.
 */

public class PusherListener extends IntentService {

    private Pusher pusher;
    private Channel channel;

    public PusherListener() {
        super("pusher_listener");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //trying pusher
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");

        //pusher = new Pusher("3d6a42461cacee58e688", options);
        pusher = new Pusher("022752ce180999f4a3ed", options);
        //pusher.connect();

        //channel = pusher.subscribe("my-channel");
        channel = pusher.subscribe("android-notification-channel");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Gets data from the incoming Intent
        String dataString = intent.getDataString();

        // Do work here, based on the contents of dataString

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        //Toast.makeText(this, "Listening!", Toast.LENGTH_SHORT).show();

        //channel.bind("my-event", new SubscriptionEventListener() {
        channel.bind("new-service-request", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                //Toast.makeText(getApplicationContext(), "Capturing!", Toast.LENGTH_SHORT).show();
                Log.d("data", data);
                JSONObject dataJSON;
                try {
                    dataJSON = new JSONObject(data);
                    Log.d("createNotif", "Creating new notification!");

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext(), "001")
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Aspac New Job!")
                                    .setContentText(dataJSON.getString("message"));

                    // Sets an ID for the notification
                    int mNotificationId = 001;
                    // Gets an instance of the NotificationManager service
                    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    // Builds the notification and issues it.
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.d("connectLog", "State changed to " + change.getCurrentState() +
                        " from " + change.getPreviousState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.d("ErrorCon", "There was a problem connecting!");
            }
        }, ConnectionState.ALL);

        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
}
