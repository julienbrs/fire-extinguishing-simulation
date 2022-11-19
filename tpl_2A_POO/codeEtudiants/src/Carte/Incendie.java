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

    /**
     * Regarde si le feux est éteint, si oui renvoie true.
     * 
     * @return boolean
     */
    public boolean estEteint() {
        return this.intensite <= 0;
    }

    /**
     * Renvoie la {@link Case} où se situe l' {@link Incendie}.
     * 
     * @return Case
     */
    public Case getPosition() {
        return this.position;
    }

    /**
     * Renvoie l'intensité de l' {@link Incendie}.
     * <p>
     * L'intensité correspond au volume d'eau nécessaire pour éteindre l'incendie.
     *
     * @return double
     */
    public double getIntensite() {
        return this.intensite;
    }

    /**
     * Renvoie les {@link DonneesSimulation} de l' {@link Incendie}.
     * 
     * @return DonneesSimulation
     */
    /**
     * Définit l'intensité de l' {@link Incendie}.
     *
     * @param intensite
     */
    public void setIntensite(double intensite) {
        this.intensite = intensite;
    }

    /**
     * Diminue l'intensité de l' {@link Incendie}.
     * <p>
     *
     * @param vol
     */
    public void decreaseIntensite(double vol) {
        this.intensite -= vol;
    }

    /**
     * toString() de l' {@link Incendie}, affichant sa position (ligne, colonne) et
     * son intensité.
     *
     * @return String
     */
    @Override
    public String toString() {
        return "(lig: " + position.getLigne() + " col: "
                + position.getColonne() + " intensité: " + intensite + " )";
    }
}
