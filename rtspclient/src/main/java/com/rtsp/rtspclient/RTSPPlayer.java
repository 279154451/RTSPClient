package com.rtsp.rtspclient;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;
import android.view.TextureView;


import com.rtsp.rtspclient.vlc.VlcListener;
import com.rtsp.rtspclient.vlc.VlcVideoLibrary;

import org.videolan.libvlc.MediaPlayer;

import java.util.Arrays;

/**
 * 创建时间：2020/11/26
 * 创建人：singleCode
 * 功能描述：
 **/
public class RTSPPlayer implements VlcListener {
    private String TAG = RTSPPlayer.class.getSimpleName();
    private static volatile RTSPPlayer helper;
    private VlcVideoLibrary library;
    private String[] options = new String[]{":fullscreen"};
    private VlcListener listener;
    private RTSPPlayer(){

    }
    public static RTSPPlayer getInstance(){
        if(helper == null){
            synchronized (RTSPPlayer.class){
                if(helper == null){
                    helper = new RTSPPlayer();
                }
            }
        }
        return helper;
    }

    public void startPlay(Context context,String url,SurfaceView surfaceView,VlcListener listener){
        Log.d(TAG, "startPlay: 1"+url);
        this.listener = listener;
        if(library == null){
            Log.d(TAG, "startPlay: 2"+url);
            library = new VlcVideoLibrary(context,this,surfaceView);
            library.setOptions(Arrays.asList(options));
            library.play(url);
        }else if(!library.isPlaying()){
            Log.d(TAG, "startPlay:3"+url);
            library.play(url);
        }
    }
    public void startPlay(Context context, String url, TextureView surfaceView, VlcListener listener){
        this.listener = listener;
        if(library == null){
            Log.d(TAG, "startPlay 1: "+url);
            library = new VlcVideoLibrary(context,this,surfaceView);
            library.setOptions(Arrays.asList(options));
            library.play(url);
        }else if(!library.isPlaying()){
            Log.d(TAG, "startPlay 2: "+url);
            library.play(url);
        }
    }
    public boolean isPlaying(){
        boolean playing = false;
        if(library != null){
            playing = library.isPlaying();
        }
        Log.d(TAG, "isPlaying: "+playing);
        return playing;
    }

    public void stopPlay(){
        Log.d(TAG, "stopPlay: 1");
        if(library != null ){
            if(library.isPlaying()){
                Log.d(TAG, "stopPlay:stop ");
                library.stop();
            }else {
                Log.d(TAG, "stopPlay:destroy ");
                library.destroy();
            }
        }
        library = null;
        listener = null;
    }
    public void pause(){
        if(library != null && library.isPlaying()){
            Log.d(TAG, "pause: ");
            library.pause();
        }
    }



    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete: ");
        if(listener != null) listener.onComplete();
    }

    @Override
    public void onError() {
        Log.d(TAG, "onError: ");
        stopPlay();
        if(listener != null) listener.onError();
    }

    @Override
    public void onBuffering(MediaPlayer.Event event) {
        Log.d(TAG, "onBuffering: "+event.toString());
        if(listener != null) listener.onBuffering(event);
    }

    @Override
    public void onDisConnected() {
        Log.d(TAG, "onDisConnected: ");
        stopPlay();
    }
}
