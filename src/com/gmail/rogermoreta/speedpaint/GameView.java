package com.gmail.rogermoreta.speedpaint;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = GameView.class.getSimpleName();
 
	private GameThread thread;
    Paint p;
    private long beginTime;
    
    // para los FPS
 	private String avgFps;
 	public void setAvgFps(String avgFps) {
 		this.avgFps = avgFps;
 	}
    
 	//CACA
 		int cont = 0;
        int width;
        int height;
		int level = 0;
		long time_milis = 0;
        //private int xPos,yPos;
        private Pair<Integer, Integer> lastPair = null;
        private Bitmap lienzoBg;
        private Bitmap lienzo;
        private Bitmap bg;
        ArrayList<Pair<Integer,Integer>> cola_click;
        boolean[][] mask_paint; 
        boolean clickan;
        private Paint circlePaint;
        private Path circlePath;
        private int pixels;
        private int total_pixels;
        
        public GameView(Context context) {
            super(context);
        }
        
        public GameView(Context context, int width, int height) {
        	super(context);
            getHolder().addCallback(this);
            thread = new GameThread(getHolder(), this,width, height);
            setFocusable(true);
            
            //CACA
            	total_pixels = 10*width*213*height/11/280;
            	pixels = 0;
            	cola_click = new ArrayList<Pair<Integer,Integer>>();
                this.width = width;
                this.height = height;
                this.mask_paint = new boolean[width][height];
                clickan = false;
                circlePaint = new Paint();
                circlePath = new Path();
                circlePaint.setAntiAlias(true);
                circlePaint.setColor(Color.RED);
                lienzo = Bitmap.createBitmap(width, 6*height/7, Bitmap.Config.ARGB_8888);
                Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.fondo_pantallas_castellano);
                bg = Bitmap.createScaledBitmap(background, width, height, true);
                background = BitmapFactory.decodeResource(getResources(), R.drawable.fondo_lienzo_pantallas_castellano);
                lienzoBg = Bitmap.createScaledBitmap(background, width, height, true);
                int margenx = width/22; 
                int margeny = height/40;
                for (int i = margeny; i < 11*height/14; ++i)
                {
                	for (int j = margenx; j < width-margenx; ++j)
                	{
                		lienzo.setPixel(j, i, 0x00000000);
                	}
                }
        }

        @Override
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
                // nothing here
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        //if it is the first time the thread starts

        beginTime = System.currentTimeMillis();
        if(thread.getState() == Thread.State.NEW){
        	GameThread.setRunning(true);
        	thread.start();
        }
        else
        	if (thread.getState() == Thread.State.TERMINATED)
        	{
		        thread = new GameThread(getHolder(), this, width, height);
		        GameThread.setRunning(true);
		        thread.start(); // Start a new thread
	        }
        }


        @Override
        public void surfaceDestroyed(SurfaceHolder arg0) {
        	Log.d(TAG, "Surface is being destroyed");
            boolean retry = true;
            GameThread.setRunning(false);
            while (retry) {
                    try {
                        thread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                            
                    }
            }
        }
        

        @Override
        public boolean onTouchEvent(MotionEvent event) {
                Pair<Integer,Integer> aux = new Pair<Integer,Integer>((int) event.getX(),(int) event.getY());
                
                switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	lastPair = aux;
                	break;
                case MotionEvent.ACTION_MOVE:
                	cola_click.add(aux);
	                break;
                case MotionEvent.ACTION_UP:
                	//lastPair = aux;
	                break;
	            default:
	            	return false;
                }
                return true;
        }
        
        public void render(Canvas canvas) {


        	canvas.drawColor(Color.BLACK);
            /*if (clickan)
            {
            	for (int ii = 0; ii < width; ++ii)
            	{
            		for (int jj = 0; jj < height; ++jj)
            		{
            			if (mask_click[ii][jj])
            			{
	        	        	for (int i = xPos-ancho_pinzel/2; i < xPos+ancho_pinzel/2; ++i)
	        	        	{
	        	            	for (int j = yPos-alto_pinzel/2; j < yPos+alto_pinzel/2; ++j)
	        	            	{
	        	            		Log.i("GameView", "i: "+i+"\nj: "+j);
	        	            		if (i > width/22 && i < 21*width/22 && j > height/40 && j < 11*height/14 && !mask_paint[i][j]) {
	        	            			mask_paint[i][j] = true;
	        	            			lienzo.setPixel(i, j, 0xffff0000);
	        	            		}
	        	            	}
	        	        	}
	        	        	mask_click[ii][jj] = false;
            			}
            		}
            	}
	        	clickan = false;
            }*/
            canvas.drawBitmap(lienzoBg,0,0,null);
            canvas.drawBitmap(lienzo,0,0,null);
            canvas.drawBitmap(bg, 0, 0, null);

    		displayTime(canvas, ""+(10*1000-time_milis)/1000+":"+((10*1000-time_milis)-((10*1000-time_milis)/1000)*1000)/10);
    		displayPercentage(canvas, ""+pixels*100/total_pixels+"%");
    		displayFps(canvas, avgFps);
    		displayLevel(canvas, "level: "+level);
        }
        
        /**
    	 * This is the game update method. It iterates through all the objects
    	 * and calls their update method if they have one or calls specific
    	 * engine's update method.
    	 */
        public void update() {
            int ancho_pinzel = 10*width/110;
            int alto_pinzel = 213*width/2800;


            //pintar cuadrado
            //scoger eskina
            //calcular cuantos pasos
            //ir en direccion eskina y pintando dos lineas(si es necesario)
            
            
            
        	for (int ii = 0; ii < cola_click.size(); ++ii)
        	{

        		//pintar cuadrado
        		//Log.i(TAG,"level<<24: "+Integer.toHexString(level<<24));
        		//int pintura = (103409*level+36469)%0x00ffffff;
        		int color = 0x88000000|(0x000000ff << level);
                int x = lastPair.first;
                int y = lastPair.second;
	        	for (int i = x-ancho_pinzel/2; i < x+ancho_pinzel/2; ++i)
	        	{
	            	for (int j = y-alto_pinzel/2; j < y+alto_pinzel/2; ++j)
	            	{
	            		//Log.i("GameView", "i: "+i+"\nj: "+j);
	            		if (i > width/22 && i < 21*width/22 && j >= height/40 && j < 11*height/14 && !mask_paint[i][j]) {
	            			mask_paint[i][j] = true;
	            			pixels++;
	            			lienzo.setPixel(i, j, color);
	            		}
	            	}
	        	}
        		//escoger pasos y esquina
        		int pasos = Math.max(Math.abs(lastPair.first-cola_click.get(ii).first), Math.abs(lastPair.second-cola_click.get(ii).second));
        		if (lastPair.first > cola_click.get(ii).first)
        		{
        			if (lastPair.second > cola_click.get(ii).second)
            		{//esquina buena es la inversa (negativa, negativa)
                		//Log.i(TAG, "Ha elegido ir para arriba, izquierda");
        				for (int i2 = 0; i2 < pasos; ++i2)
        	        	{
    	            		int i = x-ancho_pinzel/2-Math.round(Math.abs(lastPair.first-cola_click.get(ii).first)*(i2+1)/pasos);
    	            		int j = y-alto_pinzel/2-Math.round( Math.abs(lastPair.second-cola_click.get(ii).second)*(i2+1)/pasos);
        	            	for (int j2 = 0; j2 < ancho_pinzel; ++j2)
        	            	{
        	            		if (i+j2 > width/22 && i+j2 < 21*width/22 && j >= height/40 && j < 11*height/14 && !mask_paint[i+j2][j]) {
        	            			mask_paint[i+j2][j] = true;
        	            			pixels++;
        	            			lienzo.setPixel(i+j2, j, color);
        	            		}
        	            	}
        	            	for (int j2 = 0; j2 < alto_pinzel; ++j2)
        	            	{
        	            		if (i > width/22 && i < 21*width/22 && j+j2 >= height/40 && j+j2 < 11*height/14 && !mask_paint[i][j+j2]) {
        	            			mask_paint[i][j+j2] = true;
        	            			pixels++;
        	            			lienzo.setPixel(i, j+j2, color);
        	            		}
        	            	}
        	        	}
            		}
        			else if (lastPair.second < cola_click.get(ii).second)
            		{//esquina buena es la inversa (negativa, positiva)
        				for (int i2 = 0; i2 < pasos; ++i2)
        	        	{
                    		//Log.i(TAG, "Ha elegido ir para abajo, izquierda");
    	            		int i = x-ancho_pinzel/2-Math.round(Math.abs(lastPair.first-cola_click.get(ii).first)*(i2+1)/pasos);
    	            		int j = y+alto_pinzel/2+Math.round( Math.abs(lastPair.second-cola_click.get(ii).second)*(i2+1)/pasos);
        	            	for (int j2 = 0; j2 < ancho_pinzel; ++j2)
        	            	{
        	            		if (i+j2 > width/22 && i+j2 < 21*width/22 && j >= height/40 && j < 11*height/14 && !mask_paint[i+j2][j]) {
        	            			mask_paint[i+j2][j] = true;
        	            			pixels++;
        	            			lienzo.setPixel(i+j2, j, color);
        	            		}
        	            	}
        	            	for (int j2 = 0; j2 < alto_pinzel; ++j2)
        	            	{
        	            		if (i > width/22 && i < 21*width/22 && j-j2 >= height/40 && j-j2 < 11*height/14 && !mask_paint[i][j-j2]) {
        	            			mask_paint[i][j-j2] = true;
        	            			pixels++;
        	            			lienzo.setPixel(i, j-j2, color);
        	            		}
        	            	}
        	        	}
        			}
        		}
        		else if (lastPair.first < cola_click.get(ii).first)
        		{
        			if (lastPair.second > cola_click.get(ii).second)
            		{//esquina buena es la inversa (positiva, negativa)
        				for (int i2 = 0; i2 < pasos; ++i2)
        	        	{
                    		//Log.i(TAG, "Ha elegido ir para arriba, derecha");
    	            		int i = x+ancho_pinzel/2+Math.round(Math.abs(lastPair.first-cola_click.get(ii).first)*(i2+1)/pasos);
    	            		int j = y-alto_pinzel/2-Math.round( Math.abs(lastPair.second-cola_click.get(ii).second)*(i2+1)/pasos);
        	            	for (int j2 = 0; j2 < ancho_pinzel; ++j2)
        	            	{
        	            		if (i-j2 > width/22 && i-j2 < 21*width/22 && j >= height/40 && j < 11*height/14 && !mask_paint[i-j2][j]) {
        	            			mask_paint[i-j2][j] = true;
        	            			pixels++;
        	            			lienzo.setPixel(i-j2, j, color);
        	            		}
        	            	}
        	            	for (int j2 = 0; j2 < alto_pinzel; ++j2)
        	            	{
        	            		if (i > width/22 && i < 21*width/22 && j+j2 >= height/40 && j+j2 < 11*height/14 && !mask_paint[i][j+j2]) {
        	            			mask_paint[i][j+j2] = true;
        	            			pixels++;
        	            			lienzo.setPixel(i, j+j2, color);
        	            		}
        	            	}
        	        	}
            			
            		}
        			else if (lastPair.second < cola_click.get(ii).second)
            		{//esquina buena es la inversa (positiva, positiva)
        				for (int i2 = 0; i2 < pasos; ++i2)
        	        	{
                    		//Log.i(TAG, "Ha elegido ir para abajo, derecha");
    	            		int i = x+ancho_pinzel/2+Math.round(Math.abs(lastPair.first-cola_click.get(ii).first)*(i2+1)/pasos);
    	            		int j = y+alto_pinzel/2+Math.round( Math.abs(lastPair.second-cola_click.get(ii).second)*(i2+1)/pasos);
        	            	for (int j2 = 0; j2 < ancho_pinzel; ++j2)
        	            	{
        	            		if (i-j2 > width/22 && i-j2 < 21*width/22 && j >= height/40 && j < 11*height/14 && !mask_paint[i-j2][j]) {
        	            			mask_paint[i-j2][j] = true;
        	            			pixels++;
        	            			lienzo.setPixel(i-j2, j, color);
        	            		}
        	            	}
        	            	for (int j2 = 0; j2 < alto_pinzel; ++j2)
        	            	{
        	            		if (i > width/22 && i < 21*width/22 && j-j2 >= height/40 && j-j2 < 11*height/14 && !mask_paint[i][j-j2]) {
        	            			mask_paint[i][j-j2] = true;
        	            			pixels++;
        	            			lienzo.setPixel(i, j-j2, color);
        	            		}
        	            	}
        	        	}
        			}
        		}
	        	lastPair = new Pair<Integer, Integer>(cola_click.get(ii).first, cola_click.get(ii).second);
	        	
        	}
        	cola_click = new ArrayList<Pair<Integer,Integer>>();
        	if (pixels >= 0.9*total_pixels)
        	{
        		pixels = 0;
                lienzo = Bitmap.createBitmap(width, 6*height/7, Bitmap.Config.ARGB_8888);
                mask_paint = new boolean[width][height];
                int tiempo_regeneracion = 3000;
            	beginTime = Math.min(System.currentTimeMillis(), beginTime+tiempo_regeneracion);
        		level++;
        	}
        	time_milis = System.currentTimeMillis()-beginTime;
        	if (time_milis > 10*1000) ((Activity) getContext()).finish();
        	//	Log.i("GameView", "Partida al: "+pixels*100/total_pixels);
        	
        	
    	}
       
    	private void displayFps(Canvas canvas, String fps) {
    		if (canvas != null && fps != null) {
    			Paint paint = new Paint();
    			paint.setARGB(255, 255, 255, 255);
    			canvas.drawText(fps, this.getWidth() - 100, 50, paint);
    		}
    	}
    	private void displayPercentage(Canvas canvas, String perc) {
    		if (canvas != null && perc != null) {
    			Paint paint = new Paint();
    			paint.setARGB(255, 255, 255, 255);
    			canvas.drawText(perc, this.getWidth()/18, 50, paint);
    		}
    	}
    	private void displayTime(Canvas canvas, String time) {
    		if (canvas != null && time != null) {
    			Paint paint = new Paint();
    			paint.setARGB(255, 255, 255, 255);
    			canvas.drawText(time, this.getWidth() - 100, 6*height/7-100, paint);
    		}
    	}
    	private void displayLevel(Canvas canvas, String level) {
    		if (canvas != null && level != null) {
    			Paint paint = new Paint();
    			paint.setARGB(255, 255, 255, 255);
    			canvas.drawText(level, this.getWidth()/18, 6*height/7-100, paint);
    		}
    	}
        
        
}
