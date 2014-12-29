package com.gmail.rogermoreta.speedpaint;

import java.util.ArrayList;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
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
	private Bitmap bg;
	private Bitmap jugar;
	private Bitmap jugara;
	private Bitmap resistencia;
	private Bitmap resistenciaa;
	private Bitmap puntos;
	private Bitmap puntosa;
	private Bitmap logros;
	private Bitmap logrosa;
	private int offset;
	private GoogleApiClient GAP;
	private BaseGameActivity BGA;
	

	public MenuView(Context context) {
		super(context);
		getHolder().addCallback(this);	
	}

	public MenuView(Context context, AttributeSet attributeSet) {
		super(context,attributeSet);
		getHolder().addCallback(this);	
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// nothing here
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		figuras = new ArrayList<Figura>();
		figuras.add(new Rectangulo(1, width / 4, 3 * height / 7+offset, width / 2,
				height / 7));
		figuras.add(new Rectangulo(2, width / 4, 4 * height / 7+offset, width / 2,
				height / 7));
		figuras.add(new Rectangulo(3, width / 4, 5 * height / 7+offset, width / 2,
				height / 7));
		figuras.add(new Rectangulo(4, width / 4, 6 * height / 7+offset, width / 2,
				height / 7));
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

		// canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(bg, 0, 0, null);
		/*
		 * for(Figura f : figuras) { if(f instanceof Circulo) { Circulo c =
		 * (Circulo) f; p.setColor(Color.BLACK); canvas.drawCircle(c.getX(),
		 * c.getY(), c.getRadio(), p); } else { // in this context, only
		 * instanceof Rectangulo Rectangulo r = (Rectangulo) f;
		 * p.setColor(Color.RED); canvas.drawRect(r.getX(), r.getY(),
		 * r.getX()+r.getAncho(), r.getY()+r.getAlto(), p); } }
		 */

		for (int i = 0; i < figuras.size(); ++i) {
			Figura f = figuras.get(i);
			if (f.getId() == 1) {
				if (f.getId() == figuraActiva)
					canvas.drawBitmap(jugara, width / 4, 3 * height / 7 + offset, null);
				else
					canvas.drawBitmap(jugar, width / 4, 3 * height / 7 + offset, null);
			}
			if (f.getId() == 2) {
				if (f.getId() == figuraActiva)
					canvas.drawBitmap(resistenciaa, width / 4, 4 * height / 7 + offset, null);
				else
					canvas.drawBitmap(resistencia, width / 4, 4 * height / 7 + offset, null);
			}
			if (f.getId() == 3) {
				if (f.getId() == figuraActiva)
					canvas.drawBitmap(puntosa, width / 4, 5 * height / 7 + offset, null);
				else
					canvas.drawBitmap(puntos, width / 4, 5 * height / 7 + offset, null);
			}
			if (f.getId() == 4) {
				if (f.getId() == figuraActiva)
					canvas.drawBitmap(logrosa, width / 4, 6 * height / 7 + offset, null);
				else
					canvas.drawBitmap(logros, width / 4, 6 * height / 7 + offset, null);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			for (Figura f : figuras) {
				if (f instanceof Circulo) {
					Circulo c = (Circulo) f;
					int distanciaX = x - c.getX();
					int distanciaY = y - c.getY();
					if (Math.pow(c.getRadio(), 2) > (Math.pow(distanciaX, 2) + Math
							.pow(distanciaY, 2))) {
						figuraActiva = c.getId();
						// break; check blog entry for explanation on why this
						// is commented
					}
				} else { // in this context, only instanceof Rectangulo
					Rectangulo r = (Rectangulo) f;
					if (x > r.getX() && x < r.getX() + r.getAncho()
							&& y > r.getY() && y < r.getY() + r.getAlto()) {
						figuraActiva = r.getId();
						// break; check blog entry for explanation on why this
						// is commented
					}
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			figuraActiva = -1;
			for (Figura f : figuras) {
				if (f instanceof Circulo) {
					Circulo c = (Circulo) f;
					int distanciaX = x - c.getX();
					int distanciaY = y - c.getY();
					if (Math.pow(c.getRadio(), 2) > (Math.pow(distanciaX, 2) + Math
							.pow(distanciaY, 2))) {
						figuraActiva = c.getId();
						// break; check blog entry for explanation on why this
						// is commented
					}
				} else { // in this context, only instanceof Rectangulo
					Rectangulo r = (Rectangulo) f;
					if (x > r.getX() && x < r.getX() + r.getAncho()
							&& y > r.getY() && y < r.getY() + r.getAlto()) {
						figuraActiva = r.getId();
						// break; check blog entry for explanation on why this
						// is commented
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			for (Figura f : figuras) {
				if (f instanceof Circulo) {
					Circulo c = (Circulo) f;
					int distanciaX = x - c.getX();
					int distanciaY = y - c.getY();
					if (Math.pow(c.getRadio(), 2) > (Math.pow(distanciaX, 2) + Math
							.pow(distanciaY, 2))) {
						figuraActiva = c.getId();
						// break; check blog entry for explanation on why this
						// is commented
					}
				} else { // in this context, only instanceof Rectangulo
					Rectangulo r = (Rectangulo) f;
					if (x > r.getX() && x < r.getX() + r.getAncho()
							&& y > r.getY() && y < r.getY() + r.getAlto()) {
						if (figuraActiva == r.getId() && r.getId() == 1) {// Han
																			// apretado
																			// el
																			// boton
																			// 1
																			// (Jugar).
							Intent mainIntent = new Intent().setClass(
							getContext(), Game.class);
							Bundle b = new Bundle();
							b.putLong("time", 500); //Your id
							mainIntent.putExtras(b); //Put your id to your next Intent
							((Activity) getContext()).startActivity(mainIntent);
						}
						if (figuraActiva == r.getId() && r.getId() == 2) {// Han
																			// apretado
																			// el
																			// boton
																			// 2
																			// (resistencia).
							Intent mainIntent = new Intent().setClass(
									getContext(), Game.class);
							Bundle b = new Bundle();
							b.putLong("time", 3000); //Your id
							mainIntent.putExtras(b); //Put your id to your next Intent
							((Activity) getContext()).startActivity(mainIntent);
						}
						if (figuraActiva == r.getId() && r.getId() == 3) {// Han
																			// apretado
																			// el
																			// boton
																			// 3.
																			// (Puntos)
							Intent mainIntent = new Intent().setClass(
									getContext(), Points.class);
							((Activity) getContext()).startActivity(mainIntent);
						}
						if (figuraActiva == r.getId() && r.getId() == 4) {// Han
																			// apretado
																			// el
																			// boton
																			// 4
																			// (Logros)
							@SuppressWarnings("unused")
							Logros_Manager lm = new Logros_Manager(BGA,GAP);
							BGA.startActivityForResult(Games.Achievements.getAchievementsIntent(GAP), 2);
						}
						// break; check blog entry for explanation on why this
						// is commented
					}
				}
			}
			figuraActiva = -1;
			break;
		}

		return true;
	}

	public void Init(BaseGameActivity base, GoogleApiClient googleApiClient, int width, int height) {
		p = new Paint();
		this.width = width;
		this.height = height;
		GAP = googleApiClient;
		BGA = base;
		offset = - height / 14;
		Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.menut);
		bg = Bitmap.createScaledBitmap(background, width, height, true);
		background = ((BitmapDrawable) getResources().getDrawable(R.drawable.botonjugar)).getBitmap();
		jugar = Bitmap.createScaledBitmap(background, width / 2, height / 7,true);
		background = ((BitmapDrawable) getResources().getDrawable(R.drawable.botonjugara)).getBitmap();
		jugara = Bitmap.createScaledBitmap(background, width / 2, height / 7,true);
		background = ((BitmapDrawable) getResources().getDrawable(R.drawable.botonresistencia)).getBitmap();
		resistencia = Bitmap.createScaledBitmap(background, width / 2, height / 7,true);
		background = ((BitmapDrawable) getResources().getDrawable(R.drawable.botonresistenciaa)).getBitmap();
		resistenciaa = Bitmap.createScaledBitmap(background, width / 2, height / 7,	true);
		background =  ((BitmapDrawable) getResources().getDrawable(R.drawable.botonpuntos)).getBitmap();
		puntos = Bitmap.createScaledBitmap(background, width / 2, height / 7,	true);
		background =  ((BitmapDrawable) getResources().getDrawable(R.drawable.botonpuntosa)).getBitmap();
		puntosa = Bitmap.createScaledBitmap(background, width / 2, height / 7,	true);
		background = ((BitmapDrawable) getResources().getDrawable(R.drawable.botonlogros)).getBitmap();
		logros = Bitmap.createScaledBitmap(background, width / 2, height / 7,	true);
		background = ((BitmapDrawable) getResources().getDrawable(R.drawable.botonlogrosa)).getBitmap();
		logrosa = Bitmap.createScaledBitmap(background, width / 2, height / 7,	true);
	}
}
