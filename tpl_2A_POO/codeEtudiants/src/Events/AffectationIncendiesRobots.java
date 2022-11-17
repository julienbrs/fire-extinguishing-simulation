package Events;

import Robot.Robot;
import Simulation.Simulateur;

public class AffectationIncendiesRobots extends Evenement {
    
    int periode;

    public AffectationIncendiesRobots(long date, Robot robot, Simulateur simulateur, int periode) {
        super(date, null, simulateur);
        this.periode = periode;
    }

    /**
     * Le chef pompier attribue un incendie à chaque robot disponible, et ajoute un
     * nouvel évènement
     * {@link #AffectationIncendiesRobots(long, Robot, Simulateur, int)} pour que le
     * chef pompier fasse cette action en boucle.
     */
    public void execute() {
        this.simulateur.getChefPompier().affecteRobots();
        simulateur.ajouteEvenement(
                new AffectationIncendiesRobots(this.periode, this.robot, this.simulateur, this.periode));
    }
}
