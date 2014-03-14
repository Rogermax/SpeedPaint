package com.gmail.rogermoreta.speedpaint;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

public class Points extends BaseGameActivity implements OnClickListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// want fullscreen, we hide Activity's title and notification bar
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		//		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	    setContentView(R.layout.activity_main);
	    findViewById(R.id.sign_in_button).setOnClickListener(this);
	    findViewById(R.id.sign_out_button).setOnClickListener(this);  
	    findViewById(R.id.button1).setOnClickListener(this); 
		findViewById(R.id.button1).setEnabled(false);
	}
	
	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
		Log.i("MainActivity", "SigInFailed");

		findViewById(R.id.button1).setEnabled(false);
		
	    findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
	    findViewById(R.id.sign_out_button).setVisibility(View.GONE);
	    
	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
		Log.i("MainActivity", "SigInSucced");

		findViewById(R.id.button1).setEnabled(true);
		
	    findViewById(R.id.sign_in_button).setVisibility(View.GONE);
	    findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);


	}

	@Override
	public void onClick(View view) {
	    if (view.getId() == R.id.sign_in_button) {
	        // start the asynchronous sign in flow
	        beginUserInitiatedSignIn();
	    }
	    else if (view.getId() == R.id.sign_out_button) {
	        // sign out.
	        signOut();

			findViewById(R.id.button1).setEnabled(false);
			
	        // show sign-in button, hide the sign-out button
	        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
	        findViewById(R.id.sign_out_button).setVisibility(View.GONE);
	        

	    }
	    else if (view.getId() == R.id.button1) {

	    	Logros_Manager LM = new Logros_Manager(this, getApiClient());
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
	    }
	}

	private void actualiza_logros() {
		
	}
}