package com.gmail.rogermoreta.speedpaint;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements;
import com.google.android.gms.games.achievement.Achievements.LoadAchievementsResult;
import com.google.example.games.basegameutils.BaseGameActivity;

public class Logros_Manager  {

	private GoogleApiClient GAP;
	private BaseGameActivity BGA;
	private ResultCallback<Achievements.LoadAchievementsResult> callback;
	private boolean first_time;
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	
	public Logros_Manager(BaseGameActivity gameActivity, GoogleApiClient googleApiClient) {
		BGA = gameActivity;
		GAP = googleApiClient;
		callback = new ResultCallback<Achievements.LoadAchievementsResult>() {
			
			@Override
			public void onResult(LoadAchievementsResult arg0) {
				AchievementBuffer ab = arg0.getAchievements();
				
				//partidas maximo normal temporal
				int level_logros = ab.get(20).getCurrentSteps();
				int levels_a_subir = sharedPref.getInt(BGA.getString(R.string.pnt), 0);
				editor.putInt(BGA.getString(R.string.pnt), Math.max(levels_a_subir,level_logros));
				
				//level maximo resistencia temporal
				level_logros = ab.get(21).getCurrentSteps();
				levels_a_subir = sharedPref.getInt(BGA.getString(R.string.prt), 0);
				editor.putInt(BGA.getString(R.string.prt), Math.max(levels_a_subir,level_logros));
				
				//level maximo normal temporal
				level_logros = ab.get(22).getCurrentSteps();
				levels_a_subir = sharedPref.getInt(BGA.getString(R.string.lmnt), 0);
				editor.putInt(BGA.getString(R.string.lmnt), Math.max(levels_a_subir,level_logros));
				
				//level maximo resistencia temporal
				level_logros = ab.get(23).getCurrentSteps();
				levels_a_subir = sharedPref.getInt(BGA.getString(R.string.lmrt), 0);
				editor.putInt(BGA.getString(R.string.lmrt), Math.max(levels_a_subir,level_logros));
				
				//lienzo_rapido
				if (ab.get(2).getState() == Achievement.STATE_REVEALED)
				{
					if (ab.get(5).getState() == Achievement.STATE_REVEALED) 
					{
						if (ab.get(12).getState() == Achievement.STATE_REVEALED) 
						{
							if (ab.get(16).getState() == Achievement.STATE_REVEALED) 
							{
								if (ab.get(24).getState() == Achievement.STATE_REVEALED) 
								{
									level_logros = 1000;
								}
								else level_logros = 2000;
							}
							else level_logros = 3000;
						}
						else level_logros = 4000;
					}
					else level_logros = 5000;
				}
				else level_logros = Integer.MAX_VALUE;
				editor.putInt(BGA.getString(R.string.lmnt), level_logros);
				
				
				
				//for (int i = 0; i < ab.getCount(); ++i)
				//{
				//	Log.i("Logros_Manager","El elemento "+i+": "+ab.get(i).getName());
				//}
				Incrementa_y_desbloquea_logros();
			}
		};
		sharedPref = BGA.getSharedPreferences(BGA.getString(R.string.sharedPoints),	Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		first_time = isFirstTime();
		if (first_time) {
			Lee_datos_online();
		}
		else Incrementa_y_desbloquea_logros();
	}

	private void Incrementa_y_desbloquea_logros() {
		trata_logros_partidas_normales_jugadas();
		trata_logros_partidas_resistencia_jugadas();
		trata_logros_lienzo_rapido();
		//trata_logros_partidas_seguidas_jugadas();
		trata_logros_level_maximo_normal();
		trata_logros_level_maximo_resistencia();
		
	}

	//Levels, es el level subido en internet;
	//levels a subir es el level para subir a levels, despues incrementar todo.
	private void trata_logros_level_maximo_normal() {
		int levels_a_subir = sharedPref.getInt(BGA.getString(R.string.lmnt), 0);
		int levels = sharedPref.getInt(BGA.getString(R.string.lmn), 0);
		if (levels_a_subir > 0) 
		{//Si entra aki sk hace falta incrementar.
			if (levels < 1) 
			{
				Games.Achievements.unlock(GAP, BGA.getString(R.string.achievement_level_1));
				Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_level_3));
			}
			if (levels < 3)
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_level_3), levels_a_subir);
				if ((levels+levels_a_subir) >= 3) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_level_5));
			}
			if (levels < 5)
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_level_5), levels_a_subir);
				if ((levels+levels_a_subir) >= 5) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_level_6));
			}
			if (levels < 6)
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_level_6), levels_a_subir);
			}
			editor.putInt(BGA.getString(R.string.lmn), levels+levels_a_subir);
			editor.putInt(BGA.getString(R.string.lmnt), 0);
		}
	}
	
	//Levels, es el level subido en internet;
	//levels a subir es el level para subir a levels, despues incrementar todo.
	private void trata_logros_level_maximo_resistencia() {
		int levels_a_subir = sharedPref.getInt(BGA.getString(R.string.lmrt), 0);
		int levels = sharedPref.getInt(BGA.getString(R.string.lmr), 0);
		if (levels_a_subir > 0) 
		{//Si entra aki sk hace falta incrementar.
			if (levels < 2) 
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_level_2_resistance), levels_a_subir);
				if ((levels+levels_a_subir) >= 2) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_level_10_resistance));
			}
			if (levels < 10)
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_level_10_resistance), levels_a_subir);
				if ((levels+levels_a_subir) >= 10) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_level_25_resistance));
			}
			if (levels < 25)
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_level_25_resistance), levels_a_subir);
				if ((levels+levels_a_subir) >= 25) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_level_100_resistance));
			}
			if (levels < 100)
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_level_100_resistance), levels_a_subir);
				if ((levels+levels_a_subir) >= 100) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_level_500_resistance));
			}
			if (levels < 500)
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_level_500_resistance), levels_a_subir);
				if ((levels+levels_a_subir) >= 500) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_maximum_level_in_resistance));
			}
			if (levels < 999)
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_maximum_level_in_resistance), levels_a_subir);
			}
			editor.putInt(BGA.getString(R.string.lmr), levels+levels_a_subir);
			editor.putInt(BGA.getString(R.string.lmrt), 0);
		}
	}

	private void trata_logros_lienzo_rapido() {
		int tiempo_a_subir = sharedPref.getInt(BGA.getString(R.string.lrt), 0);
		int tiempo = sharedPref.getInt(BGA.getString(R.string.lr), 0);
		if (tiempo > tiempo_a_subir) 
		{//Si entra aki sk hace falta incrementar.
			if (tiempo > 5000 && tiempo_a_subir <= 5000)
			{
				Games.Achievements.unlock(GAP, BGA.getString(R.string.achievement_5_seconds));
				Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_4_seconds));
			}
			if (tiempo > 4000 && tiempo_a_subir <= 4000)
			{
				Games.Achievements.unlock(GAP, BGA.getString(R.string.achievement_4_seconds));
				Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_3_seconds));
			}
			if (tiempo > 3000 && tiempo_a_subir <= 3000)
			{
				Games.Achievements.unlock(GAP, BGA.getString(R.string.achievement_3_seconds));
				Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_2_seconds));
			}
			if (tiempo > 2000 && tiempo_a_subir <= 2000)
			{
				Games.Achievements.unlock(GAP, BGA.getString(R.string.achievement_2_seconds));
				Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_1_seconds));
			}
			if (tiempo > 1000 && tiempo_a_subir <= 1000) Games.Achievements.unlock(GAP, BGA.getString(R.string.achievement_1_seconds));
			editor.putInt(BGA.getString(R.string.lmn), tiempo_a_subir);
			editor.putInt(BGA.getString(R.string.lmnt), Integer.MAX_VALUE);
		}
	}

	private void trata_logros_partidas_normales_jugadas() {
		int partidas_a_subir = sharedPref.getInt(BGA.getString(R.string.pnt), 0);
		int partidas = sharedPref.getInt(BGA.getString(R.string.pn), 0);
		if (partidas_a_subir > 0) 
		{//Si entra aki sk hace falta incrementar.
			if (partidas < 10)
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_10_matches), partidas_a_subir);
				if ((partidas+partidas_a_subir) >= 10) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_100_matches));
			}
			if (partidas < 100)
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_100_matches), partidas_a_subir);
				if ((partidas+partidas_a_subir) >= 100) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_1000_matches));
			}
			if (partidas < 1000) Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_1000_matches), partidas_a_subir);
			editor.putInt(BGA.getString(R.string.pn), partidas+partidas_a_subir);
			editor.putInt(BGA.getString(R.string.pnt), 0);
		}
	}
	
	private void trata_logros_partidas_resistencia_jugadas() {
		int partidas_a_subir = sharedPref.getInt(BGA.getString(R.string.prt), 0);
		int partidas = sharedPref.getInt(BGA.getString(R.string.pr), 0);
		if (partidas_a_subir > 0) 
		{//Si entra aki sk hace falta incrementar.
			if (partidas < 10)
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_10_resistance_matches), partidas_a_subir);
				if ((partidas+partidas_a_subir) >= 10) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_100_resistance_matches));
			}
			if (partidas < 100)
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_100_resistance_matches), partidas_a_subir);
				if ((partidas+partidas_a_subir) >= 100) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_1000_resistance_matches));
			}
			if (partidas < 1000) Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_1000_resistance_matches), partidas_a_subir);
			editor.putInt(BGA.getString(R.string.pr), partidas+partidas_a_subir);
			editor.putInt(BGA.getString(R.string.prt), 0);
		}
	}

	private boolean isFirstTime() {
		return sharedPref.getBoolean("first_time", true);
	}

	private void Lee_datos_online() {
		Games.Achievements.load(GAP, true).setResultCallback(callback);
	}

	public void Lee_datos_offline() {
		//Games.Achievements.load(arg0, arg1);
		//GAP.
		//Lee_marcadores_de_aviso();
	}

	public void actualiza_logros() {
		SharedPreferences sharedPref = BGA.getSharedPreferences(
				BGA.getString(R.string.sharedPoints),
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		
		
		
		
		
		if (!sharedPref.getBoolean("puntos_normal_bool", false))
		{
			
		}
		AchievementBuffer aux = Games.Achievements.load(GAP,false).await().getAchievements();
		for (int i = 0; i < aux.getCount(); ++i) {
			Achievement ach = aux.get(i);
			if (ach.getAchievementId().equals(BGA.getString(R.string.achievement_level_1))) {
				if (ach.getState() != Achievement.STATE_UNLOCKED /*&& hay cambios en nº level maximo completo*/)
				{
					//deslockea o avanza 
				}
			}
		}
	}
}