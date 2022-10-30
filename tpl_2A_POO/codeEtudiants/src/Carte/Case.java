package Carte;
public class Case
{
    private int ligne, colonne;
    private NatureTerrain nature;

    public Case(int ligne, int colonne, NatureTerrain nature)
    {
        this.ligne = ligne;
        this.colonne = colonne;
        this.nature = nature;
    }
    public int getLigne()
    {
        return this.ligne;
    }
    public int getColonne()
    {
        return this.colonne;
    }
    public NatureTerrain getNature()
    {
        return this.nature;
    }
}