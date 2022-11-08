package Simulation;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.DataFormatException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

import Robot.*;
import Carte.*;
import java.io.File;
import gui.GUISimulator;
import gui.Rectangle;
import gui.ImageElement;
import gui.Simulable;
import gui.Oval;
import gui.Text;
import io.LecteurDonnees;
public class TestSimulateur
{
    public static void main(String[] args)
    {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            DonneesSimulation donnees = LecteurDonnees.creeDonneesSimulation(args[0]);
            int tailleCases = donnees.getCarte().getTailleCases();
            int nbColonnes = donnees.getCarte().getNbColonnes();
            int nbLignes = donnees.getCarte().getNbLignes();
    
            System.out.println(Integer.toString(tailleCases*nbColonnes)+ " " + Integer.toString(tailleCases*nbLignes));
            GUISimulator gui = new GUISimulator(tailleCases*nbColonnes,tailleCases*nbLignes, Color.lightGray);
            Simulateur simulateur = new Simulateur(gui, donnees);

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}

class Gif extends JComponent implements ImageObserver
{
    Image img = null;
    public Gif()
    {
        this.img = new ImageIcon("assets/fire.gif").getImage();
        repaint();
    }

    
    // public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h)
    // {
    //     repaint();
    //     return true;
    // }
}
class Simulateur implements Simulable
{
    private GUISimulator gui;
    private DonneesSimulation donnees;

    public Simulateur(GUISimulator gui, DonneesSimulation donnees)
    {
        this.gui = gui;
        gui.setSimulable(this);
        this.donnees = donnees;

        //Initialisation des couleurs
        this.draw();
    }

    private Color NatureTerrainToColor(NatureTerrain nature)
    {
        switch(nature)
        {
            case EAU:
                return new Color(44, 163, 221);//(212, 241, 249);
            case FORET:
                return new Color(31, 61, 12);
            case HABITAT:
                return new Color(149, 131, 105);
            case ROCHE:
                return new Color(90, 77, 65);
            case TERRAIN_LIBRE:
                return new Color(141, 199, 64);//(144, 238, 144);
            default:
                return Color.BLACK;
        }
    }
    private void drawGrass(int x, int y, int tailleBloc, GUISimulator gui)
    {
        Color colorGrass = new Color(54, 180, 69);
        gui.addGraphicalElement(new Rectangle(              x, tailleBloc + y, colorGrass, colorGrass, tailleBloc));
        gui.addGraphicalElement(new Rectangle(1*tailleBloc + x,              y, colorGrass, colorGrass, tailleBloc));
        gui.addGraphicalElement(new Rectangle(2*tailleBloc + x, tailleBloc + y, colorGrass, colorGrass, tailleBloc));

    }
    private void drawTerrainLibre(Case position, int tailleCases, GUISimulator gui)
    {

        int ratio = 15;
        int grassBlock = tailleCases/ratio;
        int lig = tailleCases*position.getLigne();
        int col = tailleCases*position.getColonne();

        gui.addGraphicalElement(new Rectangle(tailleCases/2 + col, tailleCases/2 + lig, NatureTerrainToColor(NatureTerrain.TERRAIN_LIBRE), NatureTerrainToColor(NatureTerrain.TERRAIN_LIBRE), tailleCases));

        this.drawGrass(grassBlock*4 + col, grassBlock*2 + lig, grassBlock, gui);
        this.drawGrass(grassBlock*11 + col, grassBlock*4 + lig, grassBlock, gui);
        this.drawGrass(grassBlock*2 + col, grassBlock*7 + lig, grassBlock, gui);
        this.drawGrass(grassBlock*13 + col, grassBlock*9 + lig, grassBlock, gui);
        this.drawGrass(grassBlock*4 + col, grassBlock*13 + lig, grassBlock, gui);
        this.drawGrass(grassBlock*13 + col, grassBlock*13 + lig, grassBlock, gui);
    }

    private void drawRoche(Case position, int tailleCases, GUISimulator gui)
    {
        int ratio = 14;
        int rockBlock = tailleCases/ratio;
        int lig = tailleCases*position.getLigne() + 2*rockBlock;
        int col = tailleCases*position.getColonne();

        Color darkerBrown   = new Color(68, 56, 55);
        Color darkBrown     = new Color(101, 89, 87);
        Color lightBrown    = new Color(128, 116, 115);
        Color lighterBrown  = new Color(170, 159, 158);

        //1
        gui.addGraphicalElement(new Rectangle(9*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(10*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(11*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));

        //2
        gui.addGraphicalElement(new Rectangle(7*rockBlock +     col, rockBlock + lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(8*rockBlock +     col, rockBlock + lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(9*rockBlock +     col, rockBlock + lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(10*rockBlock +    col, rockBlock + lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(11*rockBlock +    col, rockBlock + lig, Color.RED, Color.RED, rockBlock));

        //3
        gui.addGraphicalElement(new Rectangle(7*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(8*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(9*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(10*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(11*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(12*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));

        //4
        gui.addGraphicalElement(new Rectangle(6*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(7*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(8*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(9*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(10*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(11*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(12*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));

        //5
        gui.addGraphicalElement(new Rectangle(6*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(7*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(8*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(9*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(10*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(11*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(12*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));

        //6
        gui.addGraphicalElement(new Rectangle(3*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(4*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(5*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(6*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(7*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(8*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(9*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(10*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(11*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(12*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(13*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));

        //7
        gui.addGraphicalElement(new Rectangle(2*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(3*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(4*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(5*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(6*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(7*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(8*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(9*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(10*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(11*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(12*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(13*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));

        //8
        gui.addGraphicalElement(new Rectangle(1*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(2*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(3*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(4*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(5*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(6*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(7*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(8*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(9*rockBlock +     col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(10*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(11*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(12*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(13*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(14*rockBlock +    col, lig, Color.RED, Color.RED, rockBlock));

        //9
        gui.addGraphicalElement(new Rectangle(1*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(2*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(3*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(4*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(5*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(6*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(7*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(8*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(9*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(10*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(11*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(12*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(13*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(14*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));

        //10
        gui.addGraphicalElement(new Rectangle(1*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(2*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(3*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(4*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(5*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(6*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(7*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(8*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(9*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(10*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(11*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(12*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(13*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));
        gui.addGraphicalElement(new Rectangle(14*rockBlock + col, lig, Color.RED, Color.RED, rockBlock));


    }
    private void drawFire(Case position, int tailleCases, GUISimulator gui)
    {
        int ratio = 13;
        int fireBlock = tailleCases/ratio;
        int offset = tailleCases/2;

        Color darkOrange    = new   Color(249,  63, 6);
        Color lightOrange   = new   Color(251, 137, 5);
        Color darkYellow    = new   Color(250, 208, 0);
        Color lightYellow   = new   Color(244, 251, 0);

        int lig = tailleCases*position.getLigne();
        int col = tailleCases*position.getColonne() + 5*fireBlock/2;

        //1
        gui.addGraphicalElement(new Rectangle(2*fireBlock + col, fireBlock + lig, Color.RED, Color.RED, fireBlock));
   
        //2
        gui.addGraphicalElement(new Rectangle(3*fireBlock + col, 2*fireBlock + lig, Color.RED, Color.RED, fireBlock));
        gui.addGraphicalElement(new Rectangle(4*fireBlock + col, 2*fireBlock + lig, Color.RED, Color.RED, fireBlock));

        //3
        gui.addGraphicalElement(new Rectangle(4*fireBlock + col, 3*fireBlock + lig, darkOrange, darkOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(5*fireBlock + col, 3*fireBlock + lig, Color.RED, Color.RED, fireBlock));

        gui.addGraphicalElement(new Rectangle(7*fireBlock + col, 3*fireBlock + lig, darkOrange, darkOrange, fireBlock));
        //4
        gui.addGraphicalElement(new Rectangle(3*fireBlock + col, 4*fireBlock + lig, Color.RED, Color.RED, fireBlock));
        gui.addGraphicalElement(new Rectangle(4*fireBlock + col, 4*fireBlock + lig, darkOrange, darkOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(5*fireBlock + col, 4*fireBlock + lig, darkOrange, darkOrange, fireBlock));

        //5
        gui.addGraphicalElement(new Rectangle(3*fireBlock + col, 5*fireBlock + lig, Color.RED, Color.RED, fireBlock));
        gui.addGraphicalElement(new Rectangle(4*fireBlock + col, 5*fireBlock + lig, darkOrange, darkOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(5*fireBlock + col, 5*fireBlock + lig, darkOrange, darkOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(6*fireBlock + col, 5*fireBlock + lig, Color.RED, Color.RED,   fireBlock));
        gui.addGraphicalElement(new Rectangle(7*fireBlock + col, 5*fireBlock + lig, Color.RED, Color.RED,   fireBlock));

        //6
        gui.addGraphicalElement(new Rectangle(3*fireBlock + col, 6*fireBlock + lig, Color.RED, Color.RED, fireBlock));
        gui.addGraphicalElement(new Rectangle(4*fireBlock + col, 6*fireBlock + lig, darkOrange, darkOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(5*fireBlock + col, 6*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(6*fireBlock + col, 6*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(7*fireBlock + col, 6*fireBlock + lig, Color.RED, Color.RED, fireBlock));

        //7
        gui.addGraphicalElement(new Rectangle(2*fireBlock + col, 7*fireBlock + lig, Color.RED, Color.RED, fireBlock));
        gui.addGraphicalElement(new Rectangle(3*fireBlock + col, 7*fireBlock + lig, darkOrange, darkOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(4*fireBlock + col, 7*fireBlock + lig, darkOrange, darkOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(5*fireBlock + col, 7*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(6*fireBlock + col, 7*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(7*fireBlock + col, 7*fireBlock + lig, darkOrange, darkOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(8*fireBlock + col, 7*fireBlock + lig, Color.RED, Color.RED, fireBlock));

        //8
        gui.addGraphicalElement(new Rectangle(1*fireBlock + col, 8*fireBlock + lig, Color.RED, Color.RED, fireBlock));
        gui.addGraphicalElement(new Rectangle(2*fireBlock + col, 8*fireBlock + lig, darkOrange, darkOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(3*fireBlock + col, 8*fireBlock + lig, darkOrange, darkOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(4*fireBlock + col, 8*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(5*fireBlock + col, 8*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(6*fireBlock + col, 8*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(7*fireBlock + col, 8*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(8*fireBlock + col, 8*fireBlock + lig, Color.RED, Color.RED, fireBlock));

        //9
        gui.addGraphicalElement(new Rectangle(1*fireBlock + col, 9*fireBlock + lig, Color.RED, Color.RED, fireBlock));
        gui.addGraphicalElement(new Rectangle(2*fireBlock + col, 9*fireBlock + lig, darkOrange, darkOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(3*fireBlock + col, 9*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(4*fireBlock + col, 9*fireBlock + lig, darkYellow, darkYellow, fireBlock));
        gui.addGraphicalElement(new Rectangle(5*fireBlock + col, 9*fireBlock + lig, darkYellow, darkYellow, fireBlock));
        gui.addGraphicalElement(new Rectangle(6*fireBlock + col, 9*fireBlock + lig, darkYellow, darkYellow, fireBlock));
        gui.addGraphicalElement(new Rectangle(7*fireBlock + col, 9*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(8*fireBlock + col, 9*fireBlock + lig, Color.RED, Color.RED, fireBlock));

        //10
        gui.addGraphicalElement(new Rectangle(1*fireBlock + col, 10*fireBlock + lig, Color.RED, Color.RED, fireBlock));
        gui.addGraphicalElement(new Rectangle(2*fireBlock + col, 10*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(3*fireBlock + col, 10*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(4*fireBlock + col, 10*fireBlock + lig, darkYellow, darkYellow, fireBlock));
        gui.addGraphicalElement(new Rectangle(5*fireBlock + col, 10*fireBlock + lig, darkYellow, darkYellow, fireBlock));
        gui.addGraphicalElement(new Rectangle(6*fireBlock + col, 10*fireBlock + lig, darkYellow, darkYellow, fireBlock));
        gui.addGraphicalElement(new Rectangle(7*fireBlock + col, 10*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(8*fireBlock + col, 10*fireBlock + lig, Color.RED, Color.RED, fireBlock));

        //11
        gui.addGraphicalElement(new Rectangle(1*fireBlock + col, 11*fireBlock + lig, Color.RED, Color.RED, fireBlock));
        gui.addGraphicalElement(new Rectangle(2*fireBlock + col, 11*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(3*fireBlock + col, 11*fireBlock + lig, lightYellow, lightYellow, fireBlock));
        gui.addGraphicalElement(new Rectangle(4*fireBlock + col, 11*fireBlock + lig, darkYellow, darkYellow, fireBlock));

        gui.addGraphicalElement(new Rectangle(5*fireBlock + col, 11*fireBlock + lig, Color.WHITE, Color.WHITE, fireBlock));


        gui.addGraphicalElement(new Rectangle(6*fireBlock + col, 11*fireBlock + lig, lightYellow, lightYellow, fireBlock));
        gui.addGraphicalElement(new Rectangle(7*fireBlock + col, 11*fireBlock + lig, lightOrange, lightOrange, fireBlock));
        gui.addGraphicalElement(new Rectangle(8*fireBlock + col, 11*fireBlock + lig, Color.RED, Color.RED, fireBlock));

        //12
        gui.addGraphicalElement(new Rectangle(2*fireBlock + col, 12*fireBlock + lig, Color.RED, Color.RED, fireBlock));
        gui.addGraphicalElement(new Rectangle(3*fireBlock + col, 12*fireBlock + lig, lightYellow, lightYellow, fireBlock));

        gui.addGraphicalElement(new Rectangle(4*fireBlock + col, 12*fireBlock + lig, Color.WHITE, Color.WHITE, fireBlock));
        gui.addGraphicalElement(new Rectangle(5*fireBlock + col, 12*fireBlock + lig, Color.WHITE, Color.WHITE, fireBlock));


        gui.addGraphicalElement(new Rectangle(6*fireBlock + col, 12*fireBlock + lig, lightYellow, lightYellow, fireBlock));
        gui.addGraphicalElement(new Rectangle(7*fireBlock + col, 12*fireBlock + lig, Color.RED, Color.RED, fireBlock));

        //13
        gui.addGraphicalElement(new Rectangle(3*fireBlock + col, 13*fireBlock + lig, Color.RED, Color.RED, fireBlock));

        gui.addGraphicalElement(new Rectangle(4*fireBlock + col, 13*fireBlock + lig, Color.WHITE, Color.WHITE, fireBlock));
        gui.addGraphicalElement(new Rectangle(5*fireBlock + col, 13*fireBlock + lig, Color.WHITE, Color.WHITE, fireBlock));


        gui.addGraphicalElement(new Rectangle(6*fireBlock + col, 13*fireBlock + lig, Color.RED, Color.RED, fireBlock));

    }
    private void drawRobot(Case position, int tailleCases, GUISimulator gui)
    {
        int ratio = 20;
        int robot_block = tailleCases/ratio;
        int offset = tailleCases/2;
        int lig = tailleCases*position.getLigne() + (tailleCases - 15*robot_block)/2;
        int col = tailleCases*position.getColonne() + (tailleCases - 13*robot_block)/2;
        //Head

        System.out.println(robot_block);
        //Left ear
        gui.addGraphicalElement(new Rectangle(3*robot_block + col, robot_block  + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(3*robot_block + col,  2*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));

        // gui.addGraphicalElement(new Rectangle(4*robot_block, robot_block ,  Color.BLACK, Color.BLUE, robot_block));
        // gui.addGraphicalElement(new Rectangle(4*robot_block, 2*robot_block , Color.BLUE, Color.BLUE, robot_block));

        //Right ear
        gui.addGraphicalElement(new Rectangle(9*robot_block + col, robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(9*robot_block + col, 2*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));

        //Head
        gui.addGraphicalElement(new Rectangle(4*robot_block + col, lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(6*robot_block + col, lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(4*robot_block + col, robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, robot_block + lig, Color.BLACK, Color.BLACK, robot_block));
        gui.addGraphicalElement(new Rectangle(6*robot_block + col, robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, robot_block + lig, Color.BLACK, Color.BLACK, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 2*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 2*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(6*robot_block + col, 2*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 2*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 2*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 3*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 3*robot_block + lig, Color.YELLOW, Color.ORANGE, robot_block));
        gui.addGraphicalElement(new Rectangle(6*robot_block + col, 3*robot_block + lig, Color.YELLOW, Color.ORANGE, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 3*robot_block + lig, Color.YELLOW, Color.ORANGE, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 3*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 4*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 4*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(6*robot_block + col, 4*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 4*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 4*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        //CORPS
        gui.addGraphicalElement(new Rectangle(1*robot_block + col, 5*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));
        gui.addGraphicalElement(new Rectangle(2*robot_block + col, 5*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));

        gui.addGraphicalElement(new Rectangle(3*robot_block + col, 5*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 5*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 5*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(6*robot_block + col, 5*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 5*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 5*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(9*robot_block + col, 5*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(10*robot_block + col, 5*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));
        gui.addGraphicalElement(new Rectangle(11*robot_block + col, 5*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));

        //2eme ligne corps
        gui.addGraphicalElement(new Rectangle(1*robot_block + col, 6*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));
        gui.addGraphicalElement(new Rectangle(2*robot_block + col, 6*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));

        gui.addGraphicalElement(new Rectangle(3*robot_block + col, 6*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 6*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 6*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));
        gui.addGraphicalElement(new Rectangle(6*robot_block + col, 6*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 6*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 6*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));
        gui.addGraphicalElement(new Rectangle(9*robot_block + col, 6*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(10*robot_block + col, 6*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));
        gui.addGraphicalElement(new Rectangle(11*robot_block + col, 6*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));

        //3eme ligne corps
        gui.addGraphicalElement(new Rectangle(1*robot_block + col, 7*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(3*robot_block + col, 7*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 7*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 7*robot_block + lig, Color.YELLOW, Color.YELLOW, robot_block));
        gui.addGraphicalElement(new Rectangle(6*robot_block + col, 7*robot_block + lig, Color.YELLOW, Color.YELLOW, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 7*robot_block + lig, Color.YELLOW, Color.YELLOW, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 7*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));
        gui.addGraphicalElement(new Rectangle(9*robot_block + col, 7*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(11*robot_block + col, 7*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        //4eme ligne corps
        gui.addGraphicalElement(new Rectangle(1*robot_block + col, 8*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(3*robot_block + col, 8*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 8*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 8*robot_block + lig, Color.YELLOW, Color.YELLOW, robot_block));
        gui.addGraphicalElement(new Rectangle(6*robot_block + col, 8*robot_block + lig, Color.YELLOW, Color.YELLOW, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 8*robot_block + lig, Color.YELLOW, Color.YELLOW, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 8*robot_block + lig, Color.CYAN, Color.CYAN, robot_block));
        gui.addGraphicalElement(new Rectangle(9*robot_block + col, 8*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(11*robot_block + col, 8*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));


        //5eme ligne corps
        gui.addGraphicalElement(new Rectangle(1*robot_block + col, 9*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(2*robot_block + col, 9*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(0*robot_block + col, 9*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));


        gui.addGraphicalElement(new Rectangle(3*robot_block + col, 9*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 9*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 9*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(6*robot_block + col, 9*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 9*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 9*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(9*robot_block + col, 9*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(10*robot_block + col, 9*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(11*robot_block + col, 9*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(12*robot_block + col, 9*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));

        //6eme ligne corps
        gui.addGraphicalElement(new Rectangle(2*robot_block + col, 10*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(0*robot_block + col, 10*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));


        gui.addGraphicalElement(new Rectangle(3*robot_block + col, 10*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 10*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 10*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(6*robot_block + col, 10*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 10*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 10*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(9*robot_block + col, 10*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(10*robot_block + col, 10*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(12*robot_block + col, 10*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));

        //jambes
        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 11*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 11*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 11*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 11*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 12*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 12*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 12*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 12*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 13*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 13*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 13*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 13*robot_block + lig, Color.GRAY, Color.GRAY, robot_block));

        //pieds
        gui.addGraphicalElement(new Rectangle(3*robot_block + col, 14*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 14*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 14*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 14*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 14*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(9*robot_block + col, 14*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));

        gui.addGraphicalElement(new Rectangle(3*robot_block + col, 15*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(4*robot_block + col, 15*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(5*robot_block + col, 15*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(7*robot_block + col, 15*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(8*robot_block + col, 15*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));
        gui.addGraphicalElement(new Rectangle(9*robot_block + col, 15*robot_block + lig, Color.BLUE, Color.BLUE, robot_block));

    }
    private void draw()
    {
        gui.reset(); //clear window
        Carte carte = donnees.getCarte();
        Case caseCourante = null;
        int tailleCases = carte.getTailleCases();
        Incendie incendie = null;
        NatureTerrain nature = null;
        Gif gif = new Gif();

        for(int lig = 0; lig < carte.getNbLignes(); lig++)
        {
            for(int col = 0; col < carte.getNbColonnes(); col++)
            {
                caseCourante = carte.getCase(lig, col);
                incendie = donnees.getIncendie(caseCourante);
                nature = caseCourante.getNature();

                switch(nature)
                {
                    case TERRAIN_LIBRE:
                        // drawTerrainLibre(caseCourante, tailleCases, gui);
                        gui.addGraphicalElement(new ImageElement(col*tailleCases, lig*tailleCases, "assets/grass2.jpg", tailleCases, tailleCases, gif));
                        break;
                    case ROCHE:
                        drawRoche(caseCourante, tailleCases, gui);
                        break;
                    default:
                        gui.addGraphicalElement(new Rectangle(tailleCases/2 + col * carte.getTailleCases(), tailleCases/2 + lig * carte.getTailleCases(), NatureTerrainToColor(caseCourante.getNature()), NatureTerrainToColor(caseCourante.getNature()), carte.getTailleCases()));
                        break;
                }
                if(incendie != null)
                {
                    gui.addGraphicalElement(new ImageElement(col*tailleCases, lig*tailleCases, "assets/fire.gif", tailleCases, tailleCases, gif));
                }
            }
        }

        for(Iterator<Robot> robots = donnees.getRobots(); robots.hasNext();)
        {
            Robot robot = robots.next();
            drawRobot(robot.getPosition(), tailleCases, gui);
        }
    }
    
    @Override
    public void next()
    {
        draw();
    }

    @Override
    public void restart()
    {
        draw();
    }
}