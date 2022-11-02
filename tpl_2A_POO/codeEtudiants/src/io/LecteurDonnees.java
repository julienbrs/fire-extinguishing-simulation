package io;
import Carte.*;
import Robot.Robot;
import Robot.TypeRobot;
import Simulation.DonneesSimulation;
import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.HashMap; // import the HashMap class

// import org.jcp.xml.dsig.internal.dom.DOMReference;



/**
 * Lecteur de cartes au format spectifié dans le sujet.
 * Les données sur les cases, robots puis incendies sont lues dans le fichier,
 * puis simplement affichées.
 * A noter: pas de vérification sémantique sur les valeurs numériques lues.
 *
 * IMPORTANT:
 *
 * Cette classe ne fait que LIRE les infos et les afficher.
 * A vous de modifier ou d'ajouter des méthodes, inspirées de celles présentes
 * (ou non), qui CREENT les objets au moment adéquat pour construire une
 * instance de la classe DonneesSimulation à partir d'un fichier.
 *
 * Vous pouvez par exemple ajouter une méthode qui crée et retourne un objet
 * contenant toutes les données lues:
 *    public static DonneesSimulation creeDonnees(String fichierDonnees);
 * Et faire des méthode creeCase(), creeRobot(), ... qui lisent les données,
 * créent les objets adéquats et les ajoutent ds l'instance de
 * DonneesSimulation.
 */
public class LecteurDonnees {


    /**
     * Lit et affiche le contenu d'un fichier de donnees (cases,
     * robots et incendies).
     * Ceci est méthode de classe; utilisation:
     * LecteurDonnees.lire(fichierDonnees)
     * @param fichierDonnees nom du fichier à lire
     */
    public static DonneesSimulation creeDonneesSimulation(String fichierDonnees)
        throws FileNotFoundException, DataFormatException {

        LecteurDonnees lecteur = new LecteurDonnees(fichierDonnees);
        Carte carte = lecteur.creeCarte();
        /* crée les incendies */
        HashMap<Case, Incendie> incendies = lecteur.creeIncendies(carte);
        Robot[] robots = lecteur.creeRobots(carte);
        scanner.close();

        return new DonneesSimulation(carte, incendies, robots);
        // System.out.println("\n == Lecture terminee");
    }

    public static void lire(String fichierDonnes)
    {
        // DonneesSimulation donnees = creeDonneesSimulation(fichierDonnes);
        // Print donnees eventually (this is so that testlecteurdonnees can compile)
    }
    // public static DonneesSimulation creeDonnees(String fichierDonnes);


    // Tout le reste de la classe est prive!

    private static Scanner scanner;

    /**
     * Constructeur prive; impossible d'instancier la classe depuis l'exterieur
     * @param fichierDonnees nom du fichier a lire
     */
    private LecteurDonnees(String fichierDonnees)
        throws FileNotFoundException {
        scanner = new Scanner(new File(fichierDonnees));
        scanner.useLocale(Locale.US);
    }

    /**
     * Lit et affiche les donnees de la carte.
     * @throws ExceptionFormatDonnees
     */
    private Carte creeCarte() throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbLignes = scanner.nextInt();
            int nbColonnes = scanner.nextInt();
            Case[][] cases = new Case[nbLignes][nbColonnes];
            int tailleCases = scanner.nextInt();	// en m
            System.out.println("Carte " + nbLignes + "x" + nbColonnes
                    + "; taille des cases = " + tailleCases);

            for (int lig = 0; lig < nbLignes; lig++) {
                for (int col = 0; col < nbColonnes; col++) {
                    cases[lig][col] = creeCase(lig, col);
                }
            }
            return new Carte(tailleCases, nbLignes, nbColonnes, cases);
        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbLignes nbColonnes tailleCases");
        }
        // une ExceptionFormat levee depuis lireCase est remontee telle quelle
    }




    /**
     * Lit et affiche les donnees d'une case.
     */
    private Case creeCase(int lig, int col) throws DataFormatException {
        ignorerCommentaires();
        NatureTerrain nature;
        String chaineNature = new String();
        //		NatureTerrain nature;

        try {
            chaineNature = scanner.next();
            // si NatureTerrain est un Enum, vous pouvez recuperer la valeur
            // de l'enum a partir d'une String avec:
            //			NatureTerrain nature = NatureTerrain.valueOf(chaineNature);
            verifieLigneTerminee();

            nature = NatureTerrain.valueOf(chaineNature);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de case invalide. "
                    + "Attendu: nature altitude [valeur_specifique]");
        } catch (IllegalArgumentException e) {
            throw new DataFormatException("Nature du terrain invalide");
        }
        Case nouvelleCase = new Case(lig, col, nature);

        return nouvelleCase;
    }


    /**
     * Lit et affiche les donnees des incendies.
     */
    private HashMap<Case, Incendie> creeIncendies(Carte carte) throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbIncendies = scanner.nextInt();
            /* hashmap pour stocker les incendies */
            HashMap<Case, Incendie> incendies = new HashMap<Case, Incendie>();
            
            // System.out.println("Nb d'incendies = " + nbIncendies);
            for (int i = 0; i < nbIncendies; i++) {
                /* on crée un nouvel incendie qu'on ajoute à la hashmap */
                Incendie incendie = creeIncendie(carte);
                incendies.put(incendie.getCase(), incendie);
            }

            return incendies;
        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbIncendies");
        }
    }


    /*
     * Lit et affiche les donnees de l'incendie.
     */
    private Incendie creeIncendie(Carte carte) throws DataFormatException {
        ignorerCommentaires();

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();
            if (intensite <= 0) {
                throw new DataFormatException("incendie à la case " + lig + " " + col
                        + "nb litres pour eteindre doit etre > 0");
            }
            verifieLigneTerminee();
            
            return new Incendie(carte.getCase(lig, col), intensite);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format d'incendie invalide. "
                    + "Attendu: ligne colonne intensite");
        }
    }


    /**
     * Lit et affiche les donnees des robots.
     */
    private Robot[] creeRobots(Carte carte) throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbRobots = scanner.nextInt();
            Robot[] robots = new Robot[nbRobots];
            // System.out.println("Nb de robots = " + nbRobots);
            for (int i = 0; i < nbRobots; i++) {
                robots[i] = creeRobot(carte);
            }
            return robots;
        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbRobots");
        }
    }


    /**
     * Lit et affiche les donnees du i-eme robot.
     * @param i
     */
    private Robot creeRobot(Carte carte) throws DataFormatException {
        ignorerCommentaires();
        // System.out.print("Robot " + i + ": ");

        try {
            Robot robot;
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            // System.out.print("position = (" + lig + "," + col + ");");
            String type = scanner.next();

            // System.out.print("\t type = " + type);


            // lecture eventuelle d'une vitesse du robot (entier)
            // System.out.print("; \t vitesse = ");
            String s = scanner.findInLine("(\\d+)");	// 1 or more digit(s) ?
            // pour lire un flottant:    ("(\\d+(\\.\\d+)?)");

            if (s == null) {
                // System.out.print("valeur par defaut");
                robot = Robot.newRobot(TypeRobot.valueOf(type), carte.getCase(lig, col));
            } else {
                int vitesse = Integer.parseInt(s);
                robot = Robot.newRobot(TypeRobot.valueOf(type), carte.getCase(lig, col), vitesse);
                // System.out.print(vitesse);
            }
            verifieLigneTerminee();

            // System.out.println();
            return robot;

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de robot invalide. "
                    + "Attendu: ligne colonne type [valeur_specifique]");
        }
    }



    /** Ignore toute (fin de) ligne commencant par '#' */
    private void ignorerCommentaires() {
        while(scanner.hasNext("#.*")) {
            scanner.nextLine();
        }
    }

    /**
     * Verifie qu'il n'y a plus rien a lire sur cette ligne (int ou float).
     * @throws ExceptionFormatDonnees
     */
    private void verifieLigneTerminee() throws DataFormatException {
        if (scanner.findInLine("(\\d+)") != null) {
            throw new DataFormatException("format invalide, donnees en trop.");
        }
    }
}
