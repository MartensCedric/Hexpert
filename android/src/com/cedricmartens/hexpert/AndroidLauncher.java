package com.cedricmartens.hexpert;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.cedricmartens.hexpert.social.Purchasing;
import com.cedricmartens.hexpert.social.PlayServices;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.cedricmartens.hexpert.social.Sharing;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Billing;
import org.solovyev.android.checkout.BillingRequests;
import org.solovyev.android.checkout.Cache;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.PurchaseVerifier;

import java.util.HashMap;
import java.util.concurrent.Executor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AndroidLauncher extends AndroidApplication implements PlayServices, Sharing, Purchasing {

	private GameHelper gameHelper;

	private HashMap<String, String> leaderboardsMap;
	{
		leaderboardsMap = new HashMap<>();
		leaderboardsMap.put("handball", "CgkIq5XDzvcOEAIQDg");
		leaderboardsMap.put("holywater", "CgkIq5XDzvcOEAIQDw");
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

		Billing billing = new Billing(this, new Billing.Configuration(){

			@Nonnull
			@Override
			public String getPublicKey() {
				return "";
			}

			@Nullable
			@Override
			public Cache getCache() {
				return null;
			}

			@Nonnull
			@Override
			public PurchaseVerifier getPurchaseVerifier() {
				return null;
			}

			@Nullable
			@Override
			public Inventory getFallbackInventory(@Nonnull Checkout checkout, @Nonnull Executor onLoadExecutor) {
				return null;
			}

			@Override
			public boolean isAutoConnect() {
				return false;
			}
		});

		final ActivityCheckout checkout = Checkout.forActivity(this, billing);
		checkout.start();

		Inventory inventory = checkout.makeInventory();
		final String finalDesiredItem = desiredItem;
		inventory.load(Inventory.Request.create()
				.loadAllPurchases()
				.loadSkus(ProductTypes.IN_APP, desiredItem), new Inventory.Callback() {
			@Override
			public void onLoaded(@Nonnull Inventory.Products products) {
				checkout.whenReady(new Checkout.EmptyListener() {
					@Override
					public void onReady(BillingRequests requests) {
						requests.purchase(ProductTypes.IN_APP, finalDesiredItem, null, checkout.getPurchaseFlow());
					}
				});
			}
		});

	}

	@Override
	public boolean hasPurchased() {
		return false;
	}
}
