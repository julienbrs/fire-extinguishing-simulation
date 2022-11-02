package io;
import Carte.*;
import Robot.Robot;
import Robot.TypeRobot;
import Simulation.DonneesSimulation;
import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

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

        DonneesSimulation donnees = new DonneesSimulation();
        LecteurDonnees lecteur = new LecteurDonnees(fichierDonnees);
        Carte carte = lecteur.creeCarte(donnees);
        donnees.setCarte(carte);
        lecteur.creeIncendies(donnees);
        lecteur.creeRobots(donnees);
        scanner.close();

        return donnees;
        // System.out.println("\n == Lecture terminee");
    }

    public static void lire(String fichierDonnes) throws DataFormatException, FileNotFoundException
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
    private Carte creeCarte(DonneesSimulation donnees) throws DataFormatException {
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
            return new Carte(tailleCases, nbLignes, nbColonnes, cases, donnees);
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
    private HashMap<Case, Incendie> creeIncendies(DonneesSimulation donnees) throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbIncendies = scanner.nextInt();
            HashMap<Case, Incendie> incendies = new HashMap<Case, Incendie>();
            // System.out.println("Nb d'incendies = " + nbIncendies);
            for (int i = 0; i < nbIncendies; i++) {
                creeIncendie(donnees);
            }

            return incendies;
        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbIncendies");
        }
    }


    /**
     * Lit et affiche les donnees du i-eme incendie.
     * @param i
     */
    private void creeIncendie(DonneesSimulation donnees) throws DataFormatException {
        ignorerCommentaires();

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();
            // if (intensite <= 0) {
            //     throw new DataFormatException("incendie " + i
            //             + "nb litres pour eteindre doit etre > 0");
            // }
            verifieLigneTerminee();
            
            donnees.addIncendie(donnees.getCarte().getCase(lig, col), intensite);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format d'incendie invalide. "
                    + "Attendu: ligne colonne intensite");
        }
    }


    /**
     * Lit et affiche les donnees des robots.
     */
    private void creeRobots(DonneesSimulation donnees) throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbRobots = scanner.nextInt();
            // System.out.println("Nb de robots = " + nbRobots);
            for (int i = 0; i < nbRobots; i++) {
                creeRobot(donnees);
            }
        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbRobots");
        }
    }


    /**
     * Lit et affiche les donnees du i-eme robot.
     * @param i
     */
    private void creeRobot(DonneesSimulation donnees) throws DataFormatException {
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

            double vitesse;
            if (s == null) {
                // System.out.print("valeur par defaut");
                vitesse = Double.NaN;
            } else {
                vitesse = Integer.parseInt(s);
                // System.out.print(vitesse);
            }
            donnees.addRobot(TypeRobot.valueOf(type), donnees.getCarte().getCase(lig, col), vitesse);

            verifieLigneTerminee();

            // System.out.println();
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
