package Events;

import Robot.*;
import Carte.Direction;

public class DeplacementEst extends Evenement {

    public DeplacementEst(long date, Robot robot) {
        super(date, robot);
    }

    public void execute() {
        this.robot.moveRobotDirection(Direction.EST);
    }
}
