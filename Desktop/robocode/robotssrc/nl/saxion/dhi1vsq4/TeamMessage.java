package nl.saxion.dhi1vsq4;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class TeamMessage implements Serializable {

    private String [] teamMates;
    public TeamMessage(String[] teamMates ){
        this.teamMates = teamMates;
    }

    public String[] getTeamMates() {
        return teamMates;
    }
}
