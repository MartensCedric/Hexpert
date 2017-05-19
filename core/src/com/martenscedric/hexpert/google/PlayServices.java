package com.martenscedric.hexpert.google;

/**
 * Created by martens on 5/18/17.
 */

public interface PlayServices
{
    public void signIn();
    public void signOut();
    public void rateGame();
    public void unlockAchievement(String id);
    public boolean isSignedIn();
}
