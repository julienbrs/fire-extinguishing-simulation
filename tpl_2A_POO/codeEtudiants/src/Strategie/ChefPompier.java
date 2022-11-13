package Strategie;

import java.util.HashMap;

import Carte.Incendie;
import Robot.Robot;
import Simulation.DonneesSimulation;
import Simulation.Simulateur;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Queue;

public class ChefPompier {
    Simulateur simulation;
    DonneesSimulation donnees;
    Queue<Incendie> incendiesAffectes;
    Queue<Incendie> incendiesNonAffectes;

    Queue<Robot> robotsAffectes;
    Queue<Robot> robotsNonAffectes;

    public ChefPompier(Simulateur simulation, DonneesSimulation donnees) {
        this.simulation = simulation;
        this.donnees = donnees;
        this.incendiesAffectes = new LinkedList<Incendie>();
        this.incendiesNonAffectes = new LinkedList<Incendie>();
        this.robotsAffectes = new LinkedList<Robot>();
        this.robotsNonAffectes = new LinkedList<Robot>();
        Iterator<Incendie> incendies = this.donnees.getIncendies();
        Iterator<Robot> robots = this.donnees.getRobots();

        while (incendies.hasNext())
            this.incendiesNonAffectes.add(incendies.next());
        while (robots.hasNext())
            this.robotsNonAffectes.add(robots.next());
    }

    /*
     * renvoie vrai si l'incendie a été affecté
     */
    private boolean affecteRobotIncendie() {
        // /* Si tout les incendies sont affectées */
        if (this.incendiesNonAffectes.isEmpty())
            return true;
        // /* On récupère le premier incendie */
        Incendie incendie = this.incendiesNonAffectes.poll();

        /* Robots qui peuvent pas atteindre incendie */
        Queue<Robot> robotsInvalides = new LinkedList<Robot>();

        Robot robot = this.robotsNonAffectes.poll();

        /*
         * Si robot.affecteIncendie renvoie false
         * Le robot n'accepte pas l'incendie
         */
        while (robot != null && !robot.affecteIncendie(incendie)) {
            robotsInvalides.add(robot);
            robot = robotsNonAffectes.poll();
        }
        /* Si on trouve pas de robot qui peut éteindre l'incendie */
        if (robot == null) {
            /* Aucun robot existe qui peut l'éteindre */
            if (this.robotsAffectes.isEmpty()) {
                // On ignore l'incendie
                return true;
            }
            /* On remet les robots parcourus dans la liste non affectées */
            /* L'ordre est inversé mais c'est pas important */
            this.robotsNonAffectes = robotsInvalides;
            return false;
        }

        /* Sinon, il a été affecté */
        this.incendiesAffectes.add(incendie);
        return true;

    }

    private void checkRobotsAffectes() {
        Queue<Robot> tempQueue = new LinkedList<Robot>();
        Robot robot = this.robotsAffectes.poll();
        while(this.robotsNonAffectes.)
    }

    public void affecteIncendies() {

    }
}