package com.cedricmartens.hexpert.tile;

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

    public TileData(Hexagon<TileData> parent) {
        this.parent = parent;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TileData tileData = (TileData) o;

        if (tileType != tileData.tileType) return false;
        if (buildingType != tileData.buildingType) return false;
        if (terrainTexture != null ? !terrainTexture.equals(tileData.terrainTexture) : tileData.terrainTexture != null)
            return false;
        if (buildingTexture != null ? !buildingTexture.equals(tileData.buildingTexture) : tileData.buildingTexture != null)
            return false;
        return parent != null ? parent.equals(tileData.parent) : tileData.parent == null;

    }

    @Override
    public int hashCode() {
        int result = tileType != null ? tileType.hashCode() : 0;
        result = 31 * result + (buildingType != null ? buildingType.hashCode() : 0);
        result = 31 * result + (terrainTexture != null ? terrainTexture.hashCode() : 0);
        result = 31 * result + (buildingTexture != null ? buildingTexture.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }
}