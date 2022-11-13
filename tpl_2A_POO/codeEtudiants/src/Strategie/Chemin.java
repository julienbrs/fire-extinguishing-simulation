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

    void add(Case caseToAdd, double date) {
        chemin.addFirst(caseToAdd);
        couts.put(caseToAdd, date);
    }

    public Iterator<Case> getChemin() {
        return this.chemin.iterator();
    }

    // todo check wait why should this be public?
    private double getCout(Case case1) {
        return this.couts.get(case1);
    }

    public void cheminToEvent(Simulateur simulateur) {
        Iterator<Case> iterator = this.getChemin();
        // On saute la premiere case (source)
        Case casePrecedente = null;
        Case caseCourante = iterator.next();
        Evenement evenement = null;
        long cout = 0;

        simulateur.ajouteEvenement(new DebutAction(0, this.robot, simulateur));// <33333
                                                                               // COUCOUUUUUUUUUUUUUUUUUUUUUUUUUUU
        while (iterator.hasNext()) {
            casePrecedente = caseCourante;
            caseCourante = iterator.next();
            cout = (long) Math.ceil(this.getCout(caseCourante));
            Direction dir = Carte.getDirection(casePrecedente, caseCourante);

            switch (dir) {
                case NORD:
                    evenement = new DeplacementNord(cout, this.robot, simulateur);
                    break;
                case EST:
                    evenement = new DeplacementEst(cout, this.robot, simulateur);
                    break;
                case OUEST:
                    evenement = new DeplacementOuest(cout, this.robot, simulateur);
                    break;
                case SUD:
                    evenement = new DeplacementSud(cout, this.robot, simulateur);
                    break;
                default:
                    // bizarre l'ambiance
                    // todo
            }
            simulateur.ajouteEvenement(evenement);
        }
        simulateur.ajouteEvenement(new FinAction(cout + 1, this.robot, simulateur));

    }

}