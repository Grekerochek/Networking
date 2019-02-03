package com.alexander.networking;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class ServiceDB extends IntentService {

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 0;
    public static final int MSG_SET_VALUE = 2;

    public static final String TIME = "time";

    private Weather weather;
    private WeatherDatabase db;
    private List<Messenger> mClients = new ArrayList<>();
    private Messenger mMessenger = new Messenger(new IncomingHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return mMessenger.getBinder();
    }

    public ServiceDB() {
        super("ServiceDB");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            Thread.sleep(200);
        } catch (InterruptedException e){
            Log.v("InterruptedException", e.getMessage());
        }

        db = Room.databaseBuilder(getApplicationContext(), WeatherDatabase.class, "my_database")
                .build();
        weather = db.getWeatherDAO().getWeather(intent.getLongExtra(TIME, 0));
        for (Messenger messenger: mClients){
            try {
                messenger.send(Message.obtain(null, MSG_SET_VALUE, weather));
            } catch (RemoteException e){
                Log.v("RemoteException", e.getMessage());
            }
        }
    }

    public static Intent newIntent(Context context, long time){
        Intent intent = new Intent(context, ServiceDB.class);
        intent.putExtra(TIME, time);
        return intent;
    }

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_SET_VALUE:
                    break;
            }
        }
    }
}
