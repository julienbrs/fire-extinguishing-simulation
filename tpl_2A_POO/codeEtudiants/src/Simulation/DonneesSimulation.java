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

// For now we don't need anything
// Will have to check how to display 
// In the following part of the subject
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
     * Renvoie la {@link Carte} associé à cette simulation.
     * 
     * @return Carte
     */
    public Carte getCarte() {
        return this.carte;
    }

    /**
     * Associe la {@link Carte} carte aux {@link DonneesSimulation}
     * sur laquelle la methode est appellée.
     * 
     * @param carte
     */
    public void setCarte(Carte carte) {
        this.carte = carte;
        this.incendies = new HashMap<Case, Incendie>();
        this.robots = new LinkedList<Robot>();
    }

    /**
     * Rajoute un incendie à la {@link Case} position, d'une intensité donnée.
     * 
     * @param position
     * @param intensite
     */
    public void addIncendie(Case position, int intensite) {
        Incendie incendie = new Incendie(position, intensite, this);
        this.incendies.put(position, incendie);
    }

    /**
     * Renvoie le HashMap contenant tous les incendies sur la carte.
     * 
     * @return HashMap<Case, Incendie>
     */
    public Iterator<Incendie> getIncendies() {
        return this.incendies.values().iterator();
    }

    /**
     * Renvoie un {@link Iterator} sur les {@link Robot} de la carte.
     * On pourra itérer avec une boucle for, ou avancer avec la
     * méthode next.
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
        /* Returns the fire at the position case, else null */
        return this.incendies.get(position);
    }

    /**
     * Renvoie le premier {@link Robot} à la {@link Case} position.
     * Si il n'y a pas de Robot à cette case, la fonction renvoie null.
     * 
     * @param position
     * @return Robot
     */
    /// NE MARCHE PAS NE MARCHE PAS
    // todo
    // si il y a plusieurs robot, on ne renvoie que le premier, un peu cringe
    public Robot getRobot(Case position) {
        for (Robot robot : this.robots) {
            // We suppose all cases are unique, so position would also be
            // this.carte.getCase(position.getLigne(), position.getColonne())
            if (robot.getPosition() == position)
                return robot;
        }
        // if (this.robots.containsKey(this.carte.getCase(lig, col))) return
        // this.robots.get(this.carte.getCase(lig, col));
        return null;
    }

    /**
     * Supprime l'{@link Incendie} à la {@link Case} position;
     * 
     * @param position
     */
    public void removeIncendie(Case position) {

        if (this.incendies.containsKey(position)) {
            this.incendies.remove(position);
        }
    }

    /**
     * Ajoute un robot du {@link TypeRobot} donnée, à la {@link Case} position,
     * et avec la {@link #vitesse} vitesse donnée.
     * <p>
     * Throws:
     * <p>
     * {@link VitesseIncorrectException} si,
     * pour un type donnée, la vitesse est incorrecte.
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