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
    private static void printWarning(String s) {
        System.out.println("\033[1m\033[93m" + s + "\033[0m\033[0m");
    }

    public static void main(String[] args) {
        DonneesSimulation donnees;
        Carte carte;
        try {
            donnees = LecteurDonnees.creeDonneesSimulation(args[0]);
            carte = donnees.getCarte();
            Scanner scanner = new Scanner(System.in);
            int nbRobots = 0;
            int nbIncendies = 0;

            boolean lu = false;
            while (!lu) {
                try {
                    System.out.println(carte);
                    System.out.println("Nombre incendies à ajouter?: ");
                    nbIncendies = scanner.nextInt();
                    if (nbIncendies < 0)
                        throw new Exception("Les incendies doivent être 0 ou plus!");
                    lu = true;
                } catch (InputMismatchException e) {
                    System.out.println("Le nombre d'incendie doit être un entier.");
                    scanner.next();
                } catch (Exception e) {
                    printWarning(e.getMessage());
                }
            }
            System.out.println(
                    "Inserez les cordonnees de vos incendie un par un séparées par un espace. Ligne et puis colonne");
            for (int i = 1; i <= nbIncendies; i++) {
                try {
                    System.out.print("Ligne Incendie n°" + i + "(0<lig<7): ");
                    int lig = scanner.nextInt();
                    System.out.print("Colonne Incendie n°" + i + "(0<col<7): ");
                    int col = scanner.nextInt();

                    // On gère les erreurs
                    if (lig < 0 || lig > 7 || col < 0 || col > 7)
                        throw new Exception("Les lignes et colonnes sont comprises entre 0 et 7!");

                    System.out.print("Intensité? ");
                    int intensite = scanner.nextInt();

                    if (intensite <= 0)
                        throw new Exception("L'intensité doit être superieure à 0!");

                    donnees.addIncendie(carte.getCase(lig, col), intensite);
                    System.out.println(carte);
                } catch (InputMismatchException e) {
                    // Si erreur, on refait
                    printWarning("Inserez QUE des entiers!");
                    scanner.next();
                    i--;
                } catch (Exception e) {
                    printWarning(e.getMessage());
                    i--;
                }
            }

            lu = false;
            while (!lu) {
                try {
                    System.out.println("Nombre robots à ajouter?: ");
                    nbRobots = scanner.nextInt();
                    if (nbRobots < 1)
                        throw new Exception("Il doit il y avoir au moins un robot!");
                    lu = true;
                } catch (InputMismatchException e) {
                    System.out.println("Le nombre de robots doit être un entier.");
                    scanner.next();
                } catch (Exception e) {
                    printWarning(e.getMessage());
                }
            }

            System.out.println(
                    "Inserez les cordonnees de vos robots un par un séparées par un espace. Ligne et puis colonne");
            for (int i = 1; i <= nbRobots; i++) {
                try {
                    System.out.print("Ligne Robot n°" + i + " (0 < lig < 7): ");
                    int lig = scanner.nextInt();
                    System.out.print("Colonne Robot n°" + i + "(0 < col < 7): ");
                    int col = scanner.nextInt();
                    // Gestion input
                    if (lig < 0 || lig > 7 || col < 0 || col > 7)
                        throw new Exception("Les lignes et colonnes sont comprises entre 0 et 7!");

                    System.out.println("Type robot? (PATTES/CHENILLES/ROUES/DRONE) ");
                    String typeString = scanner.next();
                    TypeRobot type = TypeRobot.valueOf(typeString);
                    donnees.addRobot(type, carte.getCase(lig, col), Double.NaN);

                    System.out.println(carte);
                } catch (InputMismatchException e) {
                    // Si erreur, on refait
                    printWarning("Inserez QUE des entiers!");
                    scanner.next();
                    i--;
                } catch (IllegalArgumentException e) {
                    printWarning("Le type de robot n'existe pas!");
                    i--;
                } catch (Exception e) {
                    printWarning(e.getMessage());
                    i--;
                }
            }
            Simulateur simulateur = new Simulateur(donnees, 0);
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Aucun robot de ce type existe");
        }

        System.out.println("Test passé!");
    }
}