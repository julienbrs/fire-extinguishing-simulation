package Test;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

import Carte.*;
import Exception.VitesseIncorrectException;
import Robot.TypeRobot;
import Simulation.DonneesSimulation;
import Simulation.Simulateur;
import io.LecteurDonnees;

public class TestStrategieInteractif {
    public static void main(String[] args) {
        DonneesSimulation donnees;
        Carte carte;
        try {
            donnees = LecteurDonnees.creeDonneesSimulation(args[0]);
            carte = donnees.getCarte();
            Scanner scanner = new Scanner(System.in);
            System.out.println(carte);
            int nbRobots = 0;
            int nbIncendies = 0;
            try {
                System.out.println("Nombre incendies à ajouter?: ");
                nbIncendies = scanner.nextInt();

                System.out.println(
                        "Inserez les cordonnees de vos incenndie un par un séparées par un espace. Ligne et puis colonne");
                for (int i = 1; i <= nbIncendies; i++) {
                    System.out.print("Incendie n°" + i + ": ");
                    int lig = scanner.nextInt();
                    int col = scanner.nextInt();
                    System.out.print("Intensité? ");
                    int intensite = scanner.nextInt();
                    donnees.addIncendie(carte.getCase(lig, col), intensite);
                    System.out.println(carte);
                }

                System.out.println("Nombre robots à ajouter?: ");
                nbRobots = scanner.nextInt();

                System.out.println(
                        "Inserez les cordonnees de vos robots un par un séparées par un espace. Ligne et puis colonne");
                for (int i = 1; i <= nbRobots; i++) {
                    System.out.print("Robot n°" + i + ": ");
                    int lig = scanner.nextInt();
                    int col = scanner.nextInt();
                    System.out.println("Type robot? (PATTES/CHENILLES/ROUES/DRONE) ");
                    String typeString = scanner.next();
                    TypeRobot type = TypeRobot.valueOf(typeString);
                    donnees.addRobot(type, carte.getCase(lig, col), Double.NaN);
                    System.out.println(carte);
                }

            } catch (InputMismatchException e) {
                System.out.println("Inserez QUE des entiers!");
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            } catch (VitesseIncorrectException e) {
                // ne va pas arriver
            }

            Simulateur simulateur = new Simulateur(donnees, 0);

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("aucun robot de ce type existe");
        }

        System.out.println("Test passé!");
    }
}