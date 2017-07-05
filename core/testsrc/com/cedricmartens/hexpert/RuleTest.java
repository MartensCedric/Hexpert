package com.cedricmartens.hexpert;


import com.cedricmartens.hexpert.tile.BuildingType;
import com.cedricmartens.hexpert.tile.Rules;

import org.junit.*;

import static com.cedricmartens.hexpert.tile.Rules.*;


/**
 * Created by martens on 7/5/17.
 */

public class RuleTest {

    @Test
    public void testRequired()
    {
        Assert.assertTrue(isRequired(BuildingType.FARM, BuildingType.HOUSE));
        Assert.assertFalse(isRequired(BuildingType.MARKET, BuildingType.FARM));
        Assert.assertTrue(isRequired(BuildingType.FACTORY, BuildingType.MARKET));
    }

    @Test
    public void testDenied()
    {
        Assert.assertTrue(isDenied(BuildingType.FARM, BuildingType.FARM));
        Assert.assertFalse(isDenied(BuildingType.ROCKET, BuildingType.MINE));
        Assert.assertFalse(isDenied(BuildingType.MINE, BuildingType.WIND));
    }
}
