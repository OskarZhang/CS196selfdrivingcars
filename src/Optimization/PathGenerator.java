package Optimization;


import Constants.Directions;
import Car.Car;
import Map.Road.TrafficLight;

import java.util.ArrayList;


public class PathGenerator {
   
   //determines baseline path
   public ArrayList<Integer> baseline(Car car)
   {
      ArrayList<Integer> path = new ArrayList<Integer>();
      int[] position = new int[2];
      position[0] = car.getXPos();
      position[1] = car.getYPos();
      int distX =  car.getDestX() - position[0];
      int distY =  car.getDestY() - position[1];
      boolean cont = true;
      int dir = car.getDir();
      if (dir == Directions.LEFT || dir == Directions.RIGHT)
      {
         PathGenerator.travelHorizontally(path, car, position);
         dir = path.get(path.size()-1);
         if (finalXDest(position[0], car.getDestX()) && finalYDest(position[1], car.getDestY()))
            return path;
         if ((distY>0 && dir == Directions.LEFT) || (distY < 0 && dir == Directions.RIGHT))
             turnLeft(path, position);
         else if ((distY>0 && dir == Directions.LEFT) || (distY < 0 && dir == Directions.RIGHT))
            turnRight(path, position);
      }
      PathGenerator.travelVertically(path, car, position);
      if (finalXDest(position[0], car.getDestX()) && finalYDest(position[1], car.getDestY()))
         return path;
      if (distX > 0 && dir == Directions.UP || distX<0 && dir == Directions.DOWN)
         turnRight(path, position);
      else if (distX < 0 && dir == Directions.UP || distX > 0 && dir == Directions.DOWN)
         turnLeft(path, position);
      
      PathGenerator.travelHorizontally(path, car, position);
      if (finalXDest(position[0], car.getDestX()) && finalYDest(position[1], car.getDestY()))
         return path;
      if ((distY>0 && dir == Directions.LEFT) || (distY < 0 && dir == Directions.RIGHT))
         turnLeft(path, position);
      else if ((distY>0 && dir == Directions.LEFT) || (distY < 0 && dir == Directions.RIGHT))
         turnRight(path, position);
      
      PathGenerator.travelVertically(path, car, position);
         return path;
   }
   
   public static void travelHorizontally(ArrayList<Integer> path, Car car, int[] position)
   {
      int dir = path.get(path.size()-1);
      int distX =  car.getDestX() - position[0];
      short inverter = -1;
      if (dir == Directions.RIGHT)
         inverter = 1;
      boolean cont = true;
      for (int i = 0; i < distX;i++)
      {
         if (car.getNextTile(position[0], position[1], dir) instanceof TrafficLight)
         {
            cont = false;
            for (int j = 0; i+j> Math.abs(distX) && !cont ; j++)
            {
               if ((car.getNextTile(position[0]+inverter*j, position[1], dir) instanceof TrafficLight) || (finalXDest(position[0] + inverter*j, car.getDestX()) && finalYDest(position[1], car.getDestY())))
                  cont = true;
            }
         }
         if (cont)
         {
            path.add(dir);
            position[0]+= inverter;
         }
      }
      
   }
 
   public static void travelVertically(ArrayList<Integer> path, Car car, int[] position)
   {
      int dir = path.get(path.size()-1);
      int distY = car.getDestY() - position[1];
      short inverter = -1;
      if (dir == Directions.UP)
         inverter = 1;
      boolean cont = true;
      for (int i = 0; i < distY;i++)
      {
         if (car.getNextTile(position[0], position[1], dir) instanceof TrafficLight)
         {
            cont = false;
            for (int j = 0; i+j> Math.abs(distY) && !cont ; j++)
            {
               if ((car.getNextTile(position[0], position[1] + inverter*j, dir) instanceof TrafficLight) || (finalYDest(position[1] + inverter*j, car.getDestY()) && finalXDest(position[0], car.getDestX())))
                  cont = true;
            }
         }
         if (cont)
         {
            path.add(dir);
            position[1]+= inverter;
         }
      }
      
   }
   public static Boolean finalXDest(int xPos, int destX)
   {
      if (xPos == destX || xPos + 1 == destX || xPos - 1 == destX)
         return true;
      return false;
   }
   public static Boolean finalYDest(int yPos, int destY)
   {
      if (yPos == destY || yPos + 1 == destY || yPos - 1 == destY)
         return true;
      return false;
   }
   public static void turnRight(ArrayList<Integer> path, int[] position) {
      int currentDir = path.get(path.size() - 1);
      if(currentDir == 0) {
         position[0] = position[0] + 1;
         position[1] = position[1] - 1;
         path.add(0);
         path.add(1);
      }
      else if(currentDir == 1) {
         position[0] = position[0] + 1;
         position[1] = position[1] + 1;
         path.add(1);
         path.add(2);
      }
      else if(currentDir == 2) {
         position[0] = position[0] - 1;
         position[1] = position[1] + 1;
         path.add(2);
         path.add(3);
      }
      else {
         position[0] = position[0] - 1;
         position[1] = position[1] - 1;
         path.add(3);
         path.add(0);
      }
   }
   
   public static void turnLeft(ArrayList<Integer> path, int[] position) {
      int currentDir = path.get(path.size() - 1);
      if(currentDir == 0) {
         position[0] = position[0] - 2;
         position[1] = position[1] - 2;
         path.add(0);
         path.add(0);
         path.add(3);
         path.add(3);
      }
      else if(currentDir == 1) {
         position[0] = position[0] + 2;
         position[1] = position[1] - 2;
         path.add(1);
         path.add(1);
         path.add(0);
         path.add(0);
      }
      else if(currentDir == 2) {
         position[0] = position[0] + 2;
         position[1] = position[1] + 2;
         path.add(2);
         path.add(2);
         path.add(1);
         path.add(1);
      }
      else{
         position[0] = position[0] - 2;
         position[1] = position[1] + 2;
         path.add(3);
         path.add(3);
         path.add(2);
         path.add(2);
      }
   }
}