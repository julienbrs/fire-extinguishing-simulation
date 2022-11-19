package Events;

import Robot.*;
import Simulation.*;

public class FinAction extends Evenement {
    
    public FinAction(long date, Robot robot, Simulateur simulateur) {
        super(date, robot, simulateur);
    }

    /**
     * Le robot a fini sa tâche actuelle, on va lui assigner une nouvelle tâche.
     */
    public void execute() {
        this.robot.nextStep(this.simulateur);
    }

}