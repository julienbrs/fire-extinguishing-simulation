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
            GUISimulator gui = new GUISimulator(tailleCases * nbColonnes, tailleCases * nbLignes, Color.lightGray);
            long time = 0;
            PriorityQueue<Evenement> scenario = new PriorityQueue();
            Simulateur simulateur = new Simulateur(gui, donnees, scenario, time);
            Iterator<Robot> robots = donnees.getRobots();
            Robot robot = robots.next();

            long un = 1;
            long deux = 2;
            long trois = 3;
            long quatre = 4;

            DeplacementNord nord = new DeplacementNord(un, robot);
            DeplacementEst est = new DeplacementEst(deux, robot);
            DeplacementSud sud = new DeplacementSud(trois, robot);
            DeplacementOuest ouest = new DeplacementOuest(quatre, robot);
            simulateur.ajouteEvenement(nord);
            simulateur.ajouteEvenement(est);
            simulateur.ajouteEvenement(sud);
            simulateur.ajouteEvenement(ouest);

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}
