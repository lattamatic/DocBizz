        package com.example.docbizz;


        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.TaskStackBuilder;
        import android.content.Context;
        import android.content.Intent;
        import android.media.RingtoneManager;
        import android.net.Uri;
        import android.support.v4.app.NotificationCompat;
        import android.util.Log;

        import gcm.GCMBaseIntentService;
        import util.data;

        import org.json.JSONObject;


public class GCMIntentService extends GCMBaseIntentService {

    public GCMIntentService() {
        super(data.SENDER_ID);
    }

    private static final String TAG = "GCMIntentService";

    @Override
    protected void onRegistered(Context arg0, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        // write gcm key push code here
    }


    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        Log.i(TAG, "unregistered = " + arg1);
    }


    private void notify( String title, String text){
        Log.i("title", title);
        Log.i("text", text);
        Uri sound_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //builder.setSound(alarmSound);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon( R.drawable.ic_launcher )
                        .setContentTitle( title )
                        .setContentText( text )
                        .setSound( sound_uri );
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Home.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        //int mId = 1234;
        mNotificationManager.notify(1234, mBuilder.build());
    }

    @SuppressWarnings("deprecation")
    @ Override
    protected void onMessage(Context context, Intent intent) {
        Log.d(TAG, "onMessage - context: " + context);
        String msg = intent.getStringExtra("message");
        try {
            JSONObject msgJSON = new JSONObject(msg);
            String type = msgJSON.getString("type");
            String text = msgJSON.getString("text");
            String title = msgJSON.getString("title");
            notify(title, text);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        // make notifications here
        Log.i("gcm", "received");

    }

    @
            Override
    protected void onError(Context arg0, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @
            Override
    protected boolean onRecoverableError(Context context, String errorId) {
        return super.onRecoverableError(context, errorId);
    }
}