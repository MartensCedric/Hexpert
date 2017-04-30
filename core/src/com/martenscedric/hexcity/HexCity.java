package com.martenscedric.hexcity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.File;

import static com.martenscedric.hexcity.TextureData.TEXTURE_BANK;
import static com.martenscedric.hexcity.TextureData.TEXTURE_CLOUD;
import static com.martenscedric.hexcity.TextureData.TEXTURE_FACTORY;
import static com.martenscedric.hexcity.TextureData.TEXTURE_FARM;
import static com.martenscedric.hexcity.TextureData.TEXTURE_HOUSE;
import static com.martenscedric.hexcity.TextureData.TEXTURE_MARKET;
import static com.martenscedric.hexcity.TextureData.TEXTURE_MENUUI;
import static com.martenscedric.hexcity.TextureData.TEXTURE_MINE;
import static com.martenscedric.hexcity.TextureData.TEXTURE_ROCKET;
import static com.martenscedric.hexcity.TextureData.TEXTURE_WIND;

public class HexCity extends Game {

	public AssetManager assetManager = new AssetManager();
	private MainMenuScreen mainMenuScreen;
	@Override
	public void create ()
	{
		assetManager.load(TEXTURE_CLOUD, Texture.class);
		assetManager.load(TEXTURE_FARM, Texture.class);
		assetManager.load(TEXTURE_HOUSE, Texture.class);
		assetManager.load(TEXTURE_MINE, Texture.class);
		assetManager.load(TEXTURE_WIND, Texture.class);
		assetManager.load(TEXTURE_FACTORY, Texture.class);
		assetManager.load(TEXTURE_MARKET, Texture.class);
		assetManager.load(TEXTURE_BANK, Texture.class);
		assetManager.load(TEXTURE_ROCKET, Texture.class);
		assetManager.load(TEXTURE_MENUUI, Texture.class);
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
		super.resize(width, height);
	}
}
