package Strategie;

import java.util.HashMap;

import Carte.Case;
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
    HashMap<Incendie, Robot> incendiesAffectes;
    HashMap<Incendie, Robot> incendiesNonAffectes;

    Queue<Robot> robotsAffectes;
    Queue<Robot> robotsNonAffectes;

    public ChefPompier(Simulateur simulation, DonneesSimulation donnees) {
        this.simulation = simulation;
        this.donnees = donnees;
        this.incendiesAffectes = new HashMap<Incendie, Robot>();
        this.incendiesNonAffectes = new HashMap<Incendie, Robot>();
        Iterator<Incendie> incendies = this.donnees.getIncendies();

        while (incendies.hasNext())
            this.incendiesNonAffectes.put(incendies.next(), null);
    }

    private void checkIncendiesAffectes() {
        if (!this.incendiesNonAffectes.isEmpty())
            return;
        Queue<Incendie> incendiesASupprimer = new LinkedList<Incendie>();

        for (Incendie incendie : this.incendiesAffectes.keySet()) {
            if (!incendie.estEteint()) {
                incendiesASupprimer.add(incendie);
                this.incendiesNonAffectes.put(incendie, null);
            }
        }

        for (Incendie incendie : incendiesASupprimer) {
            this.incendiesAffectes.remove(incendie);
        }
    }

    public void affecteRobots() {
        Iterator<Robot> robots = this.donnees.getRobots();
        Robot robot = null;
        Graphe graphe = null;

        Queue<Incendie> incendiesASupprimer = new LinkedList<Incendie>();
        while (robots.hasNext()) {
            double minCout = Double.POSITIVE_INFINITY;
            double cout = 0;
            Incendie incendiePlusProche = null;
            robot = robots.next();
            if (!robot.isDisponible())
                continue;
            graphe = new Graphe(donnees, donnees.getCarte(), robot);
            graphe.calculeChemins();
            for (Incendie incendie : this.incendiesNonAffectes.keySet()) {
                if (incendie.estEteint()) {
                    incendiesASupprimer.add(incendie);
                    continue;
                }

                if (!robot.peutEteindre(incendie))
                    continue;
                try {
                    cout = graphe.getCout(incendie.getPosition());
                    if (cout < minCout) {
                        minCout = cout;
                        incendiePlusProche = incendie;
                    }
                } catch (Exception e) {
                    // Si graphe pas initialisé, ca doit pas arriver
                    e.printStackTrace();
                }
            }

            for (Incendie incendie : incendiesASupprimer) {
                this.incendiesAffectes.remove(incendie);
            }

            if (incendiePlusProche != null) {
                this.incendiesNonAffectes.remove(incendiePlusProche);
                this.incendiesAffectes.put(incendiePlusProche, robot);

                robot.affecteIncendie(incendiePlusProche);
                Chemin chemin = graphe.cheminDestination(incendiePlusProche.getPosition());
                chemin.cheminToEvent(simulation);
            }
        }

        // SOLO SI ISETEINT
        // todo a tester
        if (this.incendiesNonAffectes.isEmpty()) {
            this.checkIncendiesAffectes();
        }

    }

}
