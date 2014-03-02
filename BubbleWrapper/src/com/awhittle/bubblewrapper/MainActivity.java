package com.awhittle.bubblewrapper;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;

public class MainActivity extends Activity {

    private GLSurfaceView mGLView;
	public static int width;
	public static int height;
	public static MediaPlayer mPlayer;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setPlayer();

    	Display display = getWindowManager().getDefaultDisplay();
    	Point size = new Point();
    	display.getSize(size);
    	width = size.x;
    	height = size.y;
    	
	    mGLView = new MyGLSurfaceView(this);
	    setContentView(mGLView);
	      
	}

	public void setPlayer() {
		
	    try {
	    	AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	    	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
	        AssetFileDescriptor descriptor = getAssets().openFd("snap.mp3");
	        mPlayer = new MediaPlayer();
	        mPlayer.reset();
	        mPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
	        descriptor.close();
	        mPlayer.prepare();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

		 
	}
	
	@Override
	public void onBackPressed() {
		//End application
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	

}
