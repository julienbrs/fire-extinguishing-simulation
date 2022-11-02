package Test;
import io.LecteurDonnees;
import Simulation.DonneesSimulation;
import Carte.Carte;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

public class TestLecteurDonnees {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            System.out.println("Lecture des donnees...");
            DonneesSimulation donnees = LecteurDonnees.creeDonneesSimulation(args[0]);
            System.out.println("Lecture des donnees terminee");
            Carte carte = donnees.getCarte();
            System.out.println("Affichage de la carte...");
            System.out.println(carte);


        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}