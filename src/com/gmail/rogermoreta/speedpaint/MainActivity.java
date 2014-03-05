package com.gmail.rogermoreta.speedpaint;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity  extends BaseGameActivity
implements View.OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// want fullscreen, we hide Activity's title and notification bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(new DragAndDropView(this));
	}
}