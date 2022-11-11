package Events;

import Carte.Direction;
import Robot.*;
import Simulation.*;

import java.util.Iterator;

// Classe d'évènement qui permet de faire déplacer un robot donné vers le sud
public class DeplacementSud extends Evenement {
    public DeplacementSud(long date) {
        super(date);
    }

    public void execute() {
        this.robot.moveRobotDirection(Direction.SUD);
        // DonneesSimulation donnees = this.simulateur.getDonnees();
        // Iterator<Robot> robots = donnees.getRobots();
        // Robot robot = robots.next();
        // // faut verifier que c pas nul!
        // // Robot robot = donnees.getRobot();
    }
}
