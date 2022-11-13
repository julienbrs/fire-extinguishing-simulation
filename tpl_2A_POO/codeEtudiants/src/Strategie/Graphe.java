package Strategie;

import Carte.*;
import Robot.Robot;

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
    Carte carte;

    public Graphe(Carte carte) {
        this.carte = carte;
    }

    public Chemin cheminDestination(Case source, Case destination, Robot robot) {
        return this.Dijkstra(source, destination, robot, false);
    }

    public Chemin cheminRemplir(Robot robot) {
        return this.Dijkstra(robot.getPosition(), null, robot, true);
    }

    private double Edges(Robot robot, Case source, Case destination) {

        // todo vitesse in km/h and time in ????
        double res = robot.getVitesse(source.getNature()) +
                robot.getVitesse(destination.getNature());
        res /= 2;
        res = carte.getTailleCases() / res;

        return res;
    }

    private Chemin Dijkstra(Case source, Case destination, Robot robot, boolean chercheEau) {
        int nbLignes = this.carte.getNbLignes();
        int nbColonnes = this.carte.getNbColonnes();
        int nbElements = nbLignes * nbColonnes;
        Case prev[][] = new Case[nbLignes][nbColonnes];
        Chemin chemin = new Chemin(robot);

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
                Case prec = caseCourante;
                if (prev[caseCourante.getLigne()][caseCourante.getColonne()] != null || caseCourante == source) {
                    while (prec != null) {
                        chemin.add(prec, comparator.getCout(prec));
                        prec = prev[prec.getLigne()][prec.getColonne()];
                    }
                }
                return chemin;
            }
            for (Iterator<Case> voisins = this.carte.getVoisins(caseCourante); voisins.hasNext();) {
                Case voisin = voisins.next();

                if (!robot.canMove(voisin))
                    continue;
                double coutVoisin = comparator.getCout(voisin);
                double cout = comparator.getCout(caseCourante) + this.Edges(robot, caseCourante, voisin);

                if (cout < coutVoisin) {
                    comparator.setCout(voisin, cout);
                    prev[voisin.getLigne()][voisin.getColonne()] = caseCourante;
                    queue.remove(voisin);
                    queue.add(voisin);
                }

            }
        }
        // On devrait pas arriver la
        return chemin;

    }

}
