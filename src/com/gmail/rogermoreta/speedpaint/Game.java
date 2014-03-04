package com.gmail.rogermoreta.speedpaint;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity {
    /** Called when the activity is first created. */
	
	private static final String TAG = Game.class.getSimpleName();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set our MainGamePanel as the View
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
		setContentView(new GameView(this, size.x, size.y));
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroying...");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "Stopping...");
		super.onStop();
	}
    

	@Override
	protected void onPause() {
	    Log.d(TAG, "Pausing…");
	    GameThread.setRunning(false);//this is the value for stop the loop in the run()
	    super.onPause();
	}
    
}

