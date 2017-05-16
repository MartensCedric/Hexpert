package com.martenscedric.hexpert;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;
import com.martenscedric.hexpert.screens.LevelSelectScreen;
import com.martenscedric.hexpert.screens.MainMenuScreen;
import com.martenscedric.hexpert.tile.BuildingType;
import com.martenscedric.hexpert.tile.TileType;

import java.util.HashMap;

import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BACK;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BADMOVE;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BANK;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_CLOUD;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_FACTORY;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_FARM;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_GRASS;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HELP;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HOUSE;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_MARKET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_MENUUI;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_MINE;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_RESET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_ROCKET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_SAND;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_SNOW;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_UNDO;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_WATER;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_WIND;

public class Hexpert extends Game {

	public AssetManager assetManager = new AssetManager();
	public MainMenuScreen mainMenuScreen;
	public LevelSelectScreen levelSelectScreen;
	public HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	public I18NBundle i18NBundle;

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
		assetManager.load(TEXTURE_UNDO, Texture.class);
		assetManager.load(TEXTURE_RESET, Texture.class);
		assetManager.load(TEXTURE_BACK, Texture.class);
		assetManager.load(TEXTURE_HELP, Texture.class);

		assetManager.load(TEXTURE_GRASS, Texture.class);
		assetManager.load(TEXTURE_SAND, Texture.class);
		assetManager.load(TEXTURE_SNOW, Texture.class);
		assetManager.load(TEXTURE_WATER, Texture.class);

		assetManager.load(TEXTURE_BADMOVE, Texture.class);
		assetManager.load("i18n/language", I18NBundle.class);

		assetManager.load("sprites/nextlevelleft.png", Texture.class);
		assetManager.load("sprites/nextlevelright.png", Texture.class);

		sounds.put("click", Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav")));
		sounds.put("bad", Gdx.audio.newSound(Gdx.files.internal("sounds/bad.wav")));
		sounds.put("select", Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav")));
		assetManager.finishLoading();
		i18NBundle = assetManager.get("i18n/language", I18NBundle.class);
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
				texture = assetManager.get(TEXTURE_HOUSE, Texture.class);
				break;
			case WIND:
				texture = assetManager.get(TEXTURE_WIND, Texture.class);
				break;
			case FARM:
				texture = assetManager.get(TEXTURE_FARM, Texture.class);
				break;
			case MINE:
				texture = assetManager.get(TEXTURE_MINE, Texture.class);
				break;
			case FACTORY:
				texture = assetManager.get(TEXTURE_FACTORY, Texture.class);
				break;
			case MARKET:
				texture = assetManager.get(TEXTURE_MARKET, Texture.class);
				break;
			case BANK:
				texture = assetManager.get(TEXTURE_BANK, Texture.class);
				break;
			case ROCKET:
				texture = assetManager.get(TEXTURE_ROCKET, Texture.class);
				break;
		}

		return texture;
	}

	public Texture getTextureByTerrain(TileType tileType)
	{
		Texture texture = null;
		switch (tileType) {
			case GRASS:
				texture = assetManager.get(TEXTURE_GRASS, Texture.class);
				break;
			case WATER:
				texture = assetManager.get(TEXTURE_WATER, Texture.class);
				break;
			case SAND:
				texture = assetManager.get(TEXTURE_SAND, Texture.class);
				break;
			case SNOW:
				texture = assetManager.get(TEXTURE_SNOW, Texture.class);
				break;
		}

		return texture;
	}
}
