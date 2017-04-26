package com.martenscedric.hexcity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.File;

public class HexCity extends Game {
	
	@Override
	public void create () {
		AssetLoader.load();
		this.setScreen(new MainMenuScreen());
	}

	@Override
	public void render() {
		super.render();
	}
}
