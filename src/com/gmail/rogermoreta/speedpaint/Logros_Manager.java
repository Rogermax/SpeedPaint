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
	
	public Logros_Manager(BaseGameActivity gameActivity, GoogleApiClient googleApiClient) {
		BGA = gameActivity;
		GAP = googleApiClient;
		callback = new ResultCallback<Achievements.LoadAchievementsResult>() {
			
			@Override
			public void onResult(LoadAchievementsResult arg0) {
				AchievementBuffer ab = arg0.getAchievements();
				for (int i = 0; i < ab.getCount(); ++i)
				{
					Log.i("Logros_Manager","El elemento "+i+": "+ab.get(i).getName());
				}
				
			}
		};
		sharedPref = BGA.getSharedPreferences(BGA.getString(R.string.sharedPoints),	Context.MODE_PRIVATE);
		first_time = isFirstTime();
		if (first_time) {
			Lee_datos_online();
		}
		Incrementa_y_desbloquea_logros();
	}

	private void Incrementa_y_desbloquea_logros() {
		trata_logros_partidas_normales_jugadas();
		trata_logros_partidas_resistencia_jugadas();
		//trata_logros_lienzo_rapido();
		//trata_logros_partidas_seguidas_jugadas();
		//trata_logros_level_maximo_normal();
		//trata_logros_level_maximo_resistencia();
		
	}

	private void trata_logros_partidas_normales_jugadas() {
		int partidas_a_subir = sharedPref.getInt(BGA.getString(R.string.pnt), 0);
		int partidas = sharedPref.getInt(BGA.getString(R.string.pn), 0);
		if (partidas_a_subir > 0) 
		{//Si entra aki sk hace falta incrementar.
			if (partidas < 10) Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_10_matches), partidas_a_subir);
			if (partidas < 100) Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_100_matches), partidas_a_subir);
			if (partidas < 1000) Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_1000_matches), partidas_a_subir);
		}
	}
	
	private void trata_logros_partidas_resistencia_jugadas() {
		int partidas_a_subir = sharedPref.getInt(BGA.getString(R.string.prt), 0);
		int partidas = sharedPref.getInt(BGA.getString(R.string.pr), 0);
		if (partidas_a_subir > 0) 
		{//Si entra aki sk hace falta incrementar.
			if (partidas < 10) Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_10_resistance_matches), partidas_a_subir);
			if (partidas < 100) Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_100_resistance_matches), partidas_a_subir);
			if (partidas < 1000) Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_1000_resistance_matches), partidas_a_subir);
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