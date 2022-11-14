package Events;

import Robot.Robot;
import Simulation.Simulateur;
import Carte.Direction;

public class DeplacementNord extends Evenement {
    public DeplacementNord(long date, Robot robot, Simulateur simulateur) {
        super(date, robot, simulateur);
    }

    public void execute() {
        this.robot.setDirectionImage(Direction.NORD);
        this.robot.moveRobotDirection(Direction.NORD);
    }
}
