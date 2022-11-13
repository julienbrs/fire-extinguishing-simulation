package Events;

import Robot.*;
import Simulation.*;

public class DebutAction extends Evenement {
    public DebutAction(long date, Robot robot, Simulateur simulateur) {
        super(date, robot, simulateur);
    }

    public void execute() {
        this.robot.setDisponible(false);
    }

}
