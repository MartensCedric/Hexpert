package com.martenscedric.hexcity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.File;

public class HexCity extends ApplicationAdapter {
	private SpriteBatch batch;
	private SkyEffect skyEffect;
	
	@Override
	public void create () {
		AssetLoader.load();
		batch = new SpriteBatch();
		skyEffect = new SkyEffect(3);
	}

	private void tick()
	{
		skyEffect.tick();
	}

	@Override
	public void render () {
		tick();
		Gdx.gl.glClearColor(66f/255f, 206f/255f, 244f/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		Texture cloudTexture = AssetLoader.assetManager.get("sprites/cloud.png");
		for(Cloud c : skyEffect.clouds)
		{
			batch.draw(cloudTexture, c.getPosition().x, c.getPosition().y);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
