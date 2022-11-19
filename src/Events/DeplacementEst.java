package Events;

import Robot.*;
import Simulation.Simulateur;
import Carte.Direction;

public class DeplacementEst extends Evenement {

    public DeplacementEst(long date, Robot robot, Simulateur simulateur) {
        super(date, robot, simulateur);
    }

    /**
     * Le robot se tourne vers l'Est et avance.
     */
    public void execute() {
        this.robot.setDirectionImage(Direction.EST);
        this.robot.moveRobotDirection(Direction.EST);
    }
}
