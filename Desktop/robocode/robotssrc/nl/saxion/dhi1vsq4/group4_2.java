package nl.saxion.dhi1vsq4;

import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

import java.awt.geom.Point2D;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class group4_2 extends TeamRobot {
    Point2D lowestEnemyLocation;
    private double lowestX;
    private double lowestY;


    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);

        String[] teammates = getTeammates();
        for(int i = 0; i<teammates.length;i++)
            System.out.println(teammates[i]);
        while (true) {
            pow(lowestX,lowestY);
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

    public void onHitWall (HitWallEvent event) {
        turnRight(90);
        setBack(300);
        execute();
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        if (event.getMessage() instanceof Message){
            Message message = (Message) event.getMessage();
            lowestEnemyLocation = message.getLocation();
            lowestX = lowestEnemyLocation.getX();
            lowestY = lowestEnemyLocation.getY();
        }
    }

    public void pow(double x, double y){
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
}
