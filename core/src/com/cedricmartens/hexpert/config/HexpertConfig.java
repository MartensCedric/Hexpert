package com.cedricmartens.hexpert.config;

/**
 * Created by 1544256 on 2017-05-24.
 */

public class HexpertConfig
{
    private boolean buildHelp;
    private boolean keepSelection;
    private boolean showRequirements;
    private boolean hasMetPurchaseScreen;
    private float volume;

    public HexpertConfig()
    {
        buildHelp = true;
        hasMetPurchaseScreen = false;
        keepSelection = false;
        showRequirements = true;
        volume = 1;
    }

    public boolean isShowRequirements() {
        return showRequirements;
    }

    public void setShowRequirements(boolean showRequirements) {
        this.showRequirements = showRequirements;
    }

    public boolean isBuildHelp() {
        return buildHelp;
    }

    public void setBuildHelp(boolean buildHelp) {
        this.buildHelp = buildHelp;
    }

    public boolean isKeepSelection() {
        return keepSelection;
    }

    public void setKeepSelection(boolean keepSelection) {
        this.keepSelection = keepSelection;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public boolean isHasMetPurchaseScreen() {
        return hasMetPurchaseScreen;
    }

    public void setHasMetPurchaseScreen(boolean hasMetPurchaseScreen) {
        this.hasMetPurchaseScreen = hasMetPurchaseScreen;
    }
}
