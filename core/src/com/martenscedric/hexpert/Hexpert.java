package com.martenscedric.hexpert;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.martenscedric.hexpert.google.PlayServices;
import com.martenscedric.hexpert.misc.HexpertConfig;
import com.martenscedric.hexpert.screens.LevelSelectScreen;
import com.martenscedric.hexpert.screens.MainMenuScreen;
import com.martenscedric.hexpert.tile.BuildingType;
import com.martenscedric.hexpert.tile.TileType;

import java.util.HashMap;

import flexjson.JSONDeserializer;

import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BACK;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BAD;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BANK;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_CLOUD;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_CORRECT;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_FACTORY;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_FARM;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_GRASS;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HELP;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HEXBRONZE;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HEXGOLD;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HEXSILVER;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HOUSE;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_MARKET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_MENUUI;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_MINE;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_RESET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_ROCKET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_SAND;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_FOREST;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_UNDO;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_WATER;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_WIND;

public class Hexpert extends Game {

	public AssetManager assetManager = new AssetManager();
	public MainMenuScreen mainMenuScreen;
	public LevelSelectScreen levelSelectScreen;
	public HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	public I18NBundle i18NBundle;
	public PlayServices playServices;
	public HexpertConfig config;
    private BitmapFont font;
	private Skin skin;

	public Hexpert(PlayServices playServices) {
		this.playServices = playServices;
	}

	@Override
	public void create ()
	{
		FileHandle options = Gdx.files.local("options.config");
		if(options.exists())
		{
			config = new JSONDeserializer<HexpertConfig>().deserialize("options.config");
		}else
		{
			config = new HexpertConfig();
			config.save();
		}

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
		assetManager.load(TEXTURE_FOREST, Texture.class);
		assetManager.load(TEXTURE_WATER, Texture.class);

		assetManager.load(TEXTURE_BAD, Texture.class);
		assetManager.load(TEXTURE_CORRECT, Texture.class);

		assetManager.load(TEXTURE_HEXGOLD, Texture.class);
		assetManager.load(TEXTURE_HEXSILVER, Texture.class);
		assetManager.load(TEXTURE_HEXBRONZE, Texture.class);

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
				texture = assetManager.get(TEXTURE_FOREST, Texture.class);
				break;
		}

		return texture;
	}



    public BitmapFont getFont() {
        if (font == null) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/VCROSDMono.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 35;
            parameter.borderWidth = 1;
            parameter.color = Color.BLACK;
            font = generator.generateFont(parameter);
            generator.dispose();
        }
        return font;
    }

	public Skin getSkin()
	{
		if(skin == null)
		{
			skin = new Skin(Gdx.files.internal("skins/hexpert/hexpert.json"));
		}

		return skin;
	}
}
