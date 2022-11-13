package Events;

import Robot.Robot;
import Simulation.Simulateur;

public class AffectationIncendiesRobots extends Evenement {
    int periode;

    public AffectationIncendiesRobots(long date, Robot robot, Simulateur simulateur, int periode) {
        super(date, null, simulateur);
        this.periode = periode;
    }

    public void execute() {
        this.simulateur.getChefPompier().affecteIncendies();
        simulateur.ajouteEvenement(
                new AffectationIncendiesRobots(this.periode, this.robot, this.simulateur, this.periode));
    }
}
