package Carte;

import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;
public class Carte
{
    private int tailleCases;
    private int nbLignes, nbColonnes;
    private Case[][] carte;
    public Carte(int tailleCases, int nbLignes, int nbColonnes, Case[][] carte)
    {
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        this.carte = carte;
    }

    public int getNbLignes()
    {
        return this.nbLignes;
    }

    public int getNbColonnes()
    {
        return this.nbColonnes;
    }

    public int getTailleCases()
    {
        return this.tailleCases;
    }

    //dont even check for errors smh
    public Case getCase(int lig, int col)
    {
        return this.carte[lig][col];
    }
    public boolean voisinExiste(Case src, Direction dir) throws NullPointerException
    {
        int lig = src.getLigne();
        int col = src.getColonne();

        switch(dir)
        {
            case NORD:
                return (lig > 0) ? true : false;
            case EST:
                return (col < nbColonnes) ? true : false;
            case SUD:
                return (lig < nbLignes) ? true : false;
            case OUEST:
                return (col > 0) ? true : false;
            default:
                throw new NullPointerException("La direction ne devrait pas être null!");
        }
    }
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
    public String toString()
    {
        String chaine = "";
        for(int lig = 0; lig < nbLignes; lig++)
        {
            for(int col = 0; col < nbColonnes; col++)
            {
                switch(carte[lig][col].getNature())
                {
                    case EAU:
                        chaine += "~";
                        break;
                    case FORET:
                        chaine += "ϔ";
                        break;
                    case ROCHE:
                        chaine+= "@";
                        break;
                    case TERRAIN_LIBRE:
                        chaine += ".";
                        break;     
                    case HABITAT:
                        chaine+="#";
                        break;      
                    default:
                        break;
                }
            }
            chaine += '\n';
        }
        return chaine;
    }
}