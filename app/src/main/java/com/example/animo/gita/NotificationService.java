package com.example.animo.gita;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.animo.gita.activity.CommitsDetailActivity;
import com.example.animo.gita.activity.MainActivity;
import com.example.animo.gita.fragments.MainActivityFragment;

/**
 * Created by animo on 4/6/17.
 */

public class NotificationService {
    private Context mContext;

    public NotificationService(Context context){
        mContext = context;
    }

    public void createIntent(String message){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.github_logo)
                .setContentTitle("Github Activity NOtification")
                .setContentText(message);
        //Creates an explicit Intent for an Activity
        Intent resultIntent = new Intent(mContext, CommitsDetailActivity.class);
        resultIntent.putExtra(Constants.REPO,"gitA");
        resultIntent.putExtra(Constants.OWNER,"animo93");
        resultIntent.putExtra(Constants.SHA,"081350386741d7d88f7d231b87799ef5c9b6754d");
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Constants.NOTIFICATION_ID,mBuilder.build());


    }
}
