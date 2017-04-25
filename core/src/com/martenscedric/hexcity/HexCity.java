package com.martenscedric.hexcity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HexCity extends ApplicationAdapter {
	private SpriteBatch batch;
	private SkyEffect skyEffect;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		AssetLoader.load();
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
		skyEffect.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
