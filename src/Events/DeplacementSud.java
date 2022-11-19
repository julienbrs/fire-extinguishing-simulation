package Events;

import Carte.Direction;
import Robot.*;
import Simulation.Simulateur;

public class DeplacementSud extends Evenement {
    
    public DeplacementSud(long date, Robot robot, Simulateur simulateur) {
        super(date, robot, simulateur);
    }

    /**
     * Le robot se tourne vers le Sud et avance.
     */
    public void execute() {
        this.robot.setDirectionImage(Direction.SUD);
        this.robot.moveRobotDirection(Direction.SUD);
    }
}
