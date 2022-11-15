package Strategie;

import Carte.*;
import Robot.Robot;
import Simulation.DonneesSimulation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/*
* Graphe où les sommets sont les Case d'une carte donnée.
* On ne retient pas les aretes parce-que on peut les retrouver
* avec les voisins dans carte, et la fonction getVitesse du robot.
*
*/
public class Graphe {
    private DonneesSimulation donnees;
    private Carte carte;
    private Robot robot;
    private Case[][] previousCase;
    private CaseComparator caseComparator;

    private boolean rempli;

    public Graphe(DonneesSimulation donnees, Carte carte, Robot robot) {
        this.donnees = donnees;
        this.carte = carte;
        this.robot = robot;
        this.previousCase = null;
        this.caseComparator = null;
        this.rempli = false;
    }

    /* Must be preceded by a remplitChemins call */
    public double getCout(Case position) throws Exception {
        if (!rempli) {
            throw new Exception("Le graphe n'as pas été initialisé");
        }
        return this.caseComparator.getCout(position);
    }

    public Chemin cheminDestination(Case destination) {
        /* Si le graphe est rempli on le cherche */
        if (!rempli) {
            return this.cheminDestination(this.robot.getPosition(), destination);
        }
        /* Sinon on le calcule */
        return this.createChemin(destination, this.previousCase, this.caseComparator);
    }

    public void calculeChemins() {
        this.rempli = true;
        this.Dijkstra(robot.getPosition(), null, false);
    }

    private Chemin cheminDestination(Case source, Case destination) {
        this.rempli = false;
        return this.Dijkstra(source, destination, false);
    }

    public Chemin cheminRemplir() {
        this.rempli = false;
        return this.Dijkstra(this.robot.getPosition(), null, true);
    }

    private double Edges(Case source, Case destination) {

        // todo vitesse in km/h and time in ????
        double res = this.robot.getVitesse(source.getNature()) +
                robot.getVitesse(destination.getNature());
        res /= 2;
        res = carte.getTailleCases() / res;

        return res * 3600;
    }

    private Chemin createChemin(Case destination, Case[][] prev, CaseComparator comparator) {
        Chemin chemin = null;
        Case source = this.robot.getPosition();
        Case prec = destination;
        if (prev[destination.getLigne()][destination.getColonne()] != null || destination == source) {
            chemin = new Chemin(robot);
            while (prec != null) {
                chemin.add(prec, comparator.getCout(prec));
                prec = prev[prec.getLigne()][prec.getColonne()];
            }
        }
        return chemin;
    }

    private Chemin Dijkstra(Case source, Case destination, boolean chercheEau) {
        Robot robot = this.robot;
        int nbLignes = this.carte.getNbLignes();
        int nbColonnes = this.carte.getNbColonnes();
        int nbElements = nbLignes * nbColonnes;
        Case prev[][] = new Case[nbLignes][nbColonnes];
        Chemin chemin = null;
        CaseComparator comparator = new CaseComparator(nbLignes, nbColonnes);
        PriorityQueue<Case> queue = new PriorityQueue<Case>(nbElements, comparator);

        // initialize prev and ""dist""
        Case caseCourante = null;
        for (int ligne = 0; ligne < nbLignes; ligne++) {
            for (int colonne = 0; colonne < nbColonnes; colonne++) {
                caseCourante = this.carte.getCase(ligne, colonne);
                if (caseCourante == source)
                    continue;
                comparator.setCout(caseCourante, Double.POSITIVE_INFINITY);

                if (!robot.canMove(caseCourante))
                    continue;

                queue.add(caseCourante);

                prev[ligne][colonne] = null;

            }
            comparator.setCout(source, 0);
            queue.add(source);
        }

        while (!queue.isEmpty()) {
            // The main loop
            // We get the min (head cause min-priority queue)
            caseCourante = queue.poll();

            if ((!chercheEau && caseCourante == destination) || (chercheEau && robot.peutRemplir(caseCourante))) {
                chemin = this.createChemin(caseCourante, prev, comparator);
                return chemin;
            }
            for (Iterator<Case> voisins = this.carte.getVoisins(caseCourante); voisins.hasNext();) {
                Case voisin = voisins.next();

                if (!robot.canMove(voisin))
                    continue;
                double coutVoisin = comparator.getCout(voisin);
                double cout = comparator.getCout(caseCourante) + this.Edges(caseCourante, voisin);

                if (cout < coutVoisin) {
                    comparator.setCout(voisin, cout);
                    prev[voisin.getLigne()][voisin.getColonne()] = caseCourante;
                    queue.remove(voisin);
                    queue.add(voisin);
                }

            }
        }
        // /* Si il n'y a pas de chemin */
        // if (!chercheFeu)
        // return null;
        // // On arrive ici que si on cherche l'incendie plus proche
        // // Ou il y a pas de
        // double cout = 0;
        // double coutMin = Double.POSITIVE_INFINITY;
        // Iterator<Incendie> incendies = donnees.getIncendies();
        // Incendie incendie = null;
        // Incendie plusProcheIncendie = null;
        // while (incendies.hasNext()) {
        // incendie = incendies.next();
        // if (!incendie.estEteint() && robot.peutEteindre(incendie)) {
        // cout = comparator.getCout(incendie.getPosition());
        // if (cout < coutMin) {
        // coutMin = cout;
        // plusProcheIncendie = incendie;
        // }
        // }
        // }
        // if (plusProcheIncendie == null)
        // return null;
        this.previousCase = prev;
        this.caseComparator = comparator;
        return null;

    }

}
