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
            long time = 0;
            Simulateur simulateur = new Simulateur(donnees, time);

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}
