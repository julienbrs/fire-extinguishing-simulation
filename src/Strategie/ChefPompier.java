package Strategie;

import java.util.HashSet;

import Carte.Incendie;
import Robot.Robot;
import Simulation.DonneesSimulation;
import Simulation.Simulateur;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Queue;

public class ChefPompier {
    private Simulateur simulation;
    private DonneesSimulation donnees;
    private HashSet<Incendie> incendiesAffectes;
    private HashSet<Incendie> incendiesNonAffectes;

    public ChefPompier(Simulateur simulation, DonneesSimulation donnees) {

        this.simulation = simulation;
        this.donnees = donnees;
        this.incendiesAffectes = new HashSet<Incendie>();
        this.incendiesNonAffectes = new HashSet<Incendie>();
        Iterator<Incendie> incendies = this.donnees.getIncendies();

        while (incendies.hasNext())
            this.incendiesNonAffectes.add(incendies.next());
    }

    /**
     * Si tous les incendies sont affectés, on ne fait rien. Sinon, on affecte un
     * incendie à un robot disponible.
     */
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

        /* On met à jour les structures de données */
        for (Incendie incendieASupprimer : incendiesASupprimer) {
            this.incendiesNonAffectes.add(incendieASupprimer);
            this.incendiesAffectes.remove(incendieASupprimer);
        }
    }

    /**
     * Méthode privée qui renvoit l'incendie plus proche au {@link Robot} passé en
     * argument.
     * Les incendies cherchées sont dans l' {@link Iterator} incendies, et graphe
     * est le graphe du robot rempli avec {@link Graphe#calculeChemins()}
     * 
     * @param graphe
     * @param robot
     * @param incendies
     * @return Incendie
     */
    private Incendie getIncendiePlusProche(Graphe graphe, Robot robot, Iterator<Incendie> incendies) {
        Incendie incendiePlusProche = null;
        Incendie incendie = null;
        double minCout = Double.POSITIVE_INFINITY;
        double cout = 0;

        /*
         * On ne peut pas supprimer un élément dans une structure
         * pendant le parcours de son itérateur
         */
        Queue<Incendie> incendiesASupprimer = new LinkedList<Incendie>();

        /* Pour tous les incendies */
        while (incendies.hasNext()) {
            incendie = incendies.next();

            /* Si l'incendie a été éteint entre temps, on le supprimera */
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
                /* Si graphe pas initialisé, ca ne doit pas arriver */
                e.printStackTrace();
            }
        }

        /* On supprime les incendies éteints */
        for (Incendie incendieASupprimer : incendiesASupprimer) {
            this.incendiesNonAffectes.remove(incendieASupprimer);
        }

        return incendiePlusProche;
    }

    /**
     * Recalcule les incendies non affectées pour pouvoir implementer la
     * propagation.
     * Il faut parcourir les incendies pour pouvoir prendre en compte ceux rajoutées
     * par {@link Events.PropagationIncendie}.
     */
    private void updateIncendiePropagation() {
        this.incendiesNonAffectes = new HashSet<Incendie>();
        Iterator<Incendie> incendies = this.donnees.getIncendies();

        while (incendies.hasNext())
            this.incendiesNonAffectes.add(incendies.next());

        this.incendiesNonAffectes.removeAll(incendiesAffectes);
    }

    /**
     * Si tous les robots sont affectés, on ne fait rien. Sinon, on affecte un
     * robot le plus proche {@link Incendie}.
     */
    public void affecteRobots() {
        updateIncendiePropagation();
        Iterator<Robot> robots = this.donnees.getRobots();
        Robot robot = null;
        Graphe graphe = null;

        Incendie incendiePlusProche = null;

        /* Pour tout robot */
        while (robots.hasNext()) {
            robot = robots.next();
            /* Si le robot n'est pas disponible, on continue à chercher */
            if (!robot.isDisponible())
                continue;

            /* On calcule les meilleurs chemins à toutes les cases */
            graphe = new Graphe(this.donnees.getCarte(), robot);
            graphe.calculeChemins();

            /* On calcule l'incendie plus proche */
            incendiePlusProche = getIncendiePlusProche(graphe, robot, incendiesNonAffectes.iterator());

            /* Aucun incendie était atteignable, on cherche dans les incendiesAffectes */
            if (incendiePlusProche == null)
                incendiePlusProche = getIncendiePlusProche(graphe, robot, incendiesAffectes.iterator());

            /* Si on a trouvé un incendie plus proche */
            if (incendiePlusProche != null) {
                /* On calcule le chemin */
                Chemin chemin = graphe.cheminDestination(incendiePlusProche.getPosition());

                /* Si il n'existe pas de chemin, on n'affecte pas l'incendie */
                if (chemin == null)
                    continue;

                /* Sinon on l'ajoute aux incendies affectés */
                this.incendiesNonAffectes.remove(incendiePlusProche);
                this.incendiesAffectes.add(incendiePlusProche);

                /* On affecte l'incendie au robot, et on programme le deplacement */
                robot.affecteIncendie(incendiePlusProche);
                chemin.cheminToEvent(simulation);
            }
        }

        /* Si il n'y a plus d'incendie à affecter, on aidera les autres robots */
        if (this.incendiesNonAffectes.isEmpty())
            this.checkIncendiesAffectes();

    }

}
