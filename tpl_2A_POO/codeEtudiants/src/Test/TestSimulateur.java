package Test;

import Simulation.Simulateur;
import Simulation.DonneesSimulation;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import java.awt.Color;

import gui.GUISimulator;
import gui.Simulable;
import io.LecteurDonnees;

public class TestSimulateur {
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
            Simulable simulateur = new Simulateur(gui, donnees);

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}
