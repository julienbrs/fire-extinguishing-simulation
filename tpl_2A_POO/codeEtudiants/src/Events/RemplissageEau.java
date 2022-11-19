package Events;

import Robot.Robot;
import Simulation.Simulateur;
import Exception.*;

public class RemplissageEau extends Evenement {
    
    public RemplissageEau(long date, Robot robot, Simulateur simulateur) {
        super(date, robot, simulateur);
    }

    /**
     * Remplit le réservoir du robot au maximum.
     * Jette {@link TerrainIncorrectException} si cela n'est pas possible (mauvais
     * robot ou pas de case pour remplir )
     */
    public void execute() {
        try {
            this.robot.remplirReservoir();
        } catch (TerrainIncorrectException e) {
            System.out.println(e);
        }

    }
}