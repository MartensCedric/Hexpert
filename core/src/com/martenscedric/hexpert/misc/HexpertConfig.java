package com.martenscedric.hexpert.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import flexjson.JSONSerializer;

/**
 * Created by 1544256 on 2017-05-24.
 */

public class HexpertConfig
{
    private boolean buildHelp;
    private boolean keepSelection;
    private boolean showRequirements;

    public HexpertConfig()
    {
        buildHelp = true;
        keepSelection = false;
        showRequirements = true;
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
}
