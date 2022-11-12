package Strategie;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;

import Carte.Case;

public class Chemin {
    private Robot robot;
    private LinkedList<Case> chemin;
    private HashMap<Case, Double> couts;

    public Chemin(Robot robot) {
        this.robot = robot;
        this.chemin = new LinkedList<Case>();
        this.couts = new HashMap<Case, Double>();
    }

    void add(Case caseToAdd, double date) {
        chemin.addFirst(caseToAdd);
        couts.put(caseToAdd, date);
    }

    public Iterator<Case> getChemin() {
        return this.chemin.iterator();
    }

    public double getCout(Case case1) {
        return this.couts.get(case1);
    }
}