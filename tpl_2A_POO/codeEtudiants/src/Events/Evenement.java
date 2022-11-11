package Events;

import Simulation.Simulateur;
import Robot.*;

public abstract class Evenement {
    protected long date;
    protected Simulateur simulateur;
    protected Robot robot;

    public Evenement(long date, Robot robot) {
        this.date = date;
        this.robot = robot;
    }

    public long getDate() {
        return this.date;
    }

    public abstract void execute();

}