package Carte;

public class Case {
    
    private int ligne, colonne;
    private NatureTerrain nature;

    public Case(int ligne, int colonne, NatureTerrain nature) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.nature = nature;
    }

    /**
     * Renvoie la ligne correspondante à la {@link Case}.
     * 
     * Index commence à 0.
     * 
     * @return int
     */
    public int getLigne() {
        return this.ligne;
    }

    /**
     * Renvoie la colonne correspondante à la {@link Case}.
     * 
     * Index commence à 0.
     * 
     * @return int
     */
    public int getColonne() {
        return this.colonne;
    }

    /**
     * Renvoie la {@link NatureTerrain} de la case.
     * 
     * @return NatureTerrain
     */
    public NatureTerrain getNature() {
        return this.nature;
    }

    /**
     * toString() d'une case, affichant ses attributs {ligne, colonne, nature}.
     * 
     * @return String
     */
    @Override
    public String toString() {
        return "Case [ligne=" + ligne + " colonne=" + colonne + ", nature=" + nature + "]";
    }
}