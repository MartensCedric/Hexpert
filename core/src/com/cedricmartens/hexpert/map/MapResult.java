package com.cedricmartens.hexpert.map;

import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexpert.tile.BuildingType;
import com.cedricmartens.hexpert.tile.TileData;


/**
 * Created by 1544256 on 2017-05-09.
 */

public class MapResult
{
    private boolean[] objectivePassed;
    private int score;
    private BuildingType[] buildings;

    public MapResult() {
    }

    public MapResult(HexMap<TileData> grid, Objective[] objectives) {
        setBuildingFromGrid(grid);
        objectivePassed = new boolean[objectives.length];
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public void updateObjectives(Objective[] objectives, HexMap<TileData> grid)
    {
        objectivePassed = new boolean[objectives.length];

        for(int i = 0; i < objectivePassed.length; i++)
        {
            objectivePassed[i] = objectives[i].hasPassed(grid);
        }
    }

    public void setObjectivePassed(boolean[] objectivePassed) {
        this.objectivePassed = objectivePassed;
    }

    public boolean[] getObjectivePassed()
    {
        return objectivePassed;
    }

    public int getObjectivePassedCount()
    {
        boolean[] objectiveStatus = getObjectivePassed();

        int n = 0;

        for(int i = 0; i < objectiveStatus.length; i++)
        {
            if(objectiveStatus[i])
                n++;
        }
        return n;
    }

    public BuildingType[] getBuildings() {
        return buildings;
    }

    public void setBuildings(BuildingType[] buildings)
    {
        this.buildings = buildings;
    }

    public void setBuildingFromGrid(HexMap<TileData> grid)
    {
        buildings = new BuildingType[grid.getHexs().length];
        for(int i = 0; i < grid.getHexs().length; i++)
        {
            buildings[i] = ((TileData)grid.getHexs()[i].getHexData()).getBuildingType();
        }
    }
}
