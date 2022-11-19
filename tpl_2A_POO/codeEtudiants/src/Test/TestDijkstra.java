package Test;

import io.LecteurDonnees;
import Strategie.Chemin;
import Simulation.DonneesSimulation;
import Strategie.Graphe;
import Carte.Carte;
import Carte.Case;
import Robot.Robot;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class TestDijkstra {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            DonneesSimulation donnees = LecteurDonnees.creeDonneesSimulation(args[0]);
            System.out.println("Lecture des donnees terminee");
            Carte carte = donnees.getCarte();
            System.out.println(carte);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Type the line and column of the case you want to go to (<lig> <col>): ");
            int lig = scanner.nextInt();
            int col = scanner.nextInt();

            Iterator<Robot> robots = donnees.getRobots();
            Robot robot = robots.next();
            Graphe graphe = new Graphe(carte, robot);

            if (robot == null) {
                System.out.println("Aucun robot trouvé");
                System.exit(1);
            }
            System.out.println(" test " + lig + col);
            graphe.calculeChemins();
            Chemin chemin = graphe.cheminDestination(carte.getCase(lig, col));

            if (chemin == null) {
                System.out.print("Il n'existe pas de chemin vers la case! \n");
                return;
            }
            Iterator<Case> iteratorChemin = chemin.getChemin();
            while (iteratorChemin.hasNext()) {
                System.out.println("Press enter for next step");
                scanner.nextLine();
                Case courante = iteratorChemin.next();
                robot.moveRobot(courante);
                System.out.println(carte);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}