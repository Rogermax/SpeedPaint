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

public class Logros extends BaseGameActivity implements OnClickListener{

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

			SharedPreferences sharedPref = getSharedPreferences(
							getString(R.string.sharedPoints),
							Context.MODE_PRIVATE);
			
			startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 2);
	        // show sign-in button, hide the sign-out button
	        ((TextView) findViewById(R.id.textView1)).setText("le has dado a ranking: ");
	    }
	}

}