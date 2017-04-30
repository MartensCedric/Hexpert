package com.martenscedric.hexcity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_BANK;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_CLOUD;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_FACTORY;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_FARM;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_HOUSE;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_MARKET;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_MENUUI;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_MINE;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_ROCKET;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_WIND;

public class HexCity extends Game {

	public AssetManager assetManager = new AssetManager();
	private com.martenscedric.hexcity.screens.MainMenuScreen mainMenuScreen;
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
		mainMenuScreen = new com.martenscedric.hexcity.screens.MainMenuScreen(this);
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
