package Events;

import Simulation.Simulateur;
import Carte.Direction;
import Carte.Case;
import Robot.Robot;

public class Avion extends Evenement {
    
    Case positionAvion;
    int periode;

    public Avion(long date, Robot robot, Case positionAvion, Simulateur simulateur, int periode) {
        super(date, robot, simulateur);
        this.positionAvion = positionAvion;
        this.periode = periode;
    }

    /**
     * Petit avion décoratif qui passe au dessus de la carte.
     */
    public void execute() {
        try {
            this.positionAvion = this.simulateur.getDonnees().getCarte().getVoisin(this.positionAvion, Direction.EST);
            simulateur.setPositionAvion(this.positionAvion);
            simulateur.ajouteEvenement(new Avion(periode, robot, this.positionAvion, this.simulateur, this.periode));
        } catch (IllegalArgumentException e) {
            this.positionAvion = null;
            simulateur.setPositionAvion(this.positionAvion);
        }
    }
}
