package Events;

import Robot.Robot;
import Simulation.Simulateur;
import Exception.*;

public class DeversementEau extends Evenement {
    int volume;

    public DeversementEau(long date, Robot robot, int vol, Simulateur simulateur) {
        super(date, robot, simulateur);
        this.volume = vol;
    }

    public void execute() {
        try {
            this.robot.deverserEau(this.volume);
        } catch (VolumeEauIncorrectException e) {
            System.out.println(e);
        }

    }
}