package Events;

import Robot.Robot;
import Simulation.Simulateur;
import Carte.Direction;

public class DeplacementOuest extends Evenement {
    
    public DeplacementOuest(long date, Robot robot, Simulateur simulateur) {
        super(date, robot, simulateur);
    }

    /**
     * Le robot se tourne vers l'Ouest et avance.
     */
    public void execute() {
        this.robot.setDirectionImage(Direction.OUEST);
        this.robot.moveRobotDirection(Direction.OUEST);
    }

}
