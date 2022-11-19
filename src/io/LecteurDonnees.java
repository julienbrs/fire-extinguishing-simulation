package io;

import Carte.*;
import Exception.VitesseIncorrectException;
import Robot.Robot;
import Robot.TypeRobot;
import Simulation.DonneesSimulation;
import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.HashMap;


/**
 * Lecteur de cartes au format specifié dans le sujet.
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
 * public static DonneesSimulation creeDonnees(String fichierDonnees);
 * Et faire des méthode creeCase(), creeRobot(), ... qui lisent les données,
 * créent les objets adéquats et les ajoutent ds l'instance de
 * DonneesSimulation.
 */
public class LecteurDonnees {

    /**
     * Lit et affiche le contenu d'un fichier de données (cases,
     * robots et incendies).
     * Ceci est une méthode de classe.
     * <p>
     * Utilisation:
     * LecteurDonnees.lire(fichierDonnees)
     * 
     * @param fichierDonnees Nom du fichier à lire
     * @return {@link DonneesSimulation} lues
     */
    public static DonneesSimulation creeDonneesSimulation(String fichierDonnees)
            throws FileNotFoundException, DataFormatException {

        DonneesSimulation donnees = new DonneesSimulation(fichierDonnees);
        LecteurDonnees lecteur = new LecteurDonnees(fichierDonnees);
        Carte carte = lecteur.creeCarte(donnees);
        donnees.setCarte(carte);
        lecteur.creeIncendies(donnees);
        lecteur.creeRobots(donnees);
        scanner.close();

        return donnees;
    }

    private static Scanner scanner;

    /**
     * Constructeur privé; impossible d'instancier la classe depuis l'extérieur
     * 
     * @param fichierDonnees Nom du fichier à lire
     * @throws FileNotFoundException
     */
    private LecteurDonnees(String fichierDonnees)
            throws FileNotFoundException {
        scanner = new Scanner(new File(fichierDonnees));
        scanner.useLocale(Locale.US);
    }

    /**
     * Crée et renvoie la carte grâce au scanner.
     * 
     * @throws ExceptionFormatDonnees
     * @return Carte
     */
    private Carte creeCarte(DonneesSimulation donnees) throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbLignes = scanner.nextInt();
            int nbColonnes = scanner.nextInt();
            Case[][] cases = new Case[nbLignes][nbColonnes];
            int tailleCases = scanner.nextInt(); // en m
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
    }

    /**
     * Crée et renvoie une case à la position (ligne, colonne) grâce au scanner.
     * 
     * @throws DataFormatException
     * @return Case
     */
    private Case creeCase(int lig, int col) throws DataFormatException {
        ignorerCommentaires();
        NatureTerrain nature;
        String chaineNature = new String();

        try {
            chaineNature = scanner.next();
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
     * Crée et renvoie une Hashmap<Case, Incendie> grâce au scanner, cette Hashmap
     * représentant les incendies.
     * On crée les incendies via la méthode {@link #creeIncendie(DonneesSimulation)
     * creeIncendie()}.
     * 
     * @throws DataFormatException
     * @return HashMap<Case, Incendie>
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
     * Crée un {@link Incendie} grâce au scanner et l'ajoute aux
     * {@link DonneesSimulation}.
     * 
     * @param donnees
     * @throws DataFormatException
     * @return void
     */
    private void creeIncendie(DonneesSimulation donnees) throws DataFormatException {
        ignorerCommentaires();

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();
            verifieLigneTerminee();

            donnees.addIncendie(donnees.getCarte().getCase(lig, col), intensite);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format d'incendie invalide. "
                    + "Attendu: ligne colonne intensite");
        }
    }

    /**
     * Lis les robots du fichier source et les crée via la méthode
     * {@link #creeRobot(DonneesSimulation)}.
     * 
     * @throws DataFormatException
     * @return void
     */
    private void creeRobots(DonneesSimulation donnees) throws DataFormatException {
        ignorerCommentaires();
        int nbRobots = scanner.nextInt();
        for (int i = 0; i < nbRobots; i++) {
            creeRobot(donnees);
        }
    }

    /**
     * Crée un {@link Robot} avec les bons paramètres lus dans le fichier source et
     * les ajoute aux {@link DonneesSimulation}.
     * 
     * @throws DataFormatException
     * @return void
     */
    private void creeRobot(DonneesSimulation donnees) throws DataFormatException {
        ignorerCommentaires();

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            String type = scanner.next();
            String s = scanner.findInLine("(\\d+)"); // 1 or more digit(s) ?
            // pour lire un flottant: ("(\\d+(\\.\\d+)?)");

            double vitesse;
            if (s == null) {
                vitesse = Double.NaN;
            } else {
                vitesse = Integer.parseInt(s);
            }

            donnees.addRobot(TypeRobot.valueOf(type), donnees.getCarte().getCase(lig, col), vitesse);

            verifieLigneTerminee();

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de robot invalide. "
                    + "Attendu: ligne colonne type [valeur_specifique]");
        } catch (VitesseIncorrectException e) {
            throw new DataFormatException(e.getMessage());
        }
    }

    /** Ignore toute (fin de) ligne commencant par '#' */
    private void ignorerCommentaires() {
        while (scanner.hasNext("#.*")) {
            scanner.nextLine();
        }
    }

    /**
     * Vérifie qu'il n'y ai plus rien a lire sur cette ligne (int ou float).
     * Jette {@link DataFormatException} s'il y a trop de données sur une même
     * ligne.
     * 
     * @throws DataFormatException
     * @return void
     */
    private void verifieLigneTerminee() throws DataFormatException {
        if (scanner.findInLine("(\\d+)") != null) {
            throw new DataFormatException("format invalide, donnees en trop.");
        }
    }
}
