package Carte;
public class Incendie
{
    private Case position;
    private double intensite;

    public Incendie(Case position, double intensite)
    {
        this.position = position;
        this.intensite = intensite;
    }

    public Case getPosition()
    {
        return this.position.copyCase();
    }
    public double getIntensite()
    {
        return this.intensite;
    }
    //I imagine we have to decrease the fire
    //intensity to slowly put it out
    public void setIntensite(double intensite)
    {
        this.intensite = intensite;
    }
    // Pour debug
    @Override
    public String toString()
    {
        return "(lig: " + position.getLigne() +" col: " 
                + position.getColonne() + " intensité: "+intensite + " )";
    }
}