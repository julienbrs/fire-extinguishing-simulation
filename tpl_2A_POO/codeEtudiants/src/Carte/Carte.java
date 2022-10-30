package Carte;
public class Carte
{
    private int tailleCases;
    private int nbLignes, nbColonnes;
    private Case[][] carte;
    public Carte(int nbLignes, int nbColonnes, Case[][] carte)
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

    public Case getCase(int lig, int col)
    {
        return this.carte[lig][col];
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