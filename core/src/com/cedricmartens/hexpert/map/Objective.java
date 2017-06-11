package com.cedricmartens.hexpert.map;

import com.badlogic.gdx.utils.I18NBundle;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexpert.misc.Const;
import com.cedricmartens.hexpert.tile.TileData;

/**
 * Created by 1544256 on 2017-05-10.
 */
public class Objective
{
    private int minScore;
    private int[] buildingRequirement;

    public Objective() {
    }

    public Objective(int[] buildingRequirement, int minScore) {

        if(buildingRequirement.length != Const.BUILDING_COUNT)
            throw new IllegalArgumentException();

        this.buildingRequirement = buildingRequirement;
        this.minScore = minScore;
    }

    public int getMinScore() {
        return minScore;
    }

    public void setMinScore(Integer minScore) {
        this.minScore = minScore;
    }

    public int[] getBuildingRequirement() {
        return buildingRequirement;
    }

    public void setBuildingRequirement(int[] buildingRequirement) {
        this.buildingRequirement = buildingRequirement;
    }

    public boolean hasPassed(HexMap<TileData> grid)
    {
        int score = 0;
        int[] buildings = new int[Const.BUILDING_COUNT];
        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];

            if(hex.getHexData().getBuildingType() != com.cedricmartens.hexpert.tile.BuildingType.NONE)
            {
                score += hex.getHexData().getBuildingType().getScore() * hex.getHexData().getTileType().getMultiplier();
                buildings[hex.getHexData().getBuildingType().ordinal() - 1]++;
            }
        }

        for(int i = 0; i < Const.BUILDING_COUNT; i++)
        {
            if(buildings[i] < buildingRequirement[i])
                return false;
        }

        return score >= minScore;
    }

    public String toString(I18NBundle bundle) {

        String s = "";

        if(minScore != Integer.MIN_VALUE)
        {
            s+= bundle.format("objective_score", minScore);
        }

        for(int i = 0; i < Const.BUILDING_COUNT; i++)
        {
            if(buildingRequirement[i] > 0)
            {
                if(!s.equals(""))
                    s+="\n";

                s+= bundle.format("objective_building",
                        buildingRequirement[i],
                        bundle.format(com.cedricmartens.hexpert.tile.BuildingType.values()[i + 1].getName().toLowerCase() + "_choice", buildingRequirement[i]));
            }
        }

        return s;
    }
}
