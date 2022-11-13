package Carte;

import Simulation.DonneesSimulation;

public class Incendie {
    private Case position;
    private double intensite;

    private DonneesSimulation donnees;

    public Incendie(Case position, double intensite, DonneesSimulation donnees) {
        this.position = position;
        this.intensite = intensite;
        this.donnees = donnees;
    }

    public Case getPosition() {
        return this.position;
    }

    public double getIntensite() {
        return this.intensite;
    }

    // I imagine we have to decrease the fire
    // intensity to slowly put it out
    public void setIntensite(double intensite) {
        this.intensite = intensite;
    }

    public void decreaseIntensite(double vol) {
        this.intensite -= vol;
        // if (this.intensite <= 0)
        // {this.donnees.removeIncendie(this.position);}

    }

    // Pour debug
    @Override
    public String toString() {
        return "(lig: " + position.getLigne() + " col: "
                + position.getColonne() + " intensité: " + intensite + " )";
    }
}