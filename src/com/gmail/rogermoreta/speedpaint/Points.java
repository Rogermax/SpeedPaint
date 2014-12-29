package com.gmail.rogermoreta.speedpaint;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.example.games.basegameutils.BaseGameActivity;

public class Points extends BaseGameActivity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// want fullscreen, we hide Activity's title and notification bar
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		//		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	    setContentView(R.layout.sign_in_points);
	    
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
	    ((PointsView) findViewById(R.id.pointsView1)).Init(this, getApiClient(), size.x, size.y);
	    //findViewById(R.id.button1).setOnClickListener(this); 
		//findViewById(R.id.button1).setEnabled(false);
	}

	@Override
	public void onSignInFailed() {
	    findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
	    ((PointsView) findViewById(R.id.pointsView1)).desconecta();
	}

	@Override
	public void onSignInSucceeded() {
	    findViewById(R.id.sign_in_button).setVisibility(View.GONE);
	    ((PointsView) findViewById(R.id.pointsView1)).conecta();
	}

	@Override
	public void onClick(View view) {
	    if (view.getId() == R.id.sign_in_button) {
	        beginUserInitiatedSignIn();
	    }
	    /*else if (view.getId() == R.id.button1) {
			SharedPreferences sharedPref = getSharedPreferences(
					getString(R.string.sharedPoints),
					Context.MODE_PRIVATE);
			int temp = sharedPref.getInt("puntos_normal_aux", 0);
			int best = sharedPref.getInt("puntos_normal_best", 0);
			SharedPreferences.Editor editor = sharedPref.edit();
			if (!sharedPref.getBoolean("puntos_normal_bool", true))
			{
				editor.putBoolean("puntos_normal_bool", 
						true);
				if (temp > best) {
					Log.i("Points", "Lo va a meter en google");
					editor.putInt("puntos_normal_best", 
							temp);
					Games.Leaderboards.submitScore(getApiClient(), getString(R.string.leaderboard_10_seconds_ranking), temp);
				}
			
				editor.commit();
			}
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), getString(R.string.leaderboard_10_seconds_ranking)), 1);
	        // show sign-in button, hide the sign-out button
	        ((TextView) findViewById(R.id.textView1)).setText("le has dado a ranking: "+Math.max(best, temp));
	    }*/
	}

}