package nl.saxion.dhi1vsq4;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class Message implements Serializable {

    private Point2D lowestRobotLocation;
    private int order;

    public Message(Point2D lowestRobotLocation){
        this.lowestRobotLocation = lowestRobotLocation;
    }

    public void setOrder(int order){
        this.order = order;
    }

    public int getOrder(){
        return order;
    }

    public Point2D getLocation(){
        return lowestRobotLocation;
    }
}
