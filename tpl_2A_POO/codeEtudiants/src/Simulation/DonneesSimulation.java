package Simulation;

import Carte.Carte;
import Carte.Case;
import Carte.Incendie;
import Exception.*;
import Robot.Robot;
import Robot.TypeRobot;
import io.LecteurDonnees;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.zip.DataFormatException;

public class DonneesSimulation {

    private Carte carte;
    private HashMap<Case, Incendie> incendies;
    private Queue<Robot> robots;
    private String fichierDonnees;

    public DonneesSimulation(String fichierDonnees) {
        this.incendies = new HashMap<Case, Incendie>();
        this.robots = new LinkedList<Robot>();
        this.fichierDonnees = fichierDonnees;
    }

    public void resetDonnees() {
        try {
            DonneesSimulation nouvellesDonnees = LecteurDonnees.creeDonneesSimulation(this.fichierDonnees);
            this.incendies = nouvellesDonnees.incendies;
            this.carte = nouvellesDonnees.carte;
            this.robots = nouvellesDonnees.robots;
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + this.fichierDonnees + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + this.fichierDonnees + " invalide: " + e.getMessage());
        }

    }

    /**
     * Renvoie la {@link Carte} associée à cette simulation.
     * 
     * @return Carte
     */
    public Carte getCarte() {
        return this.carte;
    }

    /**
     * Associe la {@link Carte} carte aux {@link DonneesSimulation}
     * sur laquelle la méthode est appellée.
     * 
     * @param carte
     */
    public void setCarte(Carte carte) {
        this.carte = carte;
        this.incendies = new HashMap<Case, Incendie>();
        this.robots = new LinkedList<Robot>();
    }

    /**
     * Ajoute un incendie à la {@link Case} position de l'intensité donnée.
     * 
     * @param position
     * @param intensite
     */
    public void addIncendie(Case position, int intensite) {
        Incendie incendie = new Incendie(position, intensite, this);
        this.incendies.put(position, incendie);
    }

    /**
     * Renvoie un {@link Iterator} sur tous les incendies sur la carte. On peut
     * itérer avec les methodes hasNext() et next() de la classe {@link Iterator}.
     * 
     * @return HashMap<Case, Incendie>
     */
    public Iterator<Incendie> getIncendies() {
        return this.incendies.values().iterator();
    }

    /**
     * Renvoie un {@link Iterator} sur les {@link Robot} de la carte.
     * On pourra le parcourir avec la méthode next().
     * 
     * @return Iterator<Robot>
     */
    public Iterator<Robot> getRobots() {
        return this.robots.iterator();
    }

    /**
     * Renvoie l' {@link Incendie} à la {@link Case} position,
     * renvoie null si il n'y a pas d'incendie.
     * 
     * @return Incendie
     */
    public Incendie getIncendie(Case position) {
        return this.incendies.get(position);
    }

    /**
     * Renvoie le premier {@link Robot} à la {@link Case} position.
     * Si il n'y a pas de Robot à cette case, la fonction renvoie null.
     * 
     * !!Fonction qui n'est plus utilisé sauf pour ancien tests!!
     * 
     * @param position
     * @return Robot
     */
    public Robot getRobot(Case position) {
        for (Robot robot : this.robots) {
            // ON suppose que les cases et donc les poitions sont uniques
            if (robot.getPosition() == position)
                return robot;
        }
        return null;
    }

    /**
     * Ajoute un robot du {@link TypeRobot} donné, à la {@link Case} position
     * donnée, avec la {@link #vitesse} donnée.
     * <p>
     * Throws:
     * <p>
     * {@link VitesseIncorrectException} si,
     * pour un type donné, la vitesse est incorrecte.
     * <p>
     * {@link NoSuchElementException} si le type du robot est incorrect.
     * 
     * @param type
     * @param position
     * @param vitesse
     * @throws VitesseIncorrectException
     * @throws NoSuchElementException
     */
    public void addRobot(TypeRobot type, Case position, double vitesse)
            throws VitesseIncorrectException, NoSuchElementException {
        Robot robot = Robot.newRobot(type, position, vitesse, this);
        this.robots.add(robot);
    }
}