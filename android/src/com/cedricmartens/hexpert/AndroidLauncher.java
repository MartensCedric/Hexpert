package com.cedricmartens.hexpert;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.cedricmartens.hexpert.social.PlayServices;
import com.cedricmartens.hexpert.social.Purchasing;
import com.cedricmartens.hexpert.social.Sharing;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

import java.util.HashMap;

public class AndroidLauncher extends AndroidApplication implements PlayServices, Sharing, Purchasing, BillingProcessor.IBillingHandler {

	private BillingProcessor bp;
	private GameHelper gameHelper;
	private HashMap<String, String> leaderboardsMap;
	{
		leaderboardsMap = new HashMap<>();
		leaderboardsMap.put("handball", "CgkIq5XDzvcOEAIQDg");
		leaderboardsMap.put("holywater", "CgkIq5XDzvcOEAIQDw");
		leaderboardsMap.put("carrot", "CgkIq5XDzvcOEAIQIw");
		leaderboardsMap.put("berry", "CgkIq5XDzvcOEAIQJA");
		leaderboardsMap.put("triForest", "CgkIq5XDzvcOEAIQEA");
		leaderboardsMap.put("tightAroundWater", "CgkIq5XDzvcOEAIQEQ");
		leaderboardsMap.put("bent", "CgkIq5XDzvcOEAIQEg");
		leaderboardsMap.put("long", "CgkIq5XDzvcOEAIQEw");
		leaderboardsMap.put("smlLos", "CgkIq5XDzvcOEAIQFA");
		leaderboardsMap.put("asyLong", "CgkIq5XDzvcOEAIQFQ");
		leaderboardsMap.put("triHex", "CgkIq5XDzvcOEAIQFg");
		leaderboardsMap.put("triSoft", "CgkIq5XDzvcOEAIQFw");
		leaderboardsMap.put("asyHandball", "CgkIq5XDzvcOEAIQGA");
		leaderboardsMap.put("asyDiag", "CgkIq5XDzvcOEAIQGQ");
		leaderboardsMap.put("asyQuadSpike", "CgkIq5XDzvcOEAIQGg");
		leaderboardsMap.put("asyDiagLos", "CgkIq5XDzvcOEAIQGw");
		leaderboardsMap.put("vBone", "CgkIq5XDzvcOEAIQHA");
		leaderboardsMap.put("classic", "CgkIq5XDzvcOEAIQHQ");
		leaderboardsMap.put("triPlane", "CgkIq5XDzvcOEAIQHg");
		leaderboardsMap.put("triBankLckd", "CgkIq5XDzvcOEAIQHw");
		leaderboardsMap.put("hexcity", "CgkIq5XDzvcOEAIQIA");
		leaderboardsMap.put("wobblyPlane", "CgkIq5XDzvcOEAIQIQ");
		leaderboardsMap.put("crown", "CgkIq5XDzvcOEAIQIg");
		leaderboardsMap.put("spinner", "CgkIq5XDzvcOEAIQJQ");
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String pKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk7CvCwFg6zjMPmczjAxaLlUjiriAudAOS1a+AFAC5KsA1ZeK7dW8g2spFW7ukmLaSAKQWqGgaKwrs+hntgbMsp2cPWe1x77z8gISdXUNZB2XuzjZqUTzRaGU3zoO9kjBAqQmSaZIo7hLb2hn70Yzeoo31fV4m1koHsNtzpMUksYpFwuD/HVJifHeO9E1bvea6ljidGtYz10hdoF/t+Lp77Exv/17pD9IbE8dKO/j0p/MFIAz0LF1+vyhg/bhpZs2X8Y3dWVv3fkthScj4Wemu8yDksTpCH6baDwjeZ5qe7eAoA3KnQsR7kssiUNXWWtUTUOON1GlOqcHVu9BJW52GwIDAQAB";
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);

		bp = new BillingProcessor(this, pKey, this);

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
		initialize(new Hexpert(this, this, this), config);
	}

	@Override
	protected void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		gameHelper.onStop();
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
		if (!bp.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onDestroy() {
		if(bp != null)
			bp.release();
		super.onDestroy();
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
		Uri uri = Uri.parse("market://details?id=" + getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

		goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
				Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
				Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		try {
			startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
		}
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

	@Override
	public void shareTextAndImage(String message, String picturePath) {
		Intent sendIntent = new Intent();

		Uri picture = Uri.parse(picturePath);
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, message);
		sendIntent.putExtra(Intent.EXTRA_STREAM, picture);
		sendIntent.setType("image/*");
		sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		startActivity(Intent.createChooser(sendIntent, "Share images..."));
	}

	@Override
	public void purchase(Amount amount)
	{
		String desiredItem = "";
		switch (amount)
		{
			case ZERO:
				return;
			case ONE:
				desiredItem = "pwyw_one";
				break;
			case TWO:
				desiredItem = "pwyw_two";
				break;
			case THREE:
				desiredItem = "pwyw_three";
				break;
			case FOUR:
				desiredItem = "pwyw_four";
				break;
			case FIVE:
				desiredItem = "pwyw_five";
				break;
			case SIX:
				desiredItem = "pwyw_six";
				break;
			case SEVEN:
				desiredItem = "pwyw_seven";
				break;
			case EIGHT:
				desiredItem = "pwyw_eight";
				break;
			case NINE:
				desiredItem = "pwyw_nine";
				break;
			case TEN:
				desiredItem = "pwyw_ten";
				break;

		}

		bp.purchase(this, desiredItem);
	}

	@Override
	public boolean hasPurchased() {
		return false;
	}

	@Override
	public void onProductPurchased(String productId, TransactionDetails details) {
		Log.e("BILLING", productId + " has been purchased!");
	}

	@Override
	public void onPurchaseHistoryRestored() {

	}

	@Override
	public void onBillingError(int errorCode, Throwable error) {

	}

	@Override
	public void onBillingInitialized() {

	}
}
