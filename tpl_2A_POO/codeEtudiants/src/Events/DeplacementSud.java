package Events;

import Carte.Direction;
import Robot.*;
import Simulation.Simulateur;

// Classe d'évènement qui permet de faire déplacer un robot donné vers le sud
public class DeplacementSud extends Evenement {
    public DeplacementSud(long date, Robot robot, Simulateur simulateur) {
        super(date, robot, simulateur);
    }

    public void execute() {
        this.robot.moveRobotDirection(Direction.SUD);
    }
}
