package com.alexander.networking;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private LinearLayoutManager manager;
    private List<Weather> forecasts;

    private Messenger mService;
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        initListeners();
        initRecyclerView();
        startService(MyService.newIntent(MainActivity.this));
    }

    private void initViews(){
        fab = findViewById(R.id.fab);
    }

    private void initListeners(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(MyService.newIntent(MainActivity.this));
                bindService(MyService.newIntent(MainActivity.this), mServiceConnection, Context.BIND_AUTO_CREATE);;
            }
        });
    }

    private void initRecyclerView(){

        recyclerView = findViewById(R.id.recyclerView);
        forecasts = new ArrayList<>();
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new Adapter(this, forecasts);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new CustomItemDecorator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.devider)));
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message message = Message.obtain(null,
                    MyService.MSG_REGISTER_CLIENT);
            message.replyTo = mMessenger;
            try {
                mService.send(message);
            }
            catch (RemoteException e){
                e.printStackTrace();
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
        try {
            unBindService();
        } catch (Exception e){
            Log.v("Exception", e.getMessage());
        }
    }

    public void bindService(){

        bindService(MyService.newIntent(this), mServiceConnection, Context.BIND_AUTO_CREATE);

    }
    public void unBindService(){
        Message msg = Message.obtain(null,
                MyService. MSG_UNREGISTER_CLIENT);
        msg.replyTo = mMessenger;

        try {
            mService.send(msg);
        } catch (RemoteException e){
            e.printStackTrace();
        }

        unbindService(mServiceConnection);
    }
    private class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            switch (message.what){

                case MyService.MSG_SET_VALUE:
                    forecasts = (List<Weather>) message.obj;
                    if (forecasts!=null)
                        adapter.setData(forecasts);
                    else adapter.setData(new ArrayList<Weather>());
                    unBindService();
            }
        }
    }
}
