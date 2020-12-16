package com.team.NEBULA.cwc

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

class FireBroadcastReciever:BroadcastReceiver(){

    var CHANNEL_ID= (2155).toString()

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.action.equals("com.example.Broadcast")){
            Log.d("Hello","****************************************** BroadCast")
            var builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
                .setSmallIcon(R.drawable.cwc)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            var manager:NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(1,builder.build())
            manager.cancel(1)
        }
    }

}