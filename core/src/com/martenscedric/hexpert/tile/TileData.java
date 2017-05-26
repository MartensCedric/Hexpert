package com.martenscedric.hexpert.tile;

import com.badlogic.gdx.graphics.Texture;
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
    private boolean active;

    public TileData(Hexagon<TileData> parent) {
        this.parent = parent;
        setActive(true);
    }

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

    public void setParent(Hexagon<TileData> parent) {
        this.parent = parent;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}