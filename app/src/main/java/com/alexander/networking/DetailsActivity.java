package com.alexander.networking;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class DetailsActivity extends AppCompatActivity {

    private static String TIME = "time";
    private static String VALUE = "value";
    private String time;
    private long timeUn;
    private Weather weather;
    private Toolbar toolbar;
    private TextView text;
    private Messenger mService;
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        initViews();
        getData();
        setToolbar();
        startService(ServiceDB.newIntent(DetailsActivity.this, timeUn));

    }

    private void initViews(){
        text = findViewById(R.id.text);
        toolbar = findViewById(R.id.toolbar);
    }

    private void getData(){
        time = getIntent().getStringExtra(VALUE);
        timeUn = getIntent().getLongExtra(TIME, 0);
    }

    private void setToolbar(){
        toolbar.setTitle(time);
        setSupportActionBar(toolbar);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message message = Message.obtain(null,
                    ServiceDB.MSG_REGISTER_CLIENT);
            message.replyTo = mMessenger;
            try {
                mService.send(message);
            }
            catch (RemoteException e){
                Log.v("RemoteException", e.getMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        bindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unBindService();
    }

    public void bindService(){
        bindService(ServiceDB.newIntent(this, timeUn), mServiceConnection, Context.BIND_AUTO_CREATE);
    }
    public void unBindService(){
        Message msg = Message.obtain(null,
                ServiceDB.MSG_UNREGISTER_CLIENT);
        msg.replyTo = mMessenger;

        try {
            mService.send(msg);
        } catch (RemoteException e){
            Log.v("RemoteException", e.getMessage());
        }

        unbindService(mServiceConnection);
    }

    public static final Intent newIntent(Context context, String time, long timeUn){
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(VALUE, time);
        intent.putExtra(TIME, timeUn);
        return intent;
    }

    private class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            switch (message.what){

                case ServiceDB.MSG_SET_VALUE:
                   weather = (Weather) message.obj;
                    if (weather!=null)
                        text.setText(weather.toString());
            }
        }
    }
}
