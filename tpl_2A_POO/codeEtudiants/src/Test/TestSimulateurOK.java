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

public class TestSimulateurOK {
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
            Simulateur simulateur = new Simulateur(donnees, time, false, false, false);
            Iterator<Robot> robots = donnees.getRobots();
            Robot robot = robots.next();
            robot = robots.next();

            DeplacementNord nord = new DeplacementNord((long) 1, robot, simulateur);
            DeversementEau ext1 = new DeversementEau((long) 2, robot, 5000, simulateur, 0);
            DeplacementOuest ouest1 = new DeplacementOuest((long) 3, robot, simulateur);
            DeplacementOuest ouest2 = new DeplacementOuest((long) 4, robot, simulateur);
            RemplissageEau recharge = new RemplissageEau((long) 5, robot, simulateur);
            DeplacementEst est1 = new DeplacementEst((long) 6, robot, simulateur);
            DeplacementEst est2 = new DeplacementEst((long) 7, robot, simulateur);
            DeversementEau ext2 = new DeversementEau((long) 8, robot, 5000, simulateur, 0);

            simulateur.ajouteEvenement(nord);
            simulateur.ajouteEvenement(ext1);
            simulateur.ajouteEvenement(ouest1);
            simulateur.ajouteEvenement(ouest2);
            simulateur.ajouteEvenement(recharge);
            simulateur.ajouteEvenement(est1);
            simulateur.ajouteEvenement(est2);
            simulateur.ajouteEvenement(ext2);

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}
