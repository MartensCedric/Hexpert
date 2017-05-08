package com.martenscedric.hexcity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.martenscedric.hexcity.screens.LevelSelectScreen;
import com.martenscedric.hexcity.screens.MainMenuScreen;
import com.martenscedric.hexcity.tile.BuildingType;
import com.martenscedric.hexcity.tile.TileType;

import java.util.HashMap;

import static com.martenscedric.hexcity.misc.TextureData.*;

public class HexCity extends Game {

	public AssetManager assetManager = new AssetManager();
	public MainMenuScreen mainMenuScreen;
	public LevelSelectScreen levelSelectScreen;
	public HashMap<String, Sound> sounds = new HashMap<String, Sound>();


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
		assetManager.load(TEXTURE_GRASS, Texture.class);
		assetManager.load(TEXTURE_SAND, Texture.class);
		assetManager.load(TEXTURE_SNOW, Texture.class);
		assetManager.load(TEXTURE_WATER, Texture.class);
		assetManager.load(TEXTURE_BADMOVE, Texture.class);
		sounds.put("click", Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav")));
		sounds.put("bad", Gdx.audio.newSound(Gdx.files.internal("sounds/bad.wav")));
		sounds.put("select", Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav")));
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
