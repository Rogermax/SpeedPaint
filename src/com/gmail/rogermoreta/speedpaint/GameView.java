package com.gmail.rogermoreta.speedpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

        private MoveThread thread;
        Paint p;
        int width;
        int height;
        
        public GameView(Context context) {
            super(context);
        }
        
        public GameView(Context context, int width, int height) {
                super(context);
                this.width = width;
                this.height = height;
                p = new Paint();
                getHolder().addCallback(this);
        }

        @Override
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
                // nothing here
        }

        @Override
        public void surfaceCreated(SurfaceHolder arg0) {
                
                thread = new MoveThread(getHolder(), this);
                thread.setRunning(true);
                thread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder arg0) {
                boolean retry = true;
                thread.setRunning(false);
                while (retry) {
                        try {
                            thread.join();
                                retry = false;
                        } catch (InterruptedException e) {
                                
                        }
                }
        }
        
        public void onDrawC(Canvas canvas) {
                p.setAntiAlias(true);
                
                canvas.drawColor(Color.YELLOW);
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
                //int x = (int) event.getX();
                //int y = (int) event.getY();
                
                switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                        break;
                case MotionEvent.ACTION_MOVE:
	                break;
                case MotionEvent.ACTION_UP:
	                break;
                }
                
                return true;
        }
}
