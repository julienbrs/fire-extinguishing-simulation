package Simulation;

import java.awt.Color;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import Robot.*;
import Strategie.ChefPompier;
import Carte.*;
import gui.GUISimulator;
import gui.Rectangle;
import gui.ImageElement;
import gui.Simulable;
import Events.AffectationIncendiesRobots;
import Events.Avion;
import Events.Evenement;
import Events.PropagationIncendie;

class ComparatorEvenements implements Comparator<Evenement> {

    /**
     * Compare deux evenements selon leur date d'éxecution
     * 
     * @param event1
     * @param event2
     * @return int
     */
    public int compare(Evenement event1, Evenement event2) {
        return Long.compare(event1.getDate(), event2.getDate());
    }
}

public class Simulateur implements Simulable {

    private Case positionAvion;
    private long dateSimulation;
    private PriorityQueue<Evenement> scenario;
    private GUISimulator gui;
    private DonneesSimulation donnees;
    private ChefPompier chef;
    boolean lancerSimulation;
    boolean lancerPropagation;
    private boolean useGif;

    /**
     * Constructeur pour lancer la Simulation sans propagation
     */
    public Simulateur(DonneesSimulation donnees, long dateSimulation) {
        this(donnees, dateSimulation, true, false, true);
    }

    /**
     * Constructeur pour lancer la simulation avec propagation
     */
    public Simulateur(DonneesSimulation donnees, long dateSimulation,
            boolean lancerPropagation) {
        this(donnees, dateSimulation, true, lancerPropagation, true);
    }

    /* Constructeur de base */
    public Simulateur(DonneesSimulation donnees, long dateSimulation, boolean lancerSimulation,
            boolean lancerPropagation) {
        this(donnees, dateSimulation, true, true, true);

    }

    public Simulateur(DonneesSimulation donnees, long dateSimulation, boolean lancerSimulation,
            boolean lancerPropagation, boolean useGif) {
        this.lancerPropagation = lancerPropagation;
        this.lancerSimulation = lancerSimulation;
        Carte carte = donnees.getCarte();

        int tailleCases = carte.getTailleCases();
        int nbColonnes = carte.getNbColonnes();
        int nbLignes = carte.getNbLignes();

        this.positionAvion = carte.getCase(nbLignes / 5, 0);
        GUISimulator gui = new GUISimulator(nbColonnes * 2 * tailleCases, nbLignes * tailleCases,
                new Color(170, 242, 229));
        gui.setSimulable(this);

        this.gui = gui;
        this.donnees = donnees;
        this.dateSimulation = dateSimulation;
        this.scenario = new PriorityQueue<Evenement>(100, new ComparatorEvenements());
        this.chef = new ChefPompier(this, this.donnees);
        this.useGif = useGif;

        if (lancerSimulation)
            this.ajouteEvenement(new AffectationIncendiesRobots(dateSimulation, this, 100));
        if (lancerPropagation)
            this.ajouteEvenement(new PropagationIncendie(60000, this, 60000));
        this.ajouteEvenement(new Avion(100, null, this.positionAvion, this, 100));
        this.draw();
    }

    /**
     * Définie la position de l'avion via une {@link Case}.
     *
     * @param position
     */
    public void setPositionAvion(Case position) {
        this.positionAvion = position;
    }

    /**
     * Renvoie le {@link ChefPompier} de la simulation.
     *
     * @return {@link ChefPompier}
     */
    public ChefPompier getChefPompier() {
        return this.chef;
    }

    /**
     * Renvoie la date courante de la simulation.
     *
     * @return long
     */
    public long getDateCourante() {
        return this.dateSimulation;
    }

    /**
     * Ajoute un {@link Evenement} au scénario.
     *
     * @param {@link Evenement}
     */
    public void ajouteEvenement(Evenement event) {
        scenario.add(event);
    }

    /**
     * Incrémente la date de la simulation.
     * Si il y a des événements à cette date, ils sont exécutés puis supprimés.
     */
    void incrementeDate() {
        dateSimulation++;
        while (scenario.peek() != null && dateSimulation >= scenario.peek().getDate()) {
            // récupère et supprime la tete de la queue
            scenario.poll().execute();
        }
    }

    /**
     * On regarde si la simulation est finie.
     *
     * @return boolean
     */
    public boolean simulationTerminee() {
        return scenario.peek() == null;
    }

    /**
     * Dessine les {@link Robot} à leur position.
     *
     * @param robot
     * @param tailleCases
     * @param gui
     */
    private void drawRobot(Robot robot, int tailleCases, GUISimulator gui) {
        Carte carte = this.donnees.getCarte();
        Case caseRobot = robot.getPosition();
        int centerCol = (carte.getNbColonnes() - 1) * carte.getTailleCases();

        int lig = caseRobot.getLigne();
        int col = caseRobot.getColonne();
        int coordX = centerCol + (col - lig) * tailleCases + tailleCases / 2;
        int coordY = (col + lig) * tailleCases / 2 - tailleCases / 4;

        switch (robot.getType()) {
            case DRONE:
                if (this.useGif) {
                    gui.addGraphicalElement(
                            new ImageElement((int) (coordX + tailleCases * 0.1), (int) (coordY - tailleCases * 0.20),
                                    "assets/robots/drone_" + robot.getDirectionImage() + ".gif",
                                    (int) (tailleCases * 0.85),
                                    (int) (tailleCases * 0.87), null));
                } else {
                    gui.addGraphicalElement(
                            new ImageElement((int) (coordX + tailleCases * 0.1), (int) (coordY - tailleCases * 0.20),
                                    "assets/robots/drone.png",
                                    (int) (tailleCases * 0.85),
                                    (int) (tailleCases * 0.87), null));
                }
                break;
            case PATTES:
                if (this.useGif) {
                    gui.addGraphicalElement(
                            new ImageElement(coordX, coordY,
                                    "assets/robots/pattes_" + robot.getDirectionImage() + ".gif",
                                    tailleCases,
                                    tailleCases, null));
                } else {
                    gui.addGraphicalElement(
                            new ImageElement(coordX, coordY,
                                    "assets/robots/pattes.png",
                                    tailleCases,
                                    tailleCases, null));
                }
                break;
            case ROUES:
                if (this.useGif) {
                    gui.addGraphicalElement(
                            new ImageElement((int) (coordX + tailleCases * 0.2), (int) (coordY - tailleCases * 0.1),
                                    "assets/robots/wheels_" + robot.getDirectionImage() + ".gif",
                                    (int) (tailleCases * 0.6),
                                    (int) (tailleCases * 0.8), null));
                } else {
                    gui.addGraphicalElement(
                            new ImageElement((int) (coordX + tailleCases * 0.2), (int) (coordY - tailleCases * 0.1),
                                    "assets/robots/wheels.png",
                                    (int) (tailleCases * 0.6),
                                    (int) (tailleCases * 0.8), null));
                }
                break;
            case CHENILLES:
                if (this.useGif) {
                    gui.addGraphicalElement(
                            new ImageElement(coordX, (int) (coordY + tailleCases * 0.15),
                                    "assets/robots/tracks_" + robot.getDirectionImage() + ".gif",
                                    (int) (tailleCases),
                                    (int) (tailleCases * 0.625), null));
                } else {
                    gui.addGraphicalElement(
                            new ImageElement(coordX, (int) (coordY + tailleCases * 0.15),
                                    "assets/robots/tracks.png",
                                    (int) (tailleCases),
                                    (int) (tailleCases * 0.625), null));
                }
                break;
            default:
                // Robot mystère
                gui.addGraphicalElement(
                        new ImageElement(coordX, coordY, "assets/robots/robobo.gif", tailleCases, tailleCases, null));
                break;
        }
    }

    /**
     * Dessine les {@link Incendie} à leur position, les {@link Case} en fonction
     * de leur nature et les {@link Robot} à leur position.
     *
     * @param incendie
     * @param tailleCases
     * @param gui
     */
    private void draw() {
        gui.reset(); // clear window
        Carte carte = donnees.getCarte();
        int centerCol = (carte.getNbColonnes() - 1) * carte.getTailleCases();
        Case caseCourante = null;
        int tailleCases = carte.getTailleCases();
        Incendie incendie = null;
        int intensiteForTailleMax = 30000;
        int intensiteforTailleMin = 5000;
        NatureTerrain nature = null;
        String suffixe = this.useGif ? ".gif" : ".png";

        /* On parcourt la carte */
        for (int lig = 0; lig < carte.getNbLignes(); lig++) {
            for (int col = 0; col < carte.getNbColonnes(); col++) {
                caseCourante = carte.getCase(lig, col);
                incendie = donnees.getIncendie(caseCourante);
                nature = caseCourante.getNature();

                int posX = centerCol + (col - lig) * tailleCases;
                int posY = (col + lig) * tailleCases / 2;
                /* On crée les assets aléatoirement, qui rend unique chaque carte générée */
                int hashLots = caseCourante.getColonne() * 3 + caseCourante.getLigne() * 9 + carte.hashCode();

                /* On dessine la case selon sa nature */
                switch (nature) {
                    case TERRAIN_LIBRE:
                        gui.addGraphicalElement(
                                new ImageElement(posX, posY,
                                        "assets/nature/grass.png", tailleCases * 2,
                                        tailleCases, null));
                        break;
                    case ROCHE:
                        gui.addGraphicalElement(
                                new ImageElement(posX, posY,
                                        "assets/nature/dirt" + Integer.toString(hashLots % 4) + ".png", tailleCases * 2,
                                        tailleCases, null));
                        break;
                    case EAU:
                        gui.addGraphicalElement(
                                new ImageElement(posX, posY,
                                        "assets/nature/water.png", tailleCases * 2, tailleCases, null));
                        break;
                    case HABITAT:
                        gui.addGraphicalElement(
                                new ImageElement(posX, posY,
                                        "assets/nature/lot" + Integer.toString(hashLots % 7) + ".png", tailleCases * 2,
                                        tailleCases,
                                        null));
                        break;
                    case FORET:
                        gui.addGraphicalElement(
                                new ImageElement(posX, posY,
                                        "assets/nature/forest" + Integer.toString(hashLots % 2) + ".png",
                                        tailleCases * 2,
                                        tailleCases, null));
                        break;
                    default:
                        // Ca n'arrive pas parceque tous les NatureTerrain sont filtrés à la creation de
                        // la carte
                        break;
                }

                /* On dessine l'incendie s'il y en a un */
                if (incendie != null && !incendie.estEteint()) {
                    /* On calcule la taille de l'incendie en fonction de son intensité */
                    double ratio = (double) (incendie.getIntensite()
                            / (intensiteForTailleMax - intensiteforTailleMin * 1.5));
                    if (ratio > 1)
                        ratio = 0.9;
                    else if (ratio < 0.5)
                        ratio = 0.5;
                    int tailleFeu = (int) (ratio * tailleCases);

                    /* On ajuste sa position pour qu'il soit centré */
                    posY = (col + lig) * tailleCases / 2 - tailleCases / 4
                            + (int) ((0.9 - ratio) / 2 * tailleCases);
                    posX = centerCol + (col - lig) * tailleCases + tailleCases / 2
                            + (int) ((1 - ratio) / 2 * tailleCases);

                    gui.addGraphicalElement(new ImageElement(
                            posX, posY, "assets/nature/fire.gif", tailleFeu, tailleFeu, null));

                }
            }
        }

        /* On dessine les robots grâce à la méthode drawRobot() */
        for (Iterator<Robot> robots = donnees.getRobots(); robots.hasNext();) {
            Robot robot = robots.next();
            drawRobot(robot, tailleCases, gui);
        }

        /*
         * On dessine un petit avion décoratif qui vole au-dessus de la carte au début
         * de la simulation
         */
        if (this.positionAvion != null) {
            int lig = this.positionAvion.getLigne();
            int col = this.positionAvion.getColonne();
            gui.addGraphicalElement(new ImageElement(centerCol + (col - lig) * tailleCases - tailleCases / 2,
                    (col + lig) * tailleCases / 2 - tailleCases / 4, "assets/nature/airplane.png", tailleCases,
                    tailleCases, null));
        }
    }

    /**
     * Renvoie les {@link DonneesSimulation} de la simulation.
     *
     * @return
     */
    public DonneesSimulation getDonnees() {
        return this.donnees;
    }

    /**
     * On redéfinit la méthode {@link #next()} de {@link Simulateur} pour
     * incrémenter la date de la simulation
     */
    @Override
    public void next() {
        incrementeDate();
        draw();
    }

    /**
     * On redéfinit la méthode {@link #restart()} de {@link Simulateur} pour
     * remettre la date de la simulation à 0 et réinitialiser toute les données de
     * simulation.
     */
    @Override
    public void restart() {
        this.donnees.resetDonnees();
        this.dateSimulation = 0;
        this.chef = new ChefPompier(this, this.donnees);
        this.scenario = new PriorityQueue<Evenement>(100, new ComparatorEvenements());
        if (lancerSimulation)
            this.ajouteEvenement(new AffectationIncendiesRobots(dateSimulation, this, 100));
        if (this.lancerPropagation)
            this.ajouteEvenement(new PropagationIncendie(60000, this, 60000));

        Carte carte = this.donnees.getCarte();
        this.positionAvion = carte.getCase(carte.getNbLignes() / 5, 0);
        this.ajouteEvenement(new Avion(100, null, this.positionAvion, this, 100));
        draw();
    }
}
