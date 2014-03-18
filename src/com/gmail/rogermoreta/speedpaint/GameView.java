package com.gmail.rogermoreta.speedpaint;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = GameView.class.getSimpleName();

	private GameThread thread;
	Paint p;
	private long beginTime;
	private long last_time;
	private long tiempo_parpadeo;
	private long tiempo_finpartida;
	private final static Paint paint = new Paint();


	private InterstitialAd interstitial;
	
	
	// para los FPS
	private String avgFps;

	public void setAvgFps(String avgFps) {
		this.avgFps = avgFps;
	}

	// CACA
	int cont = 0;
	int width;
	int height;
	int level = 0;
	long time_milis = 0;
	private boolean partida_ON = false;
	// private int xPos,yPos;
	private Pair<Integer, Integer> lastPair = null;
	@SuppressWarnings("unused")
	private static Bitmap lienzoBg;
	private static Bitmap lienzo;
	private static Bitmap porcentaje;
	private static Bitmap fintiempo;
	private static Bitmap bg;
	private static Bitmap instruc;
	private static Bitmap instruc2;
	private Bitmap resultado;
	private Bitmap cero;
	private Bitmap uno;
	private Bitmap dos;
	private Bitmap tres;
	private Bitmap cuatro;
	private Bitmap cinco;
	private Bitmap seis;
	private Bitmap siete;
	private Bitmap ocho;
	private Bitmap nueve;
	private Bitmap boton;
	private Bitmap botona;
	ArrayList<Pair<Integer, Integer>> cola_click;
	private boolean[][] mask_paint;
	@SuppressWarnings("unused")
	private boolean clickan;
	private boolean fin_partida = false;
	private boolean cambio = false;
	private Paint circlePaint;
	@SuppressWarnings("unused")
	private Path circlePath;
	private int pixels;
	private int total_pixels;
	private Rectangulo r;
	private int offset;
	private boolean apretado = false;
	private boolean resistencia = true;
	private boolean fin_finpartida = false;
	private SharedPreferences.Editor editor;
	private SharedPreferences sharedPref;
	private long tiempo_regeneracion;

	public GameView(Context context) {
		super(context);
	}

	public GameView(Context context, int width, int height, long tiempo) {
		super(context);
		getHolder().addCallback(this);
		thread = new GameThread(getHolder(), this, width, height);
		setFocusable(true);

		if (tiempo < 100) resistencia = false;
		 // Create the interstitial.
	    interstitial = new InterstitialAd((Activity) getContext());
	    interstitial.setAdUnitId("ca-app-pub-4637004186333767/4804739134");

	    // Create ad request.
	    AdRequest adRequest = new AdRequest.Builder().build();
	    tiempo_parpadeo = System.currentTimeMillis();
	    // Begin loading your interstitial.
	    interstitial.loadAd(adRequest);
	    offset = -height /48;
		r = new Rectangulo(1, width / 3, 6 * height / 7+offset, width / 3+width/45,
				height / 8);
		// CACA
		paint.setARGB(255, 255, 255, 255);
		sharedPref = getContext()
				.getSharedPreferences(
						getContext().getString(R.string.sharedPoints),
						Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		tiempo_regeneracion = tiempo;
		total_pixels = 10 * width * 213 * height / 11 / 280;
		pixels = 0;
		cola_click = new ArrayList<Pair<Integer, Integer>>();
		this.width = width;
		this.height = height;
		this.mask_paint = new boolean[width][height];
		clickan = false;
		circlePaint = new Paint();
		circlePath = new Path();
		circlePaint.setAntiAlias(true);
		circlePaint.setColor(Color.RED);
		lienzo = Bitmap.createBitmap(width, 6 * height / 7,
				Bitmap.Config.ARGB_8888);
		Bitmap background = BitmapFactory.decodeResource(getResources(),
				R.drawable.fondo_pantallas_castellano);
		bg = Bitmap.createScaledBitmap(background, width, height, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.boton);
		boton = Bitmap.createScaledBitmap(background, width / 3, height / 8, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.botona);
		botona = Bitmap.createScaledBitmap(background, width / 3, height / 8,
				true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.fondo_lienzo_pantallas_castellano);
		lienzoBg = Bitmap.createScaledBitmap(background, width, height, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.instruc);
		instruc = Bitmap.createScaledBitmap(background, width, height, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.instruc2);
		instruc2 = Bitmap.createScaledBitmap(background, width, height, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.puntos);
		resultado = Bitmap.createScaledBitmap(background, width, height, true);
		int ancho_digito = width/12;
		int alto_digito = height/10;
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.cero);
		cero = Bitmap.createScaledBitmap(background, ancho_digito, alto_digito, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.uno);
		uno = Bitmap.createScaledBitmap(background, ancho_digito, alto_digito, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.dos);
		dos = Bitmap.createScaledBitmap(background, ancho_digito, alto_digito, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.tres);
		tres = Bitmap.createScaledBitmap(background, ancho_digito, alto_digito, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.cuatro);
		cuatro = Bitmap.createScaledBitmap(background, ancho_digito, alto_digito, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.cinco);
		cinco = Bitmap.createScaledBitmap(background, ancho_digito, alto_digito, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.seis);
		seis = Bitmap.createScaledBitmap(background, ancho_digito, alto_digito, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.siete);
		siete = Bitmap.createScaledBitmap(background, ancho_digito, alto_digito, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.ocho);
		ocho = Bitmap.createScaledBitmap(background, ancho_digito, alto_digito, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.nueve);
		nueve = Bitmap.createScaledBitmap(background, ancho_digito, alto_digito, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.porcentaje);
		porcentaje = Bitmap.createScaledBitmap(background, ancho_digito, alto_digito, true);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.fintiempo);
		fintiempo = Bitmap.createScaledBitmap(background, width, height, true);
		int margenx = width / 22;
		int margeny = height / 40;
		for (int i = margeny; i < 11 * height / 14; ++i) {
			for (int j = margenx; j < width - margenx; ++j) {
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

		// if it is the first time the thread starts

		if (thread.getState() == Thread.State.NEW) {
			GameThread.setRunning(true);
			thread.start();
		} else if (thread.getState() == Thread.State.TERMINATED) {
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
		Pair<Integer, Integer> aux = new Pair<Integer, Integer>(
				(int) event.getX(), (int) event.getY());

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (partida_ON)
				lastPair = aux;
			if (fin_finpartida) {
				int x = (int) event.getX();
				int y = (int) event.getY();
				if (x > r.getX() && x < r.getX() + r.getAncho()
						&& y > r.getY() && y < r.getY() + r.getAlto()) {
					apretado = true;
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			apretado = false;
			if (fin_finpartida) {
				int x = (int) event.getX();
				int y = (int) event.getY();
				if (x > r.getX() && x < r.getX() + r.getAncho()
						&& y > r.getY() && y < r.getY() + r.getAlto()) {
					apretado = true;
				}
			}			
			if (partida_ON)
				cola_click.add(aux);
			break;
		case MotionEvent.ACTION_UP:
			if (!partida_ON) {
				beginTime = System.currentTimeMillis();
				last_time = System.currentTimeMillis();
				partida_ON = true;
			}
			if (fin_finpartida && apretado) {
				fin();
			}
			apretado = false;
			break;
		default:
			return false;
		}
		return true;
	}

	public void render(Canvas canvas) {

		 //canvas.drawColor(Color.BLACK);
		/*
		 * if (clickan) { for (int ii = 0; ii < width; ++ii) { for (int jj = 0;
		 * jj < height; ++jj) { if (mask_click[ii][jj]) { for (int i =
		 * xPos-ancho_pinzel/2; i < xPos+ancho_pinzel/2; ++i) { for (int j =
		 * yPos-alto_pinzel/2; j < yPos+alto_pinzel/2; ++j) { Log.i("GameView",
		 * "i: "+i+"\nj: "+j); if (i > width/22 && i < 21*width/22 && j >
		 * height/40 && j < 11*height/14 && !mask_paint[i][j]) {
		 * mask_paint[i][j] = true; lienzo.setPixel(i, j, 0xffff0000); } } }
		 * mask_click[ii][jj] = false; } } } clickan = false; }
		 */

		//canvas.drawBitmap(lienzoBg, 0, 0, null);
		canvas.drawBitmap(bg, 0, 0, null);
		if (partida_ON && !fin_partida) 
		{
			canvas.drawBitmap(lienzo, 0, 0, null);
		}
		if (!partida_ON) {
			if (cambio) canvas.drawBitmap(instruc2, 0, 0, null);
			else canvas.drawBitmap(instruc, 0, 0, null);
		}
			

		if (partida_ON) {
			displayTime(canvas,(10 * 1000 - time_milis)/ 1000,((10 * 1000 - time_milis) - ((10 * 1000 - time_milis) / 1000) * 1000)/ 10);
			displayPercentage(canvas, "" + pixels * 100 / total_pixels + "%");
			displayLevel(canvas, "level: " + level);
		}
		if (fin_partida) {
			if (!fin_finpartida) canvas.drawBitmap(fintiempo, 0, 0, null);
			else {
				canvas.drawBitmap(resultado, 0, 0, null);
				if (apretado) canvas.drawBitmap(botona, width / 3 + width/45, 6 * height / 7 + offset, null);
				else canvas.drawBitmap(boton, width / 3 + width/45, 6 * height / 7 + offset, null);
				pinta_level(canvas);
				pinta_porcentaje(canvas);
				pinta_puntos(canvas);
			}
		}
		displayFps(canvas, avgFps);
	}

	private void pinta_level(Canvas canvas) {
		int dig1 = (int) (level/100);
		int dig2 = (int) ((level-(dig1)*100)/10);
		int dig3 = (int) (level-dig1*100-dig2*10);
		int posx = 7*width/20;
		int posy = 4*height/32;
		dibuja_digito(canvas, dig1,posx,posy);
		posx += width/10;
		dibuja_digito(canvas, dig2,posx,posy);
		posx += width/10;
		dibuja_digito(canvas, dig3,posx,posy);
	}

	private void pinta_porcentaje(Canvas canvas) {
		int px = pixels*100/total_pixels;
		int dig1 = (int) (px/100);
		int dig2 = (int) ((px-(dig1)*100)/10);
		int dig3 = (int) (px-dig1*100-dig2*10);
		int posx = 16*width/40;
		int posy = 51*height/128;
		dibuja_digito(canvas, dig2,posx,posy);
		posx += width/10;
		dibuja_digito(canvas, dig3,posx,posy);
		posx += width/10;
		dibuja_digito(canvas, 10,posx,posy);
	}
	
	private void pinta_puntos(Canvas canvas) {
		int puntos = (int) (level * 100 + (pixels * 100)	/ (0.9 * total_pixels));
		int dig1 = (int) (puntos/10000);
		int dig2 = (int) ((puntos-dig1*10000)/1000);
		int dig3 = (int) ((puntos-dig1*10000-dig2*1000)/100);
		int dig4 = (int) ((puntos-dig1*10000-dig2*1000-dig3*100)/10);
		int dig5 = (int) (puntos-dig1*10000-dig2*1000-dig3*100-dig4*10);
		int posx = 6*width/20;
		int posy = 21*height/32;
		dibuja_digito(canvas, dig1,posx,posy);
		posx += width/10;
		dibuja_digito(canvas, dig2,posx,posy);
		posx += width/10;
		dibuja_digito(canvas, dig3,posx,posy);
		posx += width/10;
		dibuja_digito(canvas, dig4,posx,posy);
		posx += width/10;
		dibuja_digito(canvas, dig5,posx,posy);
	}
	

	/**
	 * This is the game update method. It iterates through all the objects and
	 * calls their update method if they have one or calls specific engine's
	 * update method.
	 */
	public void update() {
		if (!fin_partida) {
			int ancho_pinzel = 10 * width / 110;
			int alto_pinzel = 213 * width / 2800;
	
			if (partida_ON) {
	
				for (int ii = 0; ii < cola_click.size(); ++ii) {
	
					// pintar cuadrado
					// Log.i(TAG,"level<<24: "+Integer.toHexString(level<<24));
					// int pintura = (103409*level+36469)%0x00ffffff;
					int color = 0x88000000 | (0x000000ff << level);
					int x = lastPair.first;
					int y = lastPair.second;
					for (int i = x - ancho_pinzel / 2; i < x + ancho_pinzel / 2; ++i) {
						for (int j = y - alto_pinzel / 2; j < y + alto_pinzel / 2; ++j) {
							// Log.i("GameView", "i: "+i+"\nj: "+j);
							if (i > width / 22 && i < 21 * width / 22
									&& j >= height / 40 && j < 11 * height / 14
									&& !mask_paint[i][j]) {
								mask_paint[i][j] = true;
								pixels++;
								lienzo.setPixel(i, j, color);
							}
						}
					}
					// escoger pasos y esquina
					int pasos = Math.max(
							Math.abs(lastPair.first - cola_click.get(ii).first),
							Math.abs(lastPair.second - cola_click.get(ii).second));
					if (lastPair.first > cola_click.get(ii).first) {
						if (lastPair.second > cola_click.get(ii).second) {// esquina
																			// buena
																			// es la
																			// inversa
																			// (negativa,
																			// negativa)
																			// Log.i(TAG,
																			// "Ha elegido ir para arriba, izquierda");
							for (int i2 = 0; i2 < pasos; ++i2) {
								int i = x
										- ancho_pinzel
										/ 2
										- Math.round(Math.abs(lastPair.first
												- cola_click.get(ii).first)
												* (i2 + 1) / pasos);
								int j = y
										- alto_pinzel
										/ 2
										- Math.round(Math.abs(lastPair.second
												- cola_click.get(ii).second)
												* (i2 + 1) / pasos);
								for (int j2 = 0; j2 < ancho_pinzel; ++j2) {
									if (i + j2 > width / 22
											&& i + j2 < 21 * width / 22
											&& j >= height / 40
											&& j < 11 * height / 14
											&& !mask_paint[i + j2][j]) {
										mask_paint[i + j2][j] = true;
										pixels++;
										lienzo.setPixel(i + j2, j, color);
									}
								}
								for (int j2 = 0; j2 < alto_pinzel; ++j2) {
									if (i > width / 22 && i < 21 * width / 22
											&& j + j2 >= height / 40
											&& j + j2 < 11 * height / 14
											&& !mask_paint[i][j + j2]) {
										mask_paint[i][j + j2] = true;
										pixels++;
										lienzo.setPixel(i, j + j2, color);
									}
								}
							}
						} else if (lastPair.second < cola_click.get(ii).second) {// esquina
																					// buena
																					// es
																					// la
																					// inversa
																					// (negativa,
																					// positiva)
							for (int i2 = 0; i2 < pasos; ++i2) {
								// Log.i(TAG,
								// "Ha elegido ir para abajo, izquierda");
								int i = x
										- ancho_pinzel
										/ 2
										- Math.round(Math.abs(lastPair.first
												- cola_click.get(ii).first)
												* (i2 + 1) / pasos);
								int j = y
										+ alto_pinzel
										/ 2
										+ Math.round(Math.abs(lastPair.second
												- cola_click.get(ii).second)
												* (i2 + 1) / pasos);
								for (int j2 = 0; j2 < ancho_pinzel; ++j2) {
									if (i + j2 > width / 22
											&& i + j2 < 21 * width / 22
											&& j >= height / 40
											&& j < 11 * height / 14
											&& !mask_paint[i + j2][j]) {
										mask_paint[i + j2][j] = true;
										pixels++;
										lienzo.setPixel(i + j2, j, color);
									}
								}
								for (int j2 = 0; j2 < alto_pinzel; ++j2) {
									if (i > width / 22 && i < 21 * width / 22
											&& j - j2 >= height / 40
											&& j - j2 < 11 * height / 14
											&& !mask_paint[i][j - j2]) {
										mask_paint[i][j - j2] = true;
										pixels++;
										lienzo.setPixel(i, j - j2, color);
									}
								}
							}
						}
					} else if (lastPair.first < cola_click.get(ii).first) {
						if (lastPair.second > cola_click.get(ii).second) {// esquina
																			// buena
																			// es la
																			// inversa
																			// (positiva,
																			// negativa)
							for (int i2 = 0; i2 < pasos; ++i2) {
								// Log.i(TAG, "Ha elegido ir para arriba, derecha");
								int i = x
										+ ancho_pinzel
										/ 2
										+ Math.round(Math.abs(lastPair.first
												- cola_click.get(ii).first)
												* (i2 + 1) / pasos);
								int j = y
										- alto_pinzel
										/ 2
										- Math.round(Math.abs(lastPair.second
												- cola_click.get(ii).second)
												* (i2 + 1) / pasos);
								for (int j2 = 0; j2 < ancho_pinzel; ++j2) {
									if (i - j2 > width / 22
											&& i - j2 < 21 * width / 22
											&& j >= height / 40
											&& j < 11 * height / 14
											&& !mask_paint[i - j2][j]) {
										mask_paint[i - j2][j] = true;
										pixels++;
										lienzo.setPixel(i - j2, j, color);
									}
								}
								for (int j2 = 0; j2 < alto_pinzel; ++j2) {
									if (i > width / 22 && i < 21 * width / 22
											&& j + j2 >= height / 40
											&& j + j2 < 11 * height / 14
											&& !mask_paint[i][j + j2]) {
										mask_paint[i][j + j2] = true;
										pixels++;
										lienzo.setPixel(i, j + j2, color);
									}
								}
							}
	
						} else if (lastPair.second < cola_click.get(ii).second) {// esquina
																					// buena
																					// es
																					// la
																					// inversa
																					// (positiva,
																					// positiva)
							for (int i2 = 0; i2 < pasos; ++i2) {
								// Log.i(TAG, "Ha elegido ir para abajo, derecha");
								int i = x
										+ ancho_pinzel
										/ 2
										+ Math.round(Math.abs(lastPair.first
												- cola_click.get(ii).first)
												* (i2 + 1) / pasos);
								int j = y
										+ alto_pinzel
										/ 2
										+ Math.round(Math.abs(lastPair.second
												- cola_click.get(ii).second)
												* (i2 + 1) / pasos);
								for (int j2 = 0; j2 < ancho_pinzel; ++j2) {
									if (i - j2 > width / 22
											&& i - j2 < 21 * width / 22
											&& j >= height / 40
											&& j < 11 * height / 14
											&& !mask_paint[i - j2][j]) {
										mask_paint[i - j2][j] = true;
										pixels++;
										lienzo.setPixel(i - j2, j, color);
									}
								}
								for (int j2 = 0; j2 < alto_pinzel; ++j2) {
									if (i > width / 22 && i < 21 * width / 22
											&& j - j2 >= height / 40
											&& j - j2 < 11 * height / 14
											&& !mask_paint[i][j - j2]) {
										mask_paint[i][j - j2] = true;
										pixels++;
										lienzo.setPixel(i, j - j2, color);
									}
								}
							}
						}
					}
					lastPair = new Pair<Integer, Integer>(cola_click.get(ii).first,
							cola_click.get(ii).second);
	
				}
				cola_click = new ArrayList<Pair<Integer, Integer>>();
				if (pixels >= 0.9 * total_pixels) {
					pixels = 0;
					lienzo = Bitmap.createBitmap(width, 6 * height / 7,
							Bitmap.Config.ARGB_8888);
					mask_paint = new boolean[width][height];

					editor.putInt(getContext().getString(R.string.lrt),
							Math.min(sharedPref.getInt(getContext().getString(R.string.lrt),Integer.MAX_VALUE),
							(int) (System.currentTimeMillis()-(last_time))));
					last_time = System.currentTimeMillis();
					beginTime = Math.min(System.currentTimeMillis(), beginTime
							+ tiempo_regeneracion);
					level++;
				}
				time_milis = System.currentTimeMillis() - beginTime;
				if (time_milis > 10 * 1000 || level > 999) {
					time_milis = 10000;
					tiempo_finpartida = System.currentTimeMillis();
					fin_partida = true;
					guardar_puntos();
					//Games.Leaderboards.submitScore(getApiClient(), "CgkIpbKptu8KEAIQAA", (int) (level * 100 + (pixels * 100)	/ (0.9 * total_pixels)));
					
					// Log.i("GameView", "Partida al: "+pixels*100/total_pixels);
				}
			}
			else {
				if (System.currentTimeMillis() - tiempo_parpadeo > 500)
				{
					tiempo_parpadeo = System.currentTimeMillis();
					cambio = !cambio;
				}
			}
		}
		else {
			if (!fin_finpartida && System.currentTimeMillis() - tiempo_finpartida > 2000)
			{
				fin_finpartida = true;
			}
		}

	}

	private void guardar_puntos() {
		editor.putInt("puntos_normal_aux", 
				Math.max(sharedPref.getInt("puntos_normal_aux", 0), (int) (level * 100 + (pixels * 100)	/ (0.9 * total_pixels)))
				);
		editor.putBoolean("puntos_normal_bool", 
				false);
		Long ultimo = sharedPref.getLong("ultimo_tiempo", 0);
		if (tiempo_finpartida-ultimo > 1440*60*1000) 
		{
			editor.putInt(getContext().getString(R.string.dsjt), 0);
			editor.putLong("dia_referencia", tiempo_finpartida);
		}
		else
		{
			Long dia_ref = sharedPref.getLong("dia_referencia", 0) + 1440*60000;
			if(ultimo < dia_ref && dia_ref < tiempo_finpartida) {
				editor.putLong("dia_referencia", dia_ref);
				editor.putInt(getContext().getString(R.string.dsjt), sharedPref.getInt(getContext().getString(R.string.dsjt),0)+1);
			}
		}
		editor.putLong("ultimo_tiempo", tiempo_finpartida);
		if (resistencia) 
		{
			editor.putInt(getContext().getString(R.string.prt), sharedPref.getInt(getContext().getString(R.string.prt),0)+1);
			editor.putInt(getContext().getString(R.string.lmrt), Math.max(sharedPref.getInt(getContext().getString(R.string.lmrt),0),level));
		}
		else 
		{
			editor.putInt(getContext().getString(R.string.pnt), sharedPref.getInt(getContext().getString(R.string.pnt),0)+1);
			editor.putInt(getContext().getString(R.string.lmnt), Math.max(sharedPref.getInt(getContext().getString(R.string.lmnt),0),level));
		}
		editor.commit();
	}

	private void displayFps(Canvas canvas, String fps) {
		if (canvas != null && fps != null) {
			canvas.drawText(fps, this.getWidth() - 100, 50, paint);
		}
	}

	private void displayPercentage(Canvas canvas, String perc) {
		if (canvas != null && perc != null) {
			canvas.drawText(perc, this.getWidth() / 18, 50, paint);
		}
	}

	private void displayTime(Canvas canvas, long sec, long cent) {
		int dig1 = (int) (sec/10);
		int dig2 = (int) (sec-(sec/10)*10);
		int dig3 = (int) (cent/10);
		int dig4 = (int) (cent-(cent/10)*10);
		int posx = 6*width/44;
		int posy = 41*height/48;
		dibuja_digito(canvas, dig1,posx,posy);
		posx += width/12;
		dibuja_digito(canvas, dig2,posx,posy);
		posx += width/8;
		dibuja_digito(canvas, dig3,posx,posy);
		posx += width/12;
		dibuja_digito(canvas, dig4,posx,posy);
	}
	
	private void dibuja_digito(Canvas canvas, int dig1, int posx, int posy) {
		if (canvas != null) {
			switch (dig1) {
				case 0:
					canvas.drawBitmap(cero, posx, posy, null);
				break;
				case 1:
					canvas.drawBitmap(uno, posx, posy, null);
				break;
				case 2:
					canvas.drawBitmap(dos, posx, posy, null);
				break;
				case 3:
					canvas.drawBitmap(tres, posx, posy, null);
				break;
				case 4:
					canvas.drawBitmap(cuatro, posx, posy, null);
				break;
				case 5:
					canvas.drawBitmap(cinco, posx, posy, null);
				break;
				case 6:
					canvas.drawBitmap(seis, posx, posy, null);
				break;
				case 7:
					canvas.drawBitmap(siete, posx, posy, null);
				break;
				case 8:
					canvas.drawBitmap(ocho, posx, posy, null);
				break;
				case 9:
					canvas.drawBitmap(nueve, posx, posy, null);
				break;
				case 10:
					canvas.drawBitmap(porcentaje, posx, posy, null);
				break;
				default:
					//nunca deberia entrar
			}
		}
	}

	private void displayLevel(Canvas canvas, String level) {
		if (canvas != null && level != null) {
			canvas.drawText(level, this.getWidth() / 18, 6 * height / 7 - 100,
					paint);
		}
	}
	
	private void fin() {
		displayInterstitial();
		((Activity) getContext()).finish();	
	}

	// Invoke displayInterstitial() when you are ready to display an interstitial.
	  public void displayInterstitial() {
	    if (interstitial.isLoaded()) {
	      interstitial.show();
	    }
	    else Log.i("Game","Ha fallado el isLoaded() de la publicidad.");
	  }
}
