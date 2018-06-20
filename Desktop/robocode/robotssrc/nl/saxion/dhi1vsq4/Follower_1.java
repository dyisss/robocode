package nl.saxion.dhi1vsq4;

import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

import java.awt.geom.Point2D;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Follower_1 extends TeamRobot {
    private String[] teamlist;

    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);

        String[] teammates = getTeammates();
        for(int i = 0; i<teammates.length;i++)
            System.out.println(teammates[i]);
        while (true) {
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
        }
        if(event.getMessage() instanceof TeamMessage){
            TeamMessage message = (TeamMessage) event.getMessage();
            teamlist = message.getTeamMates();
        }
    }
}
