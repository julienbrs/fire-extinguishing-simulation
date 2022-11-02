package Test;
import io.LecteurDonnees;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

public class TestLecteurDonnees {

    public static void main(String[] args) {
        console.log("TestLecteurDonnees");
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            console.log("Lecture des donnees depuis le fichier " + args[0]);
            LecteurDonnees.creeDonneesSimulation(args[0]);
            console.log("Lecture terminée");
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}