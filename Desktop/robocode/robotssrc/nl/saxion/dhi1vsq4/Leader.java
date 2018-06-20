package nl.saxion.dhi1vsq4;

import robocode.*;
import robocode.util.Utils;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

public class Leader extends TeamRobot {
    int previousEnergy = 100;
    private double lowest = 500;
    private Point2D lowestRobotLocation;
    private RobotStatus robotStatus;

    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);

        if(getEnergy()>199.0){ //220 if leader is a droid, 200 otherwise.
            String[] teammates = getTeammates();
            Vector<String> v = new Vector<String>();
            v.add(getName());
            v.addAll(Arrays.asList(teammates));

            teammates = (String[]) v.toArray();
            try{
                TeamMessage teamMessage = new TeamMessage(teammates);
                MessageEvent TeammateEvent = new MessageEvent("Leader",teamMessage);
                broadcastMessage(TeammateEvent);
            }catch(Exception ignored){}
        }
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

//    public void bulletShielding(ScannedRobotEvent e) {
//        double changeInEnergy = previousEnergy - e.getEnergy();
//        if (changeInEnergy > 0 && changeInEnergy <= 3) {
//            setTurnGunRight(getHeading() - getGunHeading() + e.getBearing());
//            setFire(0.1);
//        }
//    }

    public void scan() {
        turnRadarRight(Double.POSITIVE_INFINITY);
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
//        bulletShielding(e);
//        if (e.getEnergy() <= lowest) {
//            this.lowest = e.getEnergy();
//
//            double angleToEnemy = e.getBearing();
//
//            // Calculate the angle to the scanned robot
//            double angle = Math.toRadians((robotStatus.getHeading() + angleToEnemy % 360));
//
//        }
        if(isTeammate(e.getName())){
            System.out.println("isTeam");}
        else {
            double radarTurn =
                    // Absolute bearing to target
                    getHeadingRadians() + e.getBearingRadians()
                            // Subtract current radar heading to get turn required
                            - getRadarHeadingRadians();

            setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
            setTurnGunRightRadians(Utils.normalRelativeAngle(radarTurn));
            setFire(Math.min(400 / e.getDistance(), 3));
        }
    }



}
