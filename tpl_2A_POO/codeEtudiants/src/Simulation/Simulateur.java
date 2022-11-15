package Simulation;

import java.awt.Color;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

import Robot.*;
import Strategie.ChefPompier;
import Carte.*;
import gui.GUISimulator;
import gui.Rectangle;
import gui.ImageElement;
import gui.Simulable;
import gui.Text;
import Events.AffectationIncendiesRobots;
import Events.Avion;
import Events.Evenement;

class ComparatorEvenements implements Comparator<Evenement> {
    public int compare(Evenement o1, Evenement o2) {

        Evenement event1 = (Evenement) o1;
        Evenement event2 = (Evenement) o2;

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

    public Simulateur(DonneesSimulation donnees, long dateSimulation) {
        Carte carte = donnees.getCarte();

        int tailleCases = carte.getTailleCases();
        int nbColonnes = carte.getNbColonnes();
        int nbLignes = carte.getNbLignes();

        this.positionAvion = carte.getCase(nbLignes / 5, 0);
        GUISimulator gui = new GUISimulator(nbColonnes * 2 * tailleCases, nbLignes * tailleCases, null);
        gui.setSimulable(this);

        this.gui = gui;
        this.donnees = donnees;
        this.dateSimulation = dateSimulation;
        this.scenario = new PriorityQueue<Evenement>(100, new ComparatorEvenements());
        this.chef = new ChefPompier(this, this.donnees);
        // todo 100?
        this.ajouteEvenement(new AffectationIncendiesRobots(dateSimulation, null, this, 100));
        this.ajouteEvenement(new Avion(100, null, this.positionAvion, this, 100));
        // Initialisation des couleurs
        this.draw();
    }

    public void setPositionAvion(Case position) {
        this.positionAvion = position;
    }

    public ChefPompier getChefPompier() {
        return this.chef;
    }

    public long getDateCourante() {
        return this.dateSimulation;
    }

    public void ajouteEvenement(Evenement e) {
        scenario.add(e);
    }

    void incrementeDate() {
        dateSimulation++;
        while (scenario.peek() != null && dateSimulation >= scenario.peek().getDate()) {
            // poll : récupère et supprime la tete de la queue
            scenario.poll().execute();
        }
    }

    // todo
    // return just the condition
    public boolean simulationTerminee() {
        if (scenario.peek() == null) {
            return true;
        }
        return false;
    }

    private Color NatureTerrainToColor(NatureTerrain nature) {
        switch (nature) {
            case EAU:
                return new Color(44, 163, 221);// (212, 241, 249);
            case FORET:
                return new Color(31, 61, 12);
            case HABITAT:
                return new Color(149, 131, 105);
            case ROCHE:
                return new Color(90, 77, 65);
            case TERRAIN_LIBRE:
                return new Color(141, 199, 64);// (144, 238, 144);
            default:
                return Color.BLACK;
        }
    }

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
                gui.addGraphicalElement(
                        new ImageElement(coordX, coordY, "assets/drone.gif", tailleCases, tailleCases, null));
                break;
            case PATTES:
                gui.addGraphicalElement(
                        new ImageElement(coordX, coordY,
                                "assets/pattes_" + robot.getDirectionImage() + ".gif",
                                tailleCases,
                                tailleCases, null));
                break;
            case ROUES:
                gui.addGraphicalElement(
                        new ImageElement((int) (coordX * 1.03), (int) (coordY * 1),
                                "assets/wheel.gif",
                                (int) (tailleCases * 0.6),
                                (int) (tailleCases * 0.8), null));
                break;
            case CHENILLES:
                gui.addGraphicalElement(
                        new ImageElement((int) (coordX * 1.03), (int) (coordY * 1.02),
                                "assets/tracks_" + robot.getDirectionImage() + ".gif",
                                (int) (tailleCases * 0.688),
                                (int) (tailleCases * 0.8), null));
                break;
            default:
                gui.addGraphicalElement(
                        new ImageElement(coordX, coordY, "assets/robobo.gif", tailleCases, tailleCases, null));
                break;
        }
    }

    private void draw() {
        gui.reset(); // clear window
        Carte carte = donnees.getCarte();
        int centerCol = (carte.getNbColonnes() - 1) * carte.getTailleCases();
        Case caseCourante = null;
        int tailleCases = carte.getTailleCases();
        Incendie incendie = null;
        NatureTerrain nature = null;
        for (int lig = 0; lig < carte.getNbLignes(); lig++) {
            for (int col = 0; col < carte.getNbColonnes(); col++) {
                caseCourante = carte.getCase(lig, col);
                incendie = donnees.getIncendie(caseCourante);
                nature = caseCourante.getNature();

                int hashLots = caseCourante.getColonne() * 3 + caseCourante.getLigne() * 9 + carte.hashCode();

                switch (nature) {
                    case TERRAIN_LIBRE:
                        // drawTerrainLibre(caseCourante, tailleCases, gui);
                        gui.addGraphicalElement(
                                new ImageElement(centerCol + (col - lig) * tailleCases,
                                        (col + lig) * tailleCases / 2,
                                        "assets2/grass.png", tailleCases * 2,
                                        tailleCases, null));
                        break;
                    case ROCHE:
                        gui.addGraphicalElement(
                                new ImageElement(centerCol + (col - lig) * tailleCases,
                                        (col + lig) * tailleCases / 2,
                                        "assets2/dirt" + Integer.toString(hashLots % 4) + ".png", tailleCases * 2,
                                        tailleCases, null));
                        break;
                    case EAU:

                        // ca marche po
                        // if (hashLots % 100 < 15) {
                        // gui.addGraphicalElement(new ImageElement(
                        // centerCol + (col - lig) * tailleCases,
                        // (col + lig) * tailleCases / 2, "assets2/fancy_water.png",
                        // tailleCases * 2,
                        // tailleCases, null));
                        // break;
                        // }
                        gui.addGraphicalElement(
                                new ImageElement(centerCol + (col - lig) * tailleCases,
                                        (col + lig) * tailleCases / 2,
                                        "assets2/water.png", tailleCases * 2, tailleCases, null));
                        break;
                    case HABITAT:
                        gui.addGraphicalElement(
                                new ImageElement(centerCol + (col - lig) * tailleCases,
                                        (col + lig) * tailleCases / 2,
                                        "assets2/lot" + Integer.toString(hashLots % 7) + ".png", tailleCases * 2,
                                        tailleCases,
                                        null));
                        break;
                    case FORET:
                        gui.addGraphicalElement(
                                new ImageElement(centerCol + (col - lig) * tailleCases,
                                        (col + lig) * tailleCases / 2,
                                        "assets2/forest" + Integer.toString(hashLots % 2) + ".png", tailleCases * 2,
                                        tailleCases, null));
                        // gui.addGraphicalElement(new ImageElement((col + lig) *tailleCases/2, (col +
                        // lig) *
                        // tailleCases,
                        // "assets/forest.png", tailleCases*2, tailleCases*2, null));
                        break;
                    default:
                        gui.addGraphicalElement(
                                new ImageElement(centerCol + (col - lig) * tailleCases, (col + lig) * tailleCases / 2,
                                        "assets2/fire.gif", tailleCases * 2, tailleCases * 2, null));
                        gui.addGraphicalElement(new Rectangle(tailleCases / 2 + (col + lig) * carte.getTailleCases(),
                                tailleCases / 2 + (col + lig) * carte.getTailleCases(),
                                NatureTerrainToColor(caseCourante.getNature()),
                                NatureTerrainToColor(caseCourante.getNature()), carte.getTailleCases()));
                        break;
                }
                if (incendie != null && !incendie.estEteint()) {
                    gui.addGraphicalElement(new ImageElement(centerCol + (col - lig) * tailleCases + tailleCases / 2,
                            (col + lig) * tailleCases / 2 - tailleCases / 4, "assets/fire.gif", tailleCases,
                            tailleCases, null));
                    gui.addGraphicalElement(new Text(centerCol + (col - lig) * tailleCases + tailleCases,
                            (col + lig) * tailleCases / 2, Color.RED,
                            Double.toString(incendie.getIntensite())));
                }
            }
        }

        for (Iterator<Robot> robots = donnees.getRobots(); robots.hasNext();) {
            Robot robot = robots.next();
            drawRobot(robot, tailleCases, gui);
        }

        if (this.positionAvion != null) {
            int lig = this.positionAvion.getLigne();
            int col = this.positionAvion.getColonne();
            gui.addGraphicalElement(new ImageElement(centerCol + (col - lig) * tailleCases + tailleCases / 2,
                    (col + lig) * tailleCases / 2 - tailleCases / 4, "assets2/airplane.png", tailleCases,
                    tailleCases, null));
        }

    }

    public DonneesSimulation getDonnees() {
        return this.donnees;
    }

    @Override
    public void next() {
        if (!simulationTerminee())
            incrementeDate();
        draw();
    }

    @Override
    public void restart() {
        this.donnees.resetDonnees();
        this.dateSimulation = 0;
        this.chef = new ChefPompier(this, this.donnees);
        this.scenario = new PriorityQueue<Evenement>(100, new ComparatorEvenements());
        this.ajouteEvenement(new AffectationIncendiesRobots(dateSimulation, null, this, 100));

        Carte carte = this.donnees.getCarte();
        this.positionAvion = carte.getCase(carte.getNbLignes() / 5, 0);
        this.ajouteEvenement(new Avion(100, null, this.positionAvion, this, 100));

        draw();
    }
}