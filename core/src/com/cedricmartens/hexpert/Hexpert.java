package com.cedricmartens.hexpert;

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
import com.cedricmartens.hexpert.config.HexpertConfig;
import com.cedricmartens.hexpert.screens.LevelSelectScreen;
import com.cedricmartens.hexpert.social.PlayServices;
import com.cedricmartens.hexpert.social.Purchasing;
import com.cedricmartens.hexpert.social.Sharing;
import com.cedricmartens.hexpert.tile.BuildingType;
import com.cedricmartens.hexpert.tile.TileType;

import java.util.HashMap;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_ACHIEVEMENTS;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_BACK;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_BAD;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_BANK;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_BANK_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_CLOUD;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_CORRECT;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FACTORY;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FACTORY_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FARM;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FARM_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FOREST;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FOREST_CUT;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_GRASS;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_HELP;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_HEXBRONZE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_HEXGOLD;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_HEXSILVER;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_HOUSE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_HOUSE_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_LEADERBOARD;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_LEFT;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_LOCKED;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MARKET;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MARKET_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MINE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MINE_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MORE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_NOT_FARM;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_OPTIONS;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_REMOVE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_RESET;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_RIGHT;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_ROCKET;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_ROCKET_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_SAND;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_WATER;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_WIND;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_WIND2;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_WIND3;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_WIND4;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_WIND_MIN;

public class Hexpert extends Game {

	public AssetManager assetManager = new AssetManager();
	public LevelSelectScreen levelSelectScreen;
	public HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	public I18NBundle i18NBundle;
	public PlayServices playServices;
	public Sharing sharing;
	public Purchasing purchasing;
	public HexpertConfig config;
	public float masterVolume = 1;
	public HashMap<Integer, String> levelIndex;
	private BitmapFont font;
	private Skin skin;

	public Hexpert(PlayServices playServices, Sharing sharing, Purchasing purchasing) {
		this.playServices = playServices;
		this.sharing = sharing;
		this.purchasing = purchasing;
	}

	@Override
	public void create ()
	{
		boolean shouldCreateOptions = false;
		FileHandle options = Gdx.files.local("options.config");
		if(options.exists())
		{
			try {
				String content = options.readString();

				config = new JSONDeserializer<HexpertConfig>().deserialize(content);
			}catch (Exception e)
			{
				e.printStackTrace();
				shouldCreateOptions = true;
			}
		}else
		{
			shouldCreateOptions = true;
		}

		if(shouldCreateOptions)
		{
			config = new HexpertConfig();
			JSONSerializer jsonSerializer = new JSONSerializer();
			Gdx.files.local("options.config").writeString(jsonSerializer.serialize(config), false);
		}

		masterVolume = config.getVolume();
		levelIndex = new HashMap<>();

		String[] lvlIndexes = Gdx.files.internal("maps.hexindex").readString().split("\n");

		for(int i = 0; i < lvlIndexes.length; i++)
		{
			levelIndex.put(i + 1, lvlIndexes[i]);
		}

		assetManager.load(TEXTURE_CLOUD, Texture.class);

		assetManager.load(TEXTURE_FARM, Texture.class);
		assetManager.load(TEXTURE_HOUSE, Texture.class);
		assetManager.load(TEXTURE_MINE, Texture.class);
		assetManager.load(TEXTURE_WIND, Texture.class);
		assetManager.load(TEXTURE_WIND2, Texture.class);
		assetManager.load(TEXTURE_WIND3, Texture.class);
		assetManager.load(TEXTURE_WIND4, Texture.class);
		assetManager.load(TEXTURE_FACTORY, Texture.class);
		assetManager.load(TEXTURE_MARKET, Texture.class);
		assetManager.load(TEXTURE_BANK, Texture.class);
		assetManager.load(TEXTURE_ROCKET, Texture.class);

		assetManager.load(TEXTURE_FARM_MIN, Texture.class);
		assetManager.load(TEXTURE_HOUSE_MIN, Texture.class);
		assetManager.load(TEXTURE_MINE_MIN, Texture.class);
		assetManager.load(TEXTURE_WIND_MIN, Texture.class);
		assetManager.load(TEXTURE_FACTORY_MIN, Texture.class);
		assetManager.load(TEXTURE_MARKET_MIN, Texture.class);
		assetManager.load(TEXTURE_BANK_MIN, Texture.class);
		assetManager.load(TEXTURE_ROCKET_MIN, Texture.class);

		assetManager.load(TEXTURE_NOT_FARM, Texture.class);

		assetManager.load(TEXTURE_RESET, Texture.class);
		assetManager.load(TEXTURE_BACK, Texture.class);
		assetManager.load(TEXTURE_HELP, Texture.class);
		assetManager.load(TEXTURE_OPTIONS, Texture.class);
		assetManager.load(TEXTURE_REMOVE, Texture.class);
		assetManager.load(TEXTURE_ACHIEVEMENTS, Texture.class);
		assetManager.load(TEXTURE_LEADERBOARD, Texture.class);
		assetManager.load(TEXTURE_MORE, Texture.class);

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

		assetManager.load(TEXTURE_LEFT, Texture.class);
		assetManager.load(TEXTURE_RIGHT, Texture.class);
		assetManager.load(TEXTURE_LOCKED, Texture.class);

		assetManager.load(TEXTURE_FOREST_CUT, Texture.class);

		sounds.put("click", Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav")));
		sounds.put("win", Gdx.audio.newSound(Gdx.files.internal("sounds/win.wav")));
		sounds.put("bad", Gdx.audio.newSound(Gdx.files.internal("sounds/bad.wav")));
		sounds.put("select", Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav")));

		assetManager.finishLoading();
		i18NBundle = assetManager.get("i18n/language", I18NBundle.class);
		levelSelectScreen = new LevelSelectScreen(this);
		this.setScreen(levelSelectScreen);
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
			case FOREST:
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