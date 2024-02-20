package com.example.broadcastnotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
public class ServiceClass extends Service
{
    boolean running=false;
    MyThread thread;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    public static final int NOTIFICATION_ID = 1 ;
    private final static String default_notification_channel_id = "default" ;
    public void onCreate()
    {
        super.onCreate();
        Toast.makeText(getBaseContext(), "Service Created", Toast.LENGTH_LONG).show();
        running=true; thread=new MyThread(); thread.start();}
    public int onStartCommand(Intent intent, int flags,int startId)
    {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(getBaseContext(), "Service started", Toast.LENGTH_LONG).show();
        Bundle b=intent.getBundleExtra("data"); running=b.getBoolean("stop");
        if(!thread.isAlive())
        {
            thread=new MyThread(); thread.start();
        }
        return Service.START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
    public void onDestroy()
    {
        running=false;
        Toast.makeText(getBaseContext(), "Service stopped", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
    Handler hand=new Handler()
    {
        public void handleMessage(Message m)
        {
            NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext(),
                    default_notification_channel_id )
                    .setSmallIcon(R.drawable. ic_launcher_foreground )
                    .setContentTitle( "From Service" )
                    .setContentText( "Hai " +m.what );
            if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O )
            {
                int importance = NotificationManager. IMPORTANCE_HIGH ;
                NotificationChannel notificationChannel = new
                        NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
                notificationChannel.enableLights( true ) ;
                notificationChannel.setLightColor(Color. RED ) ;
                notificationChannel.enableVibration( true ) ;
                notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;
                mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
                assert manager != null;
                manager.createNotificationChannel(notificationChannel) ;
            }
            assert manager != null;manager.notify(NOTIFICATION_ID, mBuilder.build()) ;
        }
    };
    class MyThread extends Thread
    {
        public void run()
        {
            int i=0;
            while(running)
            {
                try
                {
                    Thread.sleep(5000);
                }
                catch (InterruptedException e)
                {
// TODO Auto-generated catch block e.printStackTrace();
                }
                hand.sendEmptyMessage(i++);
            }
        }
    }
}