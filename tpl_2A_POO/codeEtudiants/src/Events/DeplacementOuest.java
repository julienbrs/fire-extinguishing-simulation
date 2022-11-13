package Events;

import Robot.Robot;
import Simulation.Simulateur;
import Carte.Direction;

public class DeplacementOuest extends Evenement {
    public DeplacementOuest(long date, Robot robot, Simulateur simulateur) {
        super(date, robot, simulateur);
    }

    public void execute() {
        this.robot.moveRobotDirection(Direction.OUEST);
    }

}
