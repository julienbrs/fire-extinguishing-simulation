package Carte;
public class Case
{
    private int ligne, colonne;
    private NatureTerrain nature;

    // Si aucune nature n'est specifié
    public Case(int ligne, int colonne)
    {
        this(ligne, colonne, null);
    }
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

    // public Case copyCase()
    // {
    //     return new Case(this.ligne, this.colonne, this.nature);
    // }
}