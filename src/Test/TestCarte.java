package Test;

import Carte.*;
import Simulation.DonneesSimulation;
import io.LecteurDonnees;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

public class TestCarte {
    public static void main(String[] args) {
        DonneesSimulation donnees;
        Carte carte;
        try {
            donnees = LecteurDonnees.creeDonneesSimulation(args[0]);
            carte = donnees.getCarte();
            System.out.println(carte.toString());
            // Voisin NORD
            assert (!carte.voisinExiste(carte.getCase(0, 0), Direction.NORD));
            // Voisin EST
            assert (!carte.voisinExiste(carte.getCase(0, carte.getNbColonnes()), Direction.EST));
            // Voisin SUD
            assert (!carte.voisinExiste(carte.getCase(carte.getNbLignes(), 0), Direction.SUD));
            // Voisin OUEST
            assert (!carte.voisinExiste(carte.getCase(0, 0), Direction.OUEST));

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}