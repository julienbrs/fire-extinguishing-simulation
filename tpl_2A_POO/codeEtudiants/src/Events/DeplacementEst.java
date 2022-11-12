package Events;

import Robot.*;
import Simulation.Simulateur;
import Carte.Direction;

public class DeplacementEst extends Evenement {

    public DeplacementEst(long date, Robot robot, Simulateur simulateur) {
        super(date, robot, simulateur);
    }

    public void execute() {
        this.robot.moveRobotDirection(Direction.EST);
    }
}
