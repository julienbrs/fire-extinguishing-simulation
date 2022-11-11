package Events;

import Carte.Direction;
import Robot.*;

// Classe d'évènement qui permet de faire déplacer un robot donné vers le sud
public class DeplacementSud extends Evenement {
    public DeplacementSud(long date, Robot robot) {
        super(date, robot);
    }

    public void execute() {
        this.robot.moveRobotDirection(Direction.SUD);
    }
}
