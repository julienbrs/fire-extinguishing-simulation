package Strategie;

import Carte.*;
import Robot.Robot;
import Simulation.DonneesSimulation;

import java.util.Iterator;
import java.util.PriorityQueue;

/*
* Graphe où les sommets sont les Case d'une carte donnée.
* On ne retient pas les aretes parce-que on peut les retrouver
* avec les voisins dans carte, et la fonction getVitesse du robot.
*
*/
public class Graphe {
    private Carte carte;
    private Robot robot;
    private Case[][] previousCase;
    private CaseComparator caseComparator;

    private boolean rempli;

    public Graphe(Carte carte, Robot robot) {
        this.carte = carte;
        this.robot = robot;
        this.previousCase = null;
        this.caseComparator = null;
        this.rempli = false;
    }

    /**
     * Renvoie le coût d'une {@link Case}.
     * Doit être précédé par un appel de {@link #calculeChemins()}
     * 
     * @param position
     * @return double
     * @throws Exception
     */
    public double getCout(Case position) throws Exception {
        if (!rempli) {
            throw new Exception("Le graphe n'as pas été initialisé");
        }
        return this.caseComparator.getCout(position);
    }

    /**
     * Remplit le {@link Graphe} avec le {@link Chemin} le plus court entre la
     * {@link Case} de départ et la {@link Case} d'arrivée.
     * 
     * @param destination
     * @return Chemin
     */
    public Chemin cheminDestination(Case destination) {
        /* Si le graphe n'est rempli on le calcule */
        if (!rempli) {
            return this.cheminDestination(this.robot.getPosition(), destination);
        }
        /* Sinon on le cherche */
        return this.createChemin(destination, this.previousCase, this.caseComparator);
    }

    /**
     * Calcul les {@link Chemin}s les plus courts depuis la {@link Case} de
     * départ grâce à l'algorithme de Dijkstra.
     */
    public void calculeChemins() {
        this.rempli = true;
        this.Dijkstra(robot.getPosition(), null, false);
    }

    /**
     * Renvoie le {@link Chemin} plus court depuis la {@link Case} source à
     * {@link Case} destination.
     * 
     * @param source
     * @param destination
     * @return Chemin
     */
    private Chemin cheminDestination(Case source, Case destination) {
        this.rempli = false;
        return this.Dijkstra(source, destination, false);
    }

    /**
     * Renvoie le chemin vers la {@link Case} plus proche où le {@link Robot} du
     * graphe peut remplir.
     * 
     * @return Chemin
     */
    public Chemin cheminRemplir() {
        this.rempli = false;
        return this.Dijkstra(this.robot.getPosition(), null, true);
    }

    /**
     * Calcule et renvoie le cout du déplacement entre deux {@link Case}s
     * 
     * @param source
     * @param destination
     * @return double
     */
    private double coutDeplacement(Case source, Case destination) {

        double res = this.robot.getVitesse(source.getNature()) +
                robot.getVitesse(destination.getNature());
        res /= 2;
        res = carte.getTailleCases() / res;

        return res * 3600;
    }

    /**
     * Calcule et renvoie le chemin à partir du {@link Robot} du graphe de la
     * source jusqu'à la {@link Case} destination. Si on est déjà sur la case, le
     * chemin contiendra la case deux fois, en tant que source et en tant que
     * destination. Le tableau des noeuds précédents et le {@link CaseComparator}
     * sont utilisées pour calculer le chemin.
     * 
     * @param destination
     * @param prev
     * @param comparator
     * @return Chemin
     */
    private Chemin createChemin(Case destination, Case[][] prev, CaseComparator comparator) {
        Chemin chemin = null;
        Case source = this.robot.getPosition();

        Case prec = destination;

        if (prev[destination.getLigne()][destination.getColonne()] != null || destination == source) {
            chemin = new Chemin(robot);

            /* Si la destination est la source, on rajoute la source */
            /* Pour pouvoir verifier ce cas */
            if (destination == source) {
                chemin.add(source, comparator.getCout(source));
            }
            /* On reconstruit le chemin avec les noeuds précédents */
            while (prec != null) {
                chemin.add(prec, comparator.getCout(prec));
                prec = prev[prec.getLigne()][prec.getColonne()];
            }
        }
        return chemin;
    }

    /**
     * La fonction Dijkstra a trois finalité.
     * <p>
     * Si destination est specifié et chercheEau à false on renvoie le chemin plus
     * court de la {@link Case} source à la {@link Case} destination.
     * <p>
     * 
     * Si chercheEau est true elle renvoit la {@link Case} plus proche où le
     * {@link Robot} du graphe peut remplir.
     * 
     * <p>
     * Si destination est à null, et chercheEau à false, la fonction calcule les
     * chemins plus courts
     * vers tous les {@link Case}s atteignables par le {@link Robot} du graphe.
     * 
     * @param source
     * @param destination
     * @param chercheEau
     * @return Chemin
     */
    private Chemin Dijkstra(Case source, Case destination, boolean chercheEau) {
        Robot robot = this.robot;
        int nbLignes = this.carte.getNbLignes();
        int nbColonnes = this.carte.getNbColonnes();
        int nbElements = nbLignes * nbColonnes;
        Case prev[][] = new Case[nbLignes][nbColonnes];
        Chemin chemin = null;
        CaseComparator comparator = new CaseComparator(nbLignes, nbColonnes);
        PriorityQueue<Case> queue = new PriorityQueue<Case>(nbElements, comparator);

        /* On initialise prev et les couts de chaque case */
        Case caseCourante = null;
        for (int ligne = 0; ligne < nbLignes; ligne++) {
            for (int colonne = 0; colonne < nbColonnes; colonne++) {
                caseCourante = this.carte.getCase(ligne, colonne);

                /* On rajoute la source à la fin */
                if (caseCourante == source)
                    continue;
                /* On commence avec un cout infini */
                comparator.setCout(caseCourante, Double.POSITIVE_INFINITY);

                /* On prend pas en compte les cases où on peut pas se deplacer */
                if (!robot.canMove(caseCourante))
                    continue;

                queue.add(caseCourante);

                /* Pas de case précédente au départ */
                prev[ligne][colonne] = null;

            }
            comparator.setCout(source, 0);
            queue.add(source);
        }

        while (!queue.isEmpty()) {
            /* On prend la case avec le cout minimal */
            caseCourante = queue.poll();

            /* On renvoit la prèmiere case qui satisfait les conditions d'appel */
            if ((!chercheEau && caseCourante == destination) || (chercheEau && robot.peutRemplir(caseCourante))) {
                chemin = this.createChemin(caseCourante, prev, comparator);
                return chemin;
            }

            /* Mise à jour du coût des voisins */
            for (Iterator<Case> voisins = this.carte.getVoisins(caseCourante); voisins.hasNext();) {
                Case voisin = voisins.next();

                if (!robot.canMove(voisin))
                    continue;

                /* Calcul du cout */
                double coutVoisin = comparator.getCout(voisin);
                double cout = comparator.getCout(caseCourante) + this.coutDeplacement(caseCourante, voisin);

                /* Si on trouve un meilleur chemin, on mets à jour les variables */
                if (cout < coutVoisin) {
                    comparator.setCout(voisin, cout);
                    prev[voisin.getLigne()][voisin.getColonne()] = caseCourante;
                    queue.remove(voisin);
                    queue.add(voisin);
                }

            }
        }

        this.previousCase = prev;
        this.caseComparator = comparator;
        return null;

    }

}
