package com.martenscedric.hexpert.social;

/**
 * Created by martens on 5/18/17.
 */

public interface PlayServices
{
    void signIn();
    void signOut();
    void rateGame();
    void unlockAchievement(String id);
    void showAchievementsUI();
    boolean isSignedIn();
}
