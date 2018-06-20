package nl.saxion.dhi1vsq4;

import robocode.*;

import java.awt.geom.Point2D;
import java.io.IOException;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Leader extends TeamRobot {
    int previousEnergy = 100;
    private double lowest = 500;
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

        }
        if(isTeammate(e.getName())){
         System.out.println("isTeam");}
        else {
        setFire(Math.min(400 / e.getDistance(), 3));
        }
    }



}
