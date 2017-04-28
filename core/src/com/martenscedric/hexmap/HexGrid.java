package com.martenscedric.hexmap;

/**
 * Created by Cedric Martens on 2017-04-27.
 */

public class HexGrid
{
    private Point origin;
    private HexagonShape shape;
    private int width;
    private int height;
    private boolean built;

    public HexGrid()
    {
        origin = new Point(0, 0);
        width = 1;
        height = 1;
	built = false;
    }

    public HexGrid setWidth(int width)
    {
	this.width = width;
    }

    public HexGrid setHeight(int width)
    {
	this.height = height;
    }

    public HexGrid setOrigin(Point origin) 
    {
        this.origin = origin;
    }

    public HexGrid setShape(HexagonShape shape) 
    {
        this.shape = shape;
    }

    public HexGrid build()
    {
        switch(orientation)
	{
		case LINE :
			if((width < 1 || height < 1) 
			|| (width != 1 && height != 1))
			{

			}
		break;
	}
	return null;
    }
}
