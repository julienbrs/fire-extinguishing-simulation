package Simulation;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import javax.print.attribute.standard.MediaSize.NA;

import Carte.*;
import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;
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

class Simulateur implements Simulable
{
    private GUISimulator gui;
    private DonneesSimulation donnees;

    private static Color[] colors = {Color.orange, Color.green, Color.black, Color.blue, Color.red};
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
                return colors[0];
            case FORET:
                return colors[1];
            case HABITAT:
                return colors[2];
            case ROCHE:
                return colors[3];
            case TERRAIN_LIBRE:
                return colors[4];
            default:
                return colors[5];
        }
    }
    private void draw()
    {
        gui.reset(); //clear window
        Carte carte = donnees.getCarte();
        Case caseCourante;

        for(int lig = 0; lig < carte.getNbLignes(); lig++)
        {
            for(int col = 0; col < carte.getNbColonnes(); col++)
            {
                caseCourante = carte.getCase(lig, col);
                gui.addGraphicalElement(new Rectangle(col * carte.getTailleCases(), 50+lig * carte.getTailleCases(), NatureTerrainToColor(caseCourante.getNature()), NatureTerrainToColor(caseCourante.getNature()), carte.getTailleCases()));
            }
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