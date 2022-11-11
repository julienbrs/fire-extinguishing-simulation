package Simulation;

import java.awt.Color;
import java.util.Iterator;
import java.util.PriorityQueue;

import Robot.*;
import Carte.*;
import gui.GUISimulator;
import gui.Rectangle;
import gui.ImageElement;
import gui.Simulable;
import Events.Evenement;

public class Simulateur implements Simulable {
    private long dateSimulation;
    private PriorityQueue<Evenement> scenario;
    private GUISimulator gui;
    private DonneesSimulation donnees;

    public Simulateur(GUISimulator gui, DonneesSimulation donnees, PriorityQueue<Evenement> scenario, long dateSimulation) {
        this.gui = gui;
        gui.setSimulable(this);
        this.donnees = donnees;
        this.dateSimulation = dateSimulation;

        // Initialisation des couleurs
        this.draw();
    }

    void ajouteEvenement(Evenement e) {
        scenario.add(e);
    }

    void incrementeDate() {
        while (dateSimulation == scenario.peek().getDate()) {
            // poll : récupère et supprime la tete de la queue
            scenario.poll().execute();
        }
        dateSimulation++;

    }

    boolean simulationTerminee() {
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
        Case caseRobot = robot.getPosition();
        int lig = caseRobot.getLigne();
        int col = caseRobot.getColonne();
        switch (robot.getType()) {
            case DRONE:
                gui.addGraphicalElement(new ImageElement(col * tailleCases, lig * tailleCases, "assets/drone.gif",
                        tailleCases, tailleCases, null));
                break;
            case PATTES:
                gui.addGraphicalElement(new ImageElement(col * tailleCases, lig * tailleCases, "assets/pattes.gif",
                        tailleCases, tailleCases, null));
                break;
            default:
                gui.addGraphicalElement(new ImageElement(col * tailleCases, lig * tailleCases, "assets/robobo.gif",
                        tailleCases, tailleCases, null));
                break;
        }
    }

    private void draw() {
        gui.reset(); // clear window
        Carte carte = donnees.getCarte();
        Case caseCourante = null;
        int tailleCases = carte.getTailleCases();
        Incendie incendie = null;
        NatureTerrain nature = null;
        for (int lig = 0; lig < carte.getNbLignes(); lig++) {
            for (int col = 0; col < carte.getNbColonnes(); col++) {
                caseCourante = carte.getCase(lig, col);
                incendie = donnees.getIncendie(caseCourante);
                nature = caseCourante.getNature();

                switch (nature) {
                    case TERRAIN_LIBRE:
                        // drawTerrainLibre(caseCourante, tailleCases, gui);
                        gui.addGraphicalElement(new ImageElement(col * tailleCases, lig * tailleCases,
                                "assets/grass2.jpg", tailleCases, tailleCases, null));
                        break;
                    case ROCHE:
                        gui.addGraphicalElement(new ImageElement(col * tailleCases, lig * tailleCases,
                                "assets/grass2.jpg", tailleCases, tailleCases, null));
                        gui.addGraphicalElement(new ImageElement(col * tailleCases, lig * tailleCases,
                                "assets/rock.png", tailleCases, tailleCases, null));
                        break;
                    case EAU:
                        gui.addGraphicalElement(new ImageElement(col * tailleCases, lig * tailleCases,
                                "assets/water2.gif", tailleCases, tailleCases, null));
                        break;
                    case HABITAT:
                        gui.addGraphicalElement(new ImageElement(col * tailleCases, lig * tailleCases,
                                "assets/grass2.jpg", tailleCases, tailleCases, null));
                        gui.addGraphicalElement(new ImageElement(col * tailleCases, lig * tailleCases,
                                "assets/habitat2.png", tailleCases, tailleCases, null));
                        break;
                    case FORET:
                        gui.addGraphicalElement(new ImageElement(col * tailleCases, lig * tailleCases,
                                "assets/grass2.jpg", tailleCases, tailleCases, null));
                        gui.addGraphicalElement(new ImageElement(col * tailleCases, lig * tailleCases,
                                "assets/forest.png", tailleCases, tailleCases, null));
                        break;
                    default:
                        gui.addGraphicalElement(new ImageElement(col * tailleCases, lig * tailleCases,
                                "assets/fire2.gif", tailleCases, tailleCases, null));
                        gui.addGraphicalElement(new Rectangle(tailleCases / 2 + col * carte.getTailleCases(),
                                tailleCases / 2 + lig * carte.getTailleCases(),
                                NatureTerrainToColor(caseCourante.getNature()),
                                NatureTerrainToColor(caseCourante.getNature()), carte.getTailleCases()));
                        break;
                }
                if (incendie != null) {
                    gui.addGraphicalElement(new ImageElement(col * tailleCases, lig * tailleCases, "assets/fire2.gif",
                            tailleCases, tailleCases, null));
                }
            }
        }

        for (Iterator<Robot> robots = donnees.getRobots(); robots.hasNext();) {
            Robot robot = robots.next();
            drawRobot(robot, tailleCases, gui);
        }
    }

    public DonneesSimulation getDonnees() {
        return this.donnees;
    }

    @Override
    public void next() {
        draw();
    }

    @Override
    public void restart() {
        draw();
    }
}