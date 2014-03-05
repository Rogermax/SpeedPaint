package com.gmail.rogermoreta.speedpaint;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PointsView extends SurfaceView implements SurfaceHolder.Callback {

	private PointsThread thread;
	int width;
	int height;
	int best;
	// para los FPS
	@SuppressWarnings("unused")
	private String avgFps;

	public void setAvgFps(String avgFps) {
		this.avgFps = avgFps;
	}

	public PointsView(Context context) {
		super(context);
	}

	public PointsView(Context context, int width, int height) {
		super(context);
		this.width = width;
		this.height = height;
		getHolder().addCallback(this);
		thread = new PointsThread(getHolder(), this, width, height);
		setFocusable(true);
		SharedPreferences sharedPref = getContext()
				.getSharedPreferences(
						getContext().getString(R.string.sharedPoints),
						Context.MODE_PRIVATE);
		best = sharedPref.getInt("puntos_normal_aux", 0);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// nothing here
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		if (thread.getState() == Thread.State.NEW) {
			PointsThread.setRunning(true);
			thread.start();
		} else if (thread.getState() == Thread.State.TERMINATED) {
			thread = new PointsThread(getHolder(), this, width, height);
			GameThread.setRunning(true);
			thread.start(); // Start a new thread
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {

	}

	public void render(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		displayBest(canvas, "Best: " + best, 100, 100);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// int x = (int) event.getX();
		// int y = (int) event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:

			break;
		}

		return true;
	}

	private void displayBest(Canvas canvas, String str, int x, int y) {
		if (canvas != null && str != null) {
			Paint paint = new Paint();
			paint.setARGB(255, 255, 255, 255);
			canvas.drawText(str, x, y, paint);
		}
	}

	public void update() {
		
	}

}
