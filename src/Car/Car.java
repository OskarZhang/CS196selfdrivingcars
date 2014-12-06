package Car;


import java.util.ArrayList;

import Constants.Directions;
import Map.Map;
import Map.Tile;
import Map.Road.Road;
import Map.Road.TrafficLight;
import Optimization.PathGenerator;

public class Car implements java.io.Serializable {
    private int speed=0;
    private int dir=0;
    private int xPos=0;
    private int yPos=0;
    private int previousDir=0;
    //	final private Tile destination=new Tile();   waiting to be implemented
    ArrayList<Integer> path=new ArrayList<Integer>();
    Map map; //Grid that the car is in
    private int destX;
    private int destY;
    public boolean hasMoved=false;
    
    public Car(int x, int y, int destX, int destY, Map map) {
        xPos = x;
        yPos = y;
        this.destX = destX;
        this.destY = destY;
        Road road = (Road)map.get(xPos, yPos);
        road.carIncrement(this);
        this.dir = road.getDirection();
        this.map = map;
        path = PathGenerator.minimizeTime(map, this);
    }
    public int getXPos()
    {
       return xPos;
    }
    public int getYPos()
    {
       return yPos;
    }
    public int getDestX()
    {
       return destX;
    }
    public int getDestY()
    {
       return destY;
    }
    
    public boolean move() // plug off the first item of the arraylist and execute that
    {
        Tile nextTile=getNextTile(xPos, yPos, path.get(0));
        if(nextTile instanceof TrafficLight) { //No red traffic lights
            TrafficLight l = (TrafficLight)nextTile;
            if(l.isRed() && !(map.get(xPos,yPos) instanceof TrafficLight)) {
            	hasMoved=true;
                return false;
            }
        }
        Road road = (Road)map.get(xPos, yPos);
        int speedInit = road.getSpeed();
        for(int remaining = road.getSpeed(); remaining>=0; remaining--) {
        	road = (Road)map.get(xPos, yPos);
        	if(road.getSpeed()!=speedInit) break;
	        if(nextTile.getX()>=0 && nextTile.getY()>=0 && nextTile.getX()<map.getLengthX() && nextTile.getY()<map.getLengthY()) {
	        	if(((Road)(map.get(nextTile.getX(), nextTile.getY()))).getRemaining()>0) {
	        		hasMoved=true;
		            road.carDecrement(this);
		            xPos=nextTile.getX();
		            yPos=nextTile.getY();
		            road = (Road) map.get(xPos, yPos);
		            road.carIncrement(this);
		            path.remove(0);
		            return true;
	        	}
	        	
	        }
    	}
        return false;
    }
    public boolean turn()
    {
        dir++;
        return true;
    }
    public boolean exists()
    {
        return true;
    }
    /*
     * Given a direction, will find the tile (if it exists) in that direction and return it
     * @param Direction to check in
     * @return Tile in the given direction, or null if it doesn't exist.
     */
    public Tile getNextTile(int x, int y, int dir)
    {
        switch(dir) {
            case Directions.UP:
                y--;
                break;
            case Directions.DOWN:
                y++;
                break;
            case Directions.RIGHT:
                x++;
                break;
            case Directions.LEFT:
                x--;
                break;
        }
        if(map.pointIsValid(x, y)) //Ensure tile is in grid
            return map.get(x, y);
        else
            return null; //If not in grid, return null
    }
    public int getSpeed()
    {
        return speed;
    }
    public void setSpeed(int speed)
    {
        speed=this.speed;
    }
    public int getDir()
    {
        return dir;
    }
    public void seDir(int dir)
    {
        dir=this.dir;
    }

    private ArrayList<Integer> leftTurn(int dir) {
        ArrayList<Integer> dirs = new ArrayList<Integer>();
        int leftDir = dir-1;
        if(leftDir<0) leftDir+=4;
        dirs.add(dir);
        dirs.add(dir);
        dirs.add(leftDir);
        dirs.add(leftDir);
        return dirs;
    }
    private ArrayList<Integer> rightTurn(int dir) {
        ArrayList<Integer> dirs = new ArrayList<Integer>();
        int rightDir = dir+1;
        if(rightDir>3) rightDir-=4;
        dirs.add(dir);
        dirs.add(rightDir);
        return dirs;
    }
    public ArrayList<Integer> getPath(){ return path; }
    
    public String toString() {
        return (xPos+","+yPos);
    }
}
