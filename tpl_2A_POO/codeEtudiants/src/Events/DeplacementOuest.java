package Events;

import Robot.Robot;
import Carte.Direction;

public class DeplacementOuest extends Evenement {
    public DeplacementOuest(long date, Robot robot) {
        super(date, robot);
    }

    public void execute() {
        this.robot.moveRobotDirection(Direction.OUEST);
    }

}
