package com.martenscedric.hexpert;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.martenscedric.hexpert.social.PlayServices;
import com.martenscedric.hexpert.social.Sharing;

import java.util.HashMap;

public class AndroidLauncher extends AndroidApplication implements PlayServices, Sharing {

	private GameHelper gameHelper;
	private HashMap<String, String> leaderboardsMap;

	{
		leaderboardsMap = new HashMap<>();
		leaderboardsMap.put("handball", "CgkIq5XDzvcOEAIQDg");
		leaderboardsMap.put("", "");
		leaderboardsMap.put("", "");
		leaderboardsMap.put("", "");
		leaderboardsMap.put("", "");
		leaderboardsMap.put("", "");
		leaderboardsMap.put("", "");
		leaderboardsMap.put("", "");
		leaderboardsMap.put("", "");
		leaderboardsMap.put("", "");
		leaderboardsMap.put("", "");
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);

		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
			@Override
			public void onSignInFailed() {

			}

			@Override
			public void onSignInSucceeded() {

			}
		};

		gameHelper.setup(gameHelperListener);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Hexpert(this, this), config);
	}


	@Override
	protected void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void signIn() {
		try
		{
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void signOut() {
		try
		{
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gameHelper.signOut();
				}
			});
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void rateGame() {

	}

	@Override
	public void submitScore(int score, String leaderboard) {
		Games.Leaderboards.submitScore(gameHelper.getApiClient(), leaderboardsMap.get(leaderboard), score);
	}

	@Override
	public void showLeaderboardUI(String leaderboard) {
		startActivityForResult(
				Games.Leaderboards.getLeaderboardIntent(
						gameHelper.getApiClient(), leaderboardsMap.get(leaderboard)), 0);

	}


	@Override
	public void unlockAchievement(String id) {
		try{
			Games.Achievements.unlock(gameHelper.getApiClient(), id);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean isSignedIn() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void showAchievementsUI()
	{
		try{
			Intent intent = Games.Achievements.getAchievementsIntent(gameHelper.getApiClient());
			startActivityForResult(intent, 1);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void shareText(String message) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, message);
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, "Share via ..."));
	}
}
