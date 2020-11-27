package com.smart.rtsp.rtspclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rtsp.rtspclient.RTSPPlayer;
import com.rtsp.rtspclient.vlc.VlcListener;

import org.videolan.libvlc.MediaPlayer;

public class MainActivity extends AppCompatActivity implements VlcListener, View.OnClickListener {
    private Button bStartStop;
    private EditText etEndpoint;
    SurfaceView surfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        bStartStop = (Button) findViewById(R.id.b_start_stop);
        bStartStop.setOnClickListener(this);
        etEndpoint = (EditText) findViewById(R.id.et_endpoint);
    }

    @Override
    public void onComplete() {
        Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Error, make sure your endpoint is correct", Toast.LENGTH_SHORT).show();
        RTSPPlayer.getInstance().stopPlay();
        bStartStop.setText("播放");
    }

    @Override
    public void onBuffering(MediaPlayer.Event event) {

    }

    @Override
    public void onDisConnected() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RTSPPlayer.getInstance().stopPlay();
    }

    @Override
    public void onClick(View view) {
        if (!RTSPPlayer.getInstance().isPlaying()) {
            RTSPPlayer.getInstance().startPlay(this,etEndpoint.getText().toString(),surfaceView,this);
            bStartStop.setText("播放");
        } else {
            RTSPPlayer.getInstance().pause();
            bStartStop.setText("暂停");
        }
    }
}
