package com.gmail.rogermoreta.speedpaint;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ClickThread extends Thread {

	private SurfaceHolder sh;
	private MenuView view;
	private boolean run;

	public ClickThread(SurfaceHolder sh, MenuView view) {
		this.sh = sh;
		this.view = view;
		run = false;
	}

	public void setRunning(boolean run) {
		this.run = run;
	}

	public void run() {
		Canvas canvas;
		while (run) {
			canvas = null;
			try {
				canvas = sh.lockCanvas(null);
				synchronized (sh) {
					if (canvas != null)
						view.onDrawC(canvas);
				}
			} finally {
				if (canvas != null)
					sh.unlockCanvasAndPost(canvas); // return to a stable state
			}
		}
	}
}