package com.martenscedric.hexcity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.File;

import static com.martenscedric.hexcity.TextureData.TEXTURE_CLOUD;

public class HexCity extends Game {

	public AssetManager assetManager = new AssetManager();
	private MainMenuScreen mainMenuScreen;
	@Override
	public void create ()
	{
		assetManager.load(TEXTURE_CLOUD, Texture.class);
		assetManager.load("sprites/bank.png", Texture.class);
		assetManager.load("sprites/selectmenu.png", Texture.class);
		assetManager.finishLoading();
		mainMenuScreen = new MainMenuScreen(this);
		this.setScreen(mainMenuScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height)
	{
		TextureData.scale_ratio = (float)width/(float)height;
		super.resize(width, height);
	}
}
