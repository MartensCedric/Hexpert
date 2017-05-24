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

    public HexpertConfig()
    {
        buildHelp = false;
        keepSelection = false;
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

    public void save()
    {
        JSONSerializer jsonSerializer = new JSONSerializer();
        Gdx.files.local("options.config").writeString(jsonSerializer.deepSerialize(this), false);
    }
}
