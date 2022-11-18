package Robot;

import Carte.*;
import Exception.*;
import Simulation.DonneesSimulation;

public class Pattes extends Robot {

    static double vitesseDefaut = 30;
    static int volumeEauMax = (int) Double.POSITIVE_INFINITY;
    static int interventionUnitaire = 10;
    static int tempsInterventionUnitaire = 1;
    static int tempsRemplissage = 0;

    /* Le robot pattes à une vitesse de base fixée */
    public Pattes(Case position, DonneesSimulation donnees)
            throws VitesseIncorrectException {
        super(position, 0, vitesseDefaut, donnees, interventionUnitaire, tempsInterventionUnitaire, tempsRemplissage);
        this.volumeEau = volumeEauMax;
        this.type = TypeRobot.PATTES;

        if (vitesse < 0) {
            throw new VitesseIncorrectException(
                    "La vitesse ne peut pas être négative");
        }
    }

    /**
     * Renvoie la vitesse du {@link Robot} selon le {@link NatureTerrain}.
     * Le parametre nature doit être non null.
     *
     * @param nature
     * @return double
     */
    public double getVitesse(NatureTerrain nature) {
        switch (nature) {
            case EAU:
                // Ne peut pas se déplacer sur l'eau
                return 0;
            case ROCHE:
                // Vitesse réduite à 10 km/h
                return 10;
            case FORET:
            case TERRAIN_LIBRE:
            case HABITAT:
                return this.vitesse;
            default:
                /* Impossible d'arriver ici, l'erreur est catch avant. */
                return Double.NaN;
        }
    }

    /**
     * Vérifie s'il y a bien un {@link Incendie} sur la case courante. Si oui, on
     * diminue son intensité avec la méthode {@link Incendie#decreaseIntensite
     * decreaseIntensite}.
     * <p>
     * Jette {@link VolumeEauIncorrectException} si le volume d'eau
     * disponible est négatif.
     * 
     * @param vol
     * @throws VolumeEauIncorrectException
     */
    public void deverserEau(int vol) throws VolumeEauIncorrectException {
        if (vol < 0)
            throw new VolumeEauIncorrectException("Le volume est incorrect");
        Incendie incendie = this.donnees.getIncendie(this.position);
        if (incendie != null)
            incendie.decreaseIntensite(vol);
    }

    /**
     * Le robot {@link Pattes} n'a pas de réservoir à remplir.
     * Renvoie donc toujours false
     * 
     * @param position
     * @return boolean
     */
    public boolean peutRemplir(Case position) {
        return false;
    }

    /**
     * Le robot {@link Pattes} n'a pas de réservoir à remplir.
     * Donc la fonction ne fait rien;
     */
    public void remplirReservoir() {
        return;
    }

}
