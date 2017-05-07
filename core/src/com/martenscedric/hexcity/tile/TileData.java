package com.martenscedric.hexcity.tile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.cedricmartens.hexmap.hexagon.Hexagon;

/**
 * Created by Cedric on 2017-04-21.
 */
public class TileData {
    private TileType tileType;
    private BuildingType buildingType;
    private Texture terrainTexture;
    private Texture buildingTexture;
    private Hexagon<TileData> parent;

    public TileData(Hexagon<TileData> parent) {
        this.parent = parent;
    }

//    public void setColor(int color) {
//        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
//        pix.setColor(color);
//        pix.fill();
//        Texture tex = new Texture(pix);
//        TextureRegion region = new TextureRegion(tex);
//
//        Point p1 = (Point) parent.getHexGeometry().getPoints().toArray()[0];
//        Point p2 = (Point) parent.getHexGeometry().getPoints().toArray()[1];
//        Point p3 = (Point) parent.getHexGeometry().getPoints().toArray()[2];
//        Point p4 = (Point) parent.getHexGeometry().getPoints().toArray()[3];
//        Point p5 = (Point) parent.getHexGeometry().getPoints().toArray()[4];
//        Point p6 = (Point) parent.getHexGeometry().getPoints().toArray()[5];
//
//        float[] vertices = new float[]{(
//                float) p1.x, (float)p1.y,
//                (float)p2.x, (float)p2.y,
//                (float)p3.x, (float)p3.y,
//                (float) p4.x, (float)p4.y,
//                (float)p5.x, (float)p5.y,
//                (float)p6.x, (float)p6.y};
//        EarClippingTriangulator triangulator = new EarClippingTriangulator();
//        ShortArray triangleIndices = triangulator.computeTriangles(vertices);
//        PolygonRegion polygonRegion = new PolygonRegion(region, vertices, triangleIndices.toArray());
//        sprite = new PolygonSprite(polygonRegion);
//    }

    public void setBuildingTexture(Texture buildingTexture) {
        this.buildingTexture = buildingTexture;
    }

    public Texture getBuildingTexture() {
        return buildingTexture;
    }

    public Texture getTerrainTexture() {
        return terrainTexture;
    }

    public void setTerrainTexture(Texture terrainTexture) {
        this.terrainTexture = terrainTexture;
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

    public Hexagon<TileData> getParent() {
        return parent;
    }
}