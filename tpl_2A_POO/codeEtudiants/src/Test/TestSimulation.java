package Test;

import io.LecteurDonnees;
import Simulation.DonneesSimulation;
import Simulation.Simulateur;
import gui.GUISimulator;
import Carte.Carte;
import Carte.Incendie;
import Robot.Robot;

import java.awt.Color;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.zip.DataFormatException;

public class TestSimulation {
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

            GUISimulator gui = new GUISimulator(tailleCases * nbColonnes * 2, tailleCases * nbLignes, Color.lightGray);
            long time = 0;
            Simulateur simulateur = new Simulateur(gui, donnees, time);

            Iterator<Robot> robots = donnees.getRobots();
            Robot robot = robots.next();
            Iterator<Incendie> incendies = donnees.getIncendies();
            Incendie incendie;

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}
