package Carte;

import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import Simulation.DonneesSimulation;

import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

/* pour le tostring, on import Robot */
import Robot.*;


public class Carte
{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    

    private int tailleCases;
    private int nbLignes, nbColonnes;
    private Case[][] carte;
    private DonneesSimulation donnees;
    public Carte(int tailleCases, int nbLignes, int nbColonnes, Case[][] carte, DonneesSimulation donnees)
    {
        this.tailleCases = tailleCases;
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        this.carte = carte;
        this.donnees = donnees;
    }

    
    /** 
     * Renvoie le nombre de lignes totales dans une {@link Carte}.
     * @return int
     */
    public int getNbLignes()
    {
        return this.nbLignes;
    }

    
    /** 
     * Renvoie le nombre de colonnes totales dans une {@link Carte}.
     * @return int
     */
    public int getNbColonnes()
    {
        return this.nbColonnes;
    }

    
    /** 
     * Renvoie la taille des cases d'une {@link Carte}.
     * @return int
     */
    public int getTailleCases()
    {
        return this.tailleCases;
    }

    
    /** 
     * Renvoie la {@link Case} de la {@link Carte} à la position (lig, col). 
     * @param lig
     * @param col
     * @return Case
     */
    //dont even check for errors smh
    public Case getCase(int lig, int col)
    {
        return this.carte[lig][col];
    }
    
    /** 
     * Renvoie true si un voisin existe (on sort pas de la {@link Carte}) à la {@link Direction} dir de la {@link Case} donnée.
     * <p>
     * Jete {@link NullPointerException} si dir est null.
     * @param src
     * @param dir
     * @return boolean
     * @throws NullPointerException
     */
    public boolean voisinExiste(Case src, Direction dir) throws NullPointerException
    {
        int lig = src.getLigne();
        int col = src.getColonne();

        switch(dir)
        {
            case NORD:
                return (lig > 0);
            case EST:
                return (col < nbColonnes - 1);
            case SUD:
                return (lig < nbLignes - 1);
            case OUEST:
                return (col > 0);
            default:
                throw new NullPointerException("La direction ne devrait pas être null!");
        }
    }
    
    //todo
    //ICI ON POURRAIT RENVOIER NULL A LA CASE PLUTOT
    //QUE THROW UNE EXCEPTION, UN PEU COMME AVEC getIncendie
    //ET getRobot
    /** 
     * Renvoie le voisin de la {@link Case} src, à la {@link Direction} dir.
     * <p>
     * Si inexistant, jete {@link IllegalArgumentException}.
     * <p>
     * Si dir est null, jete {@link NullPointerException}.
     * @param src
     * @param dir
     * @return Case
     * @throws IllegalArgumentException
     * @throws NullPointerException
     */
    public Case getVoisin(Case src, Direction dir) throws IllegalArgumentException, NullPointerException
    {
        if (!this.voisinExiste(src, dir)) throw new IllegalArgumentException("Il n'existe pas un voisin à la direction demandé.");
        int lig = src.getLigne();
        int col = src.getColonne();

        switch(dir)
        {
            case NORD:
                return this.carte[lig - 1][col];
            case EST:
                return this.carte[lig][col + 1];
            case OUEST:
                return this.carte[lig][col - 1];
            case SUD:
                return this.carte[lig + 1][col];
            default:
                throw new NullPointerException("La direction ne devrait pas être null!");
        }
    }

    @Override
    /* before the graphic interface, we use shell to display donnees */
    public String toString()
    {
        /* on recupere les incendies et les robots */
        HashMap<Case, Incendie> incendies = this.donnees.getIncendies();
        // Iterator<Robot> robots = this.donnees.getRobots();

        String chaine = "";
        /* on fait d'abord la map vierge */
        for(int lig = 0; lig < nbLignes; lig++)
        {
            for(int col = 0; col < nbColonnes; col++)
            {
                /* On check s'il y a un incendie ici */
                if (incendies.containsKey(this.carte[lig][col]))
                {
                    chaine += "🔥";
                }
                else if (donnees.getRobot(this.carte[lig][col]) != null)
                {
                    chaine += "🤖";
                }
                else
                {
                    switch(carte[lig][col].getNature())
                    {
                        case EAU:
                            //💧
                            chaine += "💧";
                            break;
                        case FORET:
                            chaine += "🌲";
                            break;
                        case ROCHE:
                            chaine+= "⛰️ ";
                            break;
                        case TERRAIN_LIBRE:
                            chaine += "⬜";
                            break;     
                        case HABITAT:
                            chaine+= "🏠";
                            break;      
                        default:
                            break;
                    }
                }
            }
            chaine += '\n';
        }
        System.out.println("Map générée");
        return chaine;
    }
}