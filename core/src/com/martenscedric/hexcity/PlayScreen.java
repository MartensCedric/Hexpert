package com.martenscedric.hexcity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cedricmartens.hexpert.HexGeometry;
import com.cedricmartens.hexpert.HexStyle;
import com.cedricmartens.hexpert.Hexagon;
import com.cedricmartens.hexpert.coordinate.Point;
import com.cedricmartens.hexpert.grid.HexGrid;
import com.cedricmartens.hexpert.grid.HexGridBuilder;
import com.cedricmartens.hexpert.grid.HexagonOrientation;
import com.cedricmartens.hexpert.grid.HexagonShape;

/**
 * Created by 1544256 on 2017-04-26.
 */

public class PlayScreen  extends StageScreen
{
    private final HexCity hexCity;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private HexGrid<Integer> hex;

    public PlayScreen(HexCity hexCity) {
        this.hexCity = hexCity;
        this.batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        hex = new HexGridBuilder<Integer>()
                .setHeight(7)
                .setWidth(7)
                .setShape(HexagonShape.HEXAGON)
                .setStyle(new HexStyle(25, HexagonOrientation.FLAT_TOP))
                .setOrigin(new Point(200, 200))
                .build();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(66f/255f, 206f/255f, 244f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin();
        Hexagon<Integer>[] hexagons = hex.getHexs();
        for(int i = 0; i < hexagons.length; i++)
        {
            HexGeometry hexGeo = hexagons[i].getHexGeometry();
            Point p0 = (Point) hexGeo.getPoints().toArray()[0];
            Point pLast = (Point) hexGeo.getPoints().toArray()[hexGeo.getPoints().size() - 1];
            shapeRenderer.line((float)p0.getX(), (float)p0.getY(),
                    (float)pLast.getX(), (float)pLast.getY(),
                    Color.BLACK, Color.BLACK);
            for(int j = 1; j < hexGeo.getPoints().size(); j++)
            {
                Point current = (Point)hexGeo.getPoints().toArray()[j];
                Point precedent = (Point)hexGeo.getPoints().toArray()[j - 1];
                shapeRenderer.line((float)current.getX(), (float)current.getY(),
                        (float)precedent.getX(), (float)precedent.getY(),
                        Color.BLACK, Color.BLACK);
            }
        }

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
