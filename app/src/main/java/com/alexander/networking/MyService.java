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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyService extends IntentService {

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 0;
    public static final int MSG_SET_VALUE = 2;


    private List<Messenger> mClients = new ArrayList<>();
    private Messenger mMessenger = new Messenger(new IncomingHandler());
    private List<Weather> forecasts;
    private WeatherDatabase db;

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return mMessenger.getBinder();
    }

    public MyService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        db = Room.databaseBuilder(getApplicationContext(), WeatherDatabase.class, "my_database")
                .build();
        try {
            forecasts = new ApiMapper(new RetrofitHelper()).getWeather();
            db.getWeatherDAO().insert(forecasts);
        } catch (IOException e){
                forecasts = db.getWeatherDAO().getForecast();
        }

        for (Messenger messenger: mClients){
            try {
                messenger.send(Message.obtain(null, MSG_SET_VALUE, forecasts));
            } catch (RemoteException e){
                Log.v("RemoteException", e.getMessage());
            }
        }
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, MyService.class);
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
