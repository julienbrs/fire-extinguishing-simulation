package Strategie;

import java.util.HashMap;
import java.util.HashSet;

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
    HashSet<Incendie> incendiesAffectes;
    HashSet<Incendie> incendiesNonAffectes;

    Queue<Robot> robotsAffectes;
    Queue<Robot> robotsNonAffectes;

    public ChefPompier(Simulateur simulation, DonneesSimulation donnees) {
        this.simulation = simulation;
        this.donnees = donnees;
        this.incendiesAffectes = new HashSet<Incendie>();
        this.incendiesNonAffectes = new HashSet<Incendie>();
        Iterator<Incendie> incendies = this.donnees.getIncendies();

        while (incendies.hasNext())
            this.incendiesNonAffectes.add(incendies.next());
    }

    private void checkIncendiesAffectes() {
        /* Que si il n'y a plus d'incendie à affecter */
        if (!this.incendiesNonAffectes.isEmpty())
            return;

        /* Incendies qui seront affectables à nouveau */
        Queue<Incendie> incendiesASupprimer = new LinkedList<Incendie>();

        Iterator<Incendie> incendies = this.incendiesAffectes.iterator();
        Incendie incendie = null;
        while (incendies.hasNext()) {
            incendie = incendies.next();

            /* Si l'incendie n'est pas encore atteint */
            if (!incendie.estEteint()) {
                incendiesASupprimer.add(incendie);
            }
        }

        /* On mets à jour les structures de données */
        for (Incendie incendieASupprimer : incendiesASupprimer) {
            this.incendiesNonAffectes.add(incendie);
            this.incendiesAffectes.remove(incendieASupprimer);
        }
    }

    public void affecteRobots() {
        Iterator<Robot> robots = this.donnees.getRobots();
        Robot robot = null;
        Graphe graphe = null;

        /*
         * On peut pas supprimer un element dans une strucutre
         * pendant le parcours de son iterateur
         */
        Queue<Incendie> incendiesASupprimer = new LinkedList<Incendie>();

        /* Pour tous les robots */
        while (robots.hasNext()) {
            double cout = 0;

            double minCout = Double.POSITIVE_INFINITY;
            Incendie incendiePlusProche = null;

            robot = robots.next();
            /* Si le robot n'est pas disponible, on continue à chercher */
            if (!robot.isDisponible())
                continue;

            /* On calcule les meilleurs chemins à toutes les cases */
            graphe = new Graphe(donnees, donnees.getCarte(), robot);
            graphe.calculeChemins();

            Iterator<Incendie> incendies = this.incendiesNonAffectes.iterator();
            Incendie incendie = null;

            /* Pour tous les incendies */
            while (incendies.hasNext()) {
                incendie = incendies.next();

                /* Si l'incendie a été éteint entretemps, on le supprimera */
                if (incendie.estEteint()) {
                    incendiesASupprimer.add(incendie);
                    continue;
                }

                /* Si le robot ne peut pas atteindre le chemin, on passe au prochain incendie */
                if (!robot.peutEteindre(incendie))
                    continue;
                /* Sinon , on regarde le cout pour l'atteindre */
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

            /* On supprime les incendies éteints */
            for (Incendie incendieASupprimer : incendiesASupprimer) {
                this.incendiesAffectes.remove(incendieASupprimer);
            }

            /* Si on a trouvé un incendie, il est alors le plus proche */
            if (incendiePlusProche != null) {
                /* On l'ajoute aux incendies affectés */
                this.incendiesNonAffectes.remove(incendiePlusProche);
                this.incendiesAffectes.add(incendiePlusProche);

                /* On affecte l'incendie au robot, et on programme le deplacement */
                robot.affecteIncendie(incendiePlusProche);
                Chemin chemin = graphe.cheminDestination(incendiePlusProche.getPosition());
                chemin.cheminToEvent(simulation);
            }
        }

        /* Si il n'y a plus d'incendie à affecter, on aidera les autres robots */
        if (this.incendiesNonAffectes.isEmpty()) {
            this.checkIncendiesAffectes();
        }

    }

}
