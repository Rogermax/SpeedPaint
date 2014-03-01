package com.gmail.rogermoreta.speedpaint;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MenuView extends SurfaceView implements SurfaceHolder.Callback {

        private ClickThread thread;
        private ArrayList<Figura> figuras;
        private int figuraActiva;
        Paint p;
        int width;
        int height;
        private Bitmap scaled;
        
        public MenuView(Context context) {
            super(context);
        }
        
        public MenuView(Context context, int width, int height) {
                super(context);
                p = new Paint();
                this.width = width;
                this.height = height;
                getHolder().addCallback(this);
                Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.menut);
                scaled = Bitmap.createScaledBitmap(background, width, height, true);
        }

        @Override
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
                // nothing here
        }

        @Override
        public void surfaceCreated(SurfaceHolder arg0) {
                figuras = new ArrayList<Figura>();
                figuras.add(new Rectangulo(1,width/4,3*height/7, width/2,height/7));
                figuras.add(new Rectangulo(2,width/4,5*height/7, width/2,height/7));
                figuraActiva = -1;
                
                thread = new ClickThread(getHolder(), this);
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
                
                //canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(scaled, 0, 0, null);
                /*for(Figura f : figuras) {
                    if(f instanceof Circulo) {
                            Circulo c = (Circulo) f;
                            p.setColor(Color.BLACK);
                            canvas.drawCircle(c.getX(), c.getY(), c.getRadio(), p);
                    } else {        // in this context, only instanceof Rectangulo
                            Rectangulo r = (Rectangulo) f;
                            p.setColor(Color.RED);
                            canvas.drawRect(r.getX(), r.getY(), r.getX()+r.getAncho(), r.getY()+r.getAlto(), p);
                    }
                }*/
                
                for (int i = 0; i < figuras.size(); ++i)
                {
                	Figura f = figuras.get(i);
                	if(f instanceof Circulo) {
                         Circulo c = (Circulo) f;
                         if (figuras.get(i).getId() == figuraActiva) p.setColor(Color.YELLOW);
                         else p.setColor(Color.BLACK);
                         canvas.drawCircle(c.getX(), c.getY(), c.getRadio(), p);
                	} else {        // in this context, only instanceof Rectangulo
                         Rectangulo r = (Rectangulo) f;
                         if (figuras.get(i).getId() == figuraActiva) p.setColor(Color.YELLOW);
                         else p.setColor(Color.BLACK);
                         canvas.drawRect(r.getX(), r.getY(), r.getX()+r.getAncho(), r.getY()+r.getAlto(), p);
                	}
                }
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                
                switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                        for(Figura f : figuras) {
                                if(f instanceof Circulo) {
                                        Circulo c = (Circulo) f;
                                        int distanciaX = x - c.getX();
                                        int distanciaY = y - c.getY();
                                        if(Math.pow(c.getRadio(), 2) > (Math.pow(distanciaX, 2) + Math.pow(distanciaY, 2))) {
                                                figuraActiva = c.getId();
                                                //break; check blog entry for explanation on why this is commented
                                        }
                                } else {        // in this context, only instanceof Rectangulo
                                        Rectangulo r = (Rectangulo) f;
                                        if(x > r.getX() && x < r.getX()+r.getAncho() && y > r.getY() && y < r.getY()+r.getAlto()) {
                                                figuraActiva = r.getId();
                                                //break; check blog entry for explanation on why this is commented
                                        }
                                }
                        }
                        break;
                case MotionEvent.ACTION_MOVE:
                	figuraActiva = -1;
                	for(Figura f : figuras) {
                        if(f instanceof Circulo) {
                                Circulo c = (Circulo) f;
                                int distanciaX = x - c.getX();
                                int distanciaY = y - c.getY();
                                if(Math.pow(c.getRadio(), 2) > (Math.pow(distanciaX, 2) + Math.pow(distanciaY, 2))) {
                                        figuraActiva = c.getId();
                                        //break; check blog entry for explanation on why this is commented
                                }
                        } else {        // in this context, only instanceof Rectangulo
                                Rectangulo r = (Rectangulo) f;
                                if(x > r.getX() && x < r.getX()+r.getAncho() && y > r.getY() && y < r.getY()+r.getAlto()) {
                                        figuraActiva = r.getId();
                                        //break; check blog entry for explanation on why this is commented
                                }
                        }
	                }
	                break;
                case MotionEvent.ACTION_UP:
                    for(Figura f : figuras) {
                        if(f instanceof Circulo) {
                                Circulo c = (Circulo) f;
                                int distanciaX = x - c.getX();
                                int distanciaY = y - c.getY();
                                if(Math.pow(c.getRadio(), 2) > (Math.pow(distanciaX, 2) + Math.pow(distanciaY, 2))) {
                                        figuraActiva = c.getId();
                                        //break; check blog entry for explanation on why this is commented
                                }
                        } else {        // in this context, only instanceof Rectangulo
                                Rectangulo r = (Rectangulo) f;
                                if(x > r.getX() && x < r.getX()+r.getAncho() && y > r.getY() && y < r.getY()+r.getAlto()) {
                                        if (figuraActiva == r.getId() && r.getId() == 1)
                                        {//Han apretado el boton 1 (Jugar).
                                        	 Intent mainIntent = new Intent().setClass(getContext(), Game.class);
                                        	 ((Activity) getContext()).startActivity(mainIntent);
                                        }
                                        if (figuraActiva == r.getId() && r.getId() == 2)
                                        {//Han apretado el boton 2.
                                        	
                                        }
                                        //break; check blog entry for explanation on why this is commented
                                }
                        }
	                }
                    figuraActiva = -1;
	                break;
                }
                
                return true;
        }
}
