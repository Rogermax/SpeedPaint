package com.gmail.rogermoreta.speedpaint;

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
				int i1=0,i2=0,i3=0,i4=0,i5=0,level_logros, levels_a_subir;
				int j3=0,j4=0;
				int jj1=0,jj2=0,jj3=0,jj4=0;
				for (int i = 0; i < ab.getCount(); ++i) {
					//partidas normal temporal
					if (ab.get(i).getAchievementId().equals(BGA.getString(R.string.achievement_1000_matches)))
						jj1=i;
					else if (ab.get(i).getAchievementId().equals(BGA.getString(R.string.achievement_1000_resistance_matches)))
						jj2=i;
					else if (ab.get(i).getAchievementId().equals(BGA.getString(R.string.achievement_level_6)))
						jj3=i;
					else if (ab.get(i).getAchievementId().equals(BGA.getString(R.string.achievement_maximum_level_in_resistance)))
						jj4=i;
					else if (ab.get(i).getAchievementId().equals(BGA.getString(R.string.achievement_level_1)))
						j3=i;
					else if (ab.get(i).getAchievementId().equals(BGA.getString(R.string.achievement_level_2_resistance)))
						j4=i;
					else if (ab.get(i).getAchievementId().equals(BGA.getString(R.string.achievement_1_seconds)))
						i5 = i;
					else if (ab.get(i).getAchievementId().equals(BGA.getString(R.string.achievement_2_seconds)))
						i4 = i;
					else if (ab.get(i).getAchievementId().equals(BGA.getString(R.string.achievement_3_seconds)))
						i3 = i;
					else if (ab.get(i).getAchievementId().equals(BGA.getString(R.string.achievement_4_seconds)))
						i2 = i;
					else if (ab.get(i).getAchievementId().equals(BGA.getString(R.string.achievement_5_seconds)))
						i1 = i;
				}
				if (ab.get(j3).getState() == Achievement.STATE_UNLOCKED)
				{
					//partidas normal temporal
					level_logros = ab.get(jj1).getCurrentSteps();
					levels_a_subir = sharedPref.getInt(BGA.getString(R.string.pnt), 0);
					editor.putInt(BGA.getString(R.string.pnt), Math.max(levels_a_subir,level_logros));
					Log.i("Logros_Manager_create_pnt", ab.get(jj1).getName()+": "+level_logros);
					
					//level maximo normal temporal
					level_logros = ab.get(jj3).getCurrentSteps();
					levels_a_subir = sharedPref.getInt(BGA.getString(R.string.lmnt), 0);
					editor.putInt(BGA.getString(R.string.lmnt), Math.max(levels_a_subir,level_logros));
					Log.i("Logros_Manager_create_lmnt",  ab.get(jj3).getName()+": "+level_logros);
				}
				if (ab.get(j4).getState() == Achievement.STATE_UNLOCKED)
				{
					//partidas resistencia temporal
					level_logros = ab.get(jj2).getCurrentSteps();
					levels_a_subir = sharedPref.getInt(BGA.getString(R.string.prt), 0);
					editor.putInt(BGA.getString(R.string.prt), Math.max(levels_a_subir,level_logros));
					Log.i("Logros_Manager_create_prt",  ab.get(jj2).getName()+": "+level_logros);
					
					//level maximo resistencia temporal
					level_logros = ab.get(jj4).getCurrentSteps();
					levels_a_subir = sharedPref.getInt(BGA.getString(R.string.lmrt), 0);
					editor.putInt(BGA.getString(R.string.lmrt), Math.max(levels_a_subir,level_logros));
					Log.i("Logros_Manager_create_lmrt",  ab.get(jj4).getName()+": "+level_logros);
				}
				//lienzo_rapido
				if (ab.get(i1).getState() == Achievement.STATE_UNLOCKED)
				{
					if (ab.get(i2).getState() == Achievement.STATE_UNLOCKED) 
					{
						if (ab.get(i3).getState() == Achievement.STATE_UNLOCKED) 
						{
							if (ab.get(i4).getState() == Achievement.STATE_UNLOCKED) 
							{
								if (ab.get(i5).getState() == Achievement.STATE_UNLOCKED) 
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
				editor.putInt(BGA.getString(R.string.lrt), level_logros);
				Log.i("Logros_Manager_create_lrt", ""+level_logros);
				
				
				
				//for (int i = 0; i < ab.getCount(); ++i)
				//{
				//	Log.i("Logros_Manager","El elemento "+i+": "+ab.get(i).getName());
				//}
				Incrementa_y_desbloquea_logros();
				BGA.startActivityForResult(Games.Achievements.getAchievementsIntent(GAP), 2);
			}
		};
		sharedPref = BGA.getSharedPreferences(BGA.getString(R.string.sharedPoints),	Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		first_time = isFirstTime();
		if (first_time) {
			//Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_1000_matches), 0);
			//Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_1000_resistance_matches), 0);
			//Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_maximum_level_in_resistance), 0);
			//Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_level_6), 0);
			Lee_datos_online();
			editor.putBoolean("first_time", false);
			editor.commit();
		}
		else 
		{
			Incrementa_y_desbloquea_logros();
			BGA.startActivityForResult(Games.Achievements.getAchievementsIntent(GAP), 2);
		}
	}

	private void Incrementa_y_desbloquea_logros() {
		trata_logros_partidas_normales_jugadas();
		trata_logros_partidas_resistencia_jugadas();
		trata_logros_lienzo_rapido();
		trata_logros_partidas_seguidas_jugadas();
		trata_logros_level_maximo_normal();
		trata_logros_level_maximo_resistencia();
		editor.commit();
	}

	private void trata_logros_partidas_seguidas_jugadas() {
		int days = sharedPref.getInt(BGA.getString(R.string.dsj), 0);
		int days_a_subir = sharedPref.getInt(BGA.getString(R.string.dsjt), 0)-days;
		if (days_a_subir > 0) 
		{//Si entra aki sk hace falta incrementar.
			if (days < 3) 
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_3_days),days_a_subir);
				if ((days+days_a_subir) >= 3) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_7_days));
			}
			if (days < 7) 
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_7_days),days_a_subir);
				if ((days+days_a_subir) >= 7) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_30_days));
			}if (days < 30) 
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_30_days),days_a_subir);
				if ((days+days_a_subir) >= 30) Games.Achievements.reveal(GAP, BGA.getString(R.string.achievement_365_days));
			}
			if (days < 365)
			{
				Games.Achievements.increment(GAP, BGA.getString(R.string.achievement_365_days),days_a_subir);
			}
			editor.putInt(BGA.getString(R.string.dsj), days+days_a_subir);
			editor.putInt(BGA.getString(R.string.dsjt), 0);
		}
	}

	//Levels, es el level subido en internet;
	//levels a subir es el level para subir a levels, despues incrementar todo.
	private void trata_logros_level_maximo_normal() {
		int levels = sharedPref.getInt(BGA.getString(R.string.lmn), 0);
		int levels_a_subir = sharedPref.getInt(BGA.getString(R.string.lmnt), 0)-levels;
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
		int levels = sharedPref.getInt(BGA.getString(R.string.lmr), 0)- levels_a_subir;
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
		int tiempo_a_subir = sharedPref.getInt(BGA.getString(R.string.lrt), Integer.MAX_VALUE);
		int tiempo = sharedPref.getInt(BGA.getString(R.string.lr), Integer.MAX_VALUE);
		Log.i("Logros_Manager", ""+tiempo_a_subir);
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
}