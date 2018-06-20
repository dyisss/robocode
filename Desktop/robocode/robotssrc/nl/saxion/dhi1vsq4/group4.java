package nl.saxion.dhi1vsq4;

import robocode.*;

import java.awt.geom.Point2D;
import java.io.IOException;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class group4 extends TeamRobot {
    int previousEnergy = 100;
    private double lowest = 500;
    private double lowestX;
    private double lowestY;
    private Point2D lowestRobotLocation;
    private RobotStatus robotStatus;

    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);

        String[] teammates = getTeammates();
        for (int i = 0; i < teammates.length; i++)
            setAdjustGunForRobotTurn(true);
        while (true) {
            scan();
            pow(lowestX, lowestY);
            setAhead(300);
            setTurnLeft(180);

            ahead(200);

            setAhead(300);
            setTurnRight(180);

            while (getTurnRemaining() > 0 && getDistanceRemaining() > 0) {
                execute();
            }
        }
    }

    public void bulletShielding(ScannedRobotEvent e) {
        double changeInEnergy = previousEnergy - e.getEnergy();
        if (changeInEnergy > 0 && changeInEnergy <= 3) {
            setTurnGunRight(getHeading() - getGunHeading() + e.getBearing());
            setFire(0.1);
        }
    }

    public void scan() {
        turnRadarRight(360);
        try {
            sendLocation(lowestRobotLocation);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void sendLocation(Point2D location) {
        Message dm = new Message(location);
        try {
            broadcastMessage(dm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onHitWall(HitWallEvent event) {
        turnRight(90);
        setBack(300);
        execute();
    }

    public void pow(double x, double y) {
        double xo = x - getX();
        double yo = y - getY();
        double hyp = Point2D.distance(getX(), getY(), x, y);
        double arcSin = Math.toDegrees(Math.asin(xo / hyp));
        double bearing = 0;

        System.out.println("arcsin " + arcSin);

        if (xo > 0 && yo > 0) { // both pos: lower-Left
            bearing = arcSin;
        } else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right
            bearing = 360 + arcSin; // arcsin is negative here, actuall 360 - ang
        } else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left
            bearing = 180 - arcSin;
        } else if (xo < 0 && yo < 0) { // both neg: upper-right
            bearing = 180 - arcSin; // arcsin is negative here, actually 180 + ang
        }

        System.out.println("absolute bearing " + bearing);
        System.out.println("our gun heading " + getGunHeading());

        setTurnGunRight(bearing - getGunHeading());
    }

    public void onStatus(StatusEvent e) {
        this.robotStatus = e.getStatus();
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        bulletShielding(e);
        if (e.getEnergy() <= lowest) {
            this.lowest = e.getEnergy();

            double angleToEnemy = e.getBearing();

            // Calculate the angle to the scanned robot
            double angle = Math.toRadians((robotStatus.getHeading() + angleToEnemy % 360));

            // Calculate the coordinates of the robot
            this.lowestX = (robotStatus.getX() + Math.sin(angle) * e.getDistance());
            this.lowestY = (robotStatus.getY() + Math.cos(angle) * e.getDistance());

            lowestRobotLocation = new Point2D.Double(lowestX, lowestY);
            System.out.println(lowestRobotLocation);

        }
        if(isTeammate(e.getName())){
            return ;}
        else {
        setFire(Math.min(400 / e.getDistance(), 3));
        }
    }



}
