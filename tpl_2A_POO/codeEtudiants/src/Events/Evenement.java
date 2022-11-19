package Events;

import Simulation.Simulateur;
import Robot.*;

public abstract class Evenement {
    
    protected long date;
    protected Simulateur simulateur;
    protected Robot robot;

    public Evenement(long date, Robot robot, Simulateur simulateur) {
        this.simulateur = simulateur;
        this.date = this.simulateur.getDateCourante() + date; // On ajoute la date de manière relative
        this.robot = robot;
    }

    /**
     * Renvoie la date à laquelle l'évènement doit s'exécuter.
     */
    public long getDate() {
        return this.date;
    }

    public abstract void execute();

}