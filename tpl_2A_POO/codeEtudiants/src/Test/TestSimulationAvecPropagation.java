package Test;

import io.LecteurDonnees;
import Simulation.DonneesSimulation;
import Simulation.Simulateur;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

public class TestSimulationAvecPropagation {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            DonneesSimulation donnees = LecteurDonnees.creeDonneesSimulation(args[0]);
            long time = 0;
            Simulateur simulateur = new Simulateur(donnees, time, true, true);

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}
