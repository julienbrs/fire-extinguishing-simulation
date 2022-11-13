package Events;

import Robot.*;
import Simulation.*;

public class FinAction extends Evenement {
    public FinAction(long date, Robot robot, Simulateur simulateur) {
        super(date, robot, simulateur);
    }

    public void execute() {
        System.out.println("FIN ACTION PET");
        this.robot.nextStep(this.simulateur);
    }

}