package Test;

import Simulation.Simulateur;
import Simulation.DonneesSimulation;
import Events.*;
import Robot.*;
import Carte.*;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import java.awt.Color;
import java.util.Iterator;
import java.util.PriorityQueue;

import gui.GUISimulator;
import gui.Simulable;
import io.LecteurDonnees;

public class TestSimulateurBonus {
    private static void moveRectangle(Simulateur simulateur, Robot robot) {
        DeplacementNord nord = new DeplacementNord(10, robot, simulateur);
        DeplacementEst est = new DeplacementEst(20, robot, simulateur);
        DeplacementSud sud = new DeplacementSud(30, robot, simulateur);
        DeplacementOuest ouest = new DeplacementOuest(40, robot, simulateur);
        simulateur.ajouteEvenement(nord);
        simulateur.ajouteEvenement(est);
        simulateur.ajouteEvenement(sud);
        simulateur.ajouteEvenement(ouest);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            DonneesSimulation donnees = LecteurDonnees.creeDonneesSimulation(args[0]);
            int tailleCases = donnees.getCarte().getTailleCases();
            int nbColonnes = donnees.getCarte().getNbColonnes();
            int nbLignes = donnees.getCarte().getNbLignes();

            System.out.println(
                    Integer.toString(tailleCases * nbColonnes) + " " + Integer.toString(tailleCases * nbLignes));
            long time = 0;
            Simulateur simulateur = new Simulateur(donnees, time, false, true);
            Iterator<Robot> robots = donnees.getRobots();
            Robot robot = robots.next();

            TestSimulateurBonus.moveRectangle(simulateur, robot);

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}
