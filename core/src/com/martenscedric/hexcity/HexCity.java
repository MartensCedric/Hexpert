package com.martenscedric.hexcity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.martenscedric.hexcity.screens.LevelSelectScreen;
import com.martenscedric.hexcity.screens.MainMenuScreen;
import com.martenscedric.hexcity.tile.BuildingType;

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
	public MainMenuScreen mainMenuScreen;
	public LevelSelectScreen levelSelectScreen;


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
		levelSelectScreen = new LevelSelectScreen(this);
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


	public Texture getTextureByBuilding(BuildingType buildingType)
	{
		Texture texture = null;

		switch (buildingType)
		{
			case HOUSE:
				texture = assetManager.get("sprites/house.png", Texture.class);
				break;
			case WIND:
				texture = assetManager.get("sprites/wind.png", Texture.class);
				break;
			case FARM:
				texture = assetManager.get("sprites/farm.png", Texture.class);
				break;
			case MINE:
				texture = assetManager.get("sprites/mine.png", Texture.class);
				break;
			case FACTORY:
				texture = assetManager.get("sprites/factory.png", Texture.class);
				break;
			case MARKET:
				texture = assetManager.get("sprites/market.png", Texture.class);
				break;
			case BANK:
				texture = assetManager.get("sprites/bank.png", Texture.class);
				break;
			case ROCKET:
				texture = assetManager.get("sprites/rocket.png", Texture.class);
				break;
		}

		return texture;
	}
}
