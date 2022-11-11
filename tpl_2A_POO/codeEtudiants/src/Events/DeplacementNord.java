package Events;

import Robot.Robot;
import Carte.Direction;

public class DeplacementNord extends Evenement {
    public DeplacementNord(long date, Robot robot) {
        super(date, robot);
    }

    public void execute() {
        this.robot.moveRobotDirection(Direction.NORD);
    }
}
