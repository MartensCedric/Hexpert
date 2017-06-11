package com.cedricmartens.hexpert;

import com.cedricmartens.hexpert.map.Objective;

import org.junit.Test;

/**
 * Created by 1544256 on 2017-05-10.
 */

public class ObjectiveTest
{
    @Test (timeout = 1000)
    public void testToString()
    {
        for(int i = 0; i < 3; i++)
        {
            Objective objective = new Objective(new int[]{0, 0, 0, 1, 0, 1, 0, 0}, Integer.MIN_VALUE);
            System.out.println(objective);

            Objective objective2 = new Objective(new int[]{0, 0, 2, 0, 0, 0, 1, 2}, 3);
            System.out.println(objective2);

            Objective objective3 = new Objective(new int[]{0, 1, 3, 0, 1, 5, 0, 0}, 5);
            System.out.println(objective3);
        }
    }
}
