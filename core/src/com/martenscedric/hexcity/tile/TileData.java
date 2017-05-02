package com.martenscedric.hexcity.tile;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;
import com.cedricmartens.hexpert.Hexagon;
import com.cedricmartens.hexpert.coordinate.Point;

/**
 * Created by Cedric on 2017-04-21.
 */
public class TileData {
    private TileType tileType;
    private BuildingType buildingType;
    private PolygonSprite sprite;
    private Texture texture;
    private Hexagon<TileData> parent;

    public TileData(Hexagon<TileData> parent) {
        this.parent = parent;
    }

    public void setColor(int color) {
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(color);
        pix.fill();
        Texture tex = new Texture(pix);
        TextureRegion region = new TextureRegion(tex);

        Point p1 = (Point) parent.getHexGeometry().getPoints().toArray()[0];
        Point p2 = (Point) parent.getHexGeometry().getPoints().toArray()[1];
        Point p3 = (Point) parent.getHexGeometry().getPoints().toArray()[2];
        Point p4 = (Point) parent.getHexGeometry().getPoints().toArray()[3];
        Point p5 = (Point) parent.getHexGeometry().getPoints().toArray()[4];
        Point p6 = (Point) parent.getHexGeometry().getPoints().toArray()[5];

        float[] vertices = new float[]{(
                float) p1.getX(), (float)p1.getY(),
                (float)p2.getX(), (float)p2.getY(),
                (float)p3.getX(), (float)p3.getY(),
                (float) p4.getX(), (float)p4.getY(),
                (float)p5.getX(), (float)p5.getY(),
                (float)p6.getX(), (float)p6.getY()};
        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        ShortArray triangleIndices = triangulator.computeTriangles(vertices);
        PolygonRegion polygonRegion = new PolygonRegion(region, vertices, triangleIndices.toArray());
        sprite = new PolygonSprite(polygonRegion);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }



    public PolygonSprite getSprite() {
        return sprite;
    }


    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }
}