package Strategie;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;

import java.lang.Math;
import Simulation.Simulateur;
import Robot.Robot;
import Events.*;
import Carte.Case;
import Carte.Carte;
import Carte.Direction;

public class Chemin {

    private Robot robot;
    private LinkedList<Case> chemin;
    private HashMap<Case, Double> couts;

    public Chemin(Robot robot) {
        this.robot = robot;
        this.chemin = new LinkedList<Case>();
        this.couts = new HashMap<Case, Double>();
    }

    /**
     * Renvoie le dernier élément de la liste (qui sera la destination du
     * {@link Chemin}.
     * 
     * @return {@link Case}
     */
    public Case getDestination() {
        return this.chemin.getLast();
    }

    /**
     * Ajoute une {@link Case} à la liste des cases, et il faudra atteindre
     * cette case à la date donnée. Cette méthode n'est accessible qu'au classes du
     * même package.
     * 
     * @param caseToAdd
     * @param date
     */
    void add(Case caseToAdd, double date) {
        chemin.addFirst(caseToAdd);
        couts.put(caseToAdd, date);
    }

    /**
     * Renvoie le chemin sous forme d'un {@link Iterator} sur les {@link Case}s.
     * 
     * @return Iterator<Case>
     */
    public Iterator<Case> getChemin() {
        return this.chemin.iterator();
    }

    /**
     * Renvoie le coût d'une {@link Case} dans le chemin.
     * 
     * @param case1
     * @return double
     */
    public double getCout(Case case1) {
        return this.couts.get(case1);
    }

    /**
     * Convertit le {@link Chemin} en {@link Event}s grâce à la méthode
     * {@link Carte getDirection(casePrecedente, caseCourante)}.
     * 
     * @param simulateur
     */
    public void cheminToEvent(Simulateur simulateur) {
        Iterator<Case> iterator = this.getChemin();
        // On saute la premiere case (source)
        Case caseCourante = iterator.next();

        simulateur.ajouteEvenement(new DebutAction(0, this.robot, simulateur));
        simulateur.ajouteEvenement(new Deplacement(0, this.robot, simulateur, this, iterator, caseCourante));
    }

}