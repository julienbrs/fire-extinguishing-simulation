package Robot;

import java.util.Iterator;
import Exception.*;
import Carte.*;
import Simulation.DonneesSimulation;

public class Chenilles extends Robot {

    static double vitesseDefaut = 60;
    static double vitesseMax = 80;
    static int volumeEauMax = 2000;

    static int interventionUnitaire = 100;
    static int tempsInterventionUnitaire = 8;
    static int tempsRemplissage = 5 * 60;

    public Chenilles(Case position, double vitesse, DonneesSimulation donnees)
            throws VitesseIncorrectException

    {
        super(position, 0, vitesse, donnees, interventionUnitaire, tempsInterventionUnitaire, tempsRemplissage);
        this.volumeEau = volumeEauMax;
        this.type = TypeRobot.CHENILLES;

        if (vitesse < 0) {
            throw new VitesseIncorrectException("La vitesse ne peut pas être négative");
        }
        if (vitesse > vitesseMax) {
            throw new VitesseIncorrectException(
                    "La vitesse ne peut pas être supérieure à " + vitesseMax + "km/h pour un robot Chenilles");
        }
        /* Si la vitesse n'a pas été spécifiée, la mettre par défaut */
        if (Double.isNaN(vitesse)) {
            this.vitesse = vitesseDefaut;
        }

    }

    /**
     * Renvoie la vitesse du {@link Robot} selon le {@link NatureTerrain}.
     * Le paramètre nature doit être non null. Si ça arrive la fonction renvoit
     * Double.NaN
     * 
     * @param nature
     * @return double
     */
    public double getVitesse(NatureTerrain nature) {
        switch (nature) {
            case EAU:
            case ROCHE:
                /* Ne peut pas se déplacer sur l'eau et la roche */
                return 0;
            case FORET:
                /* Vitesse diminuée de 50% en forêt */
                return this.vitesse * 0.5;
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
        if (vol < 0) {
            throw new VolumeEauIncorrectException("Le volume d'eau ne peut pas être négatif");
        }
        /*
         * Si le volume d'eau est supérieur à la capacité du robot, on le met à la
         * capacité max
         */
        if (vol > this.volumeEau) {
            vol = this.volumeEau;
        }
        /* On regarde si la case est un incendie */
        Incendie incendie = this.donnees.getIncendie(this.position);
        if (incendie != null) {
            this.volumeEau -= vol;
            /* on déverse l'eau sur la position du robot */
            incendie.decreaseIntensite(vol);
        }
    }

    /**
     * Vérifie si le {@link Robot} peut remplir son réservoir d'eau.
     * <p>
     * Pour le robot {@link Chenilles}, on vérifie si l'une des cases voisines est
     * de type EAU.
     * 
     * @param position
     * @return boolean
     */
    @Override
    public boolean peutRemplir(Case position) {
        Case voisin = null;
        Carte carte = this.donnees.getCarte();
        Iterator<Case> iterator = carte.getVoisins(position);
        // On regarde si au moins une des cases voisines est de type eau
        while (iterator.hasNext()) {
            voisin = iterator.next();
            if (voisin.getNature() == NatureTerrain.EAU)
                return true;
        }
        return false;
    }

    /**
     * Remplit complètement le réservoir du robot {@link Chenilles}.
     * 
     * Jette {@link TerrainIncorrectException} s'il ne peut pas remplir son
     * réservoir si {@link #peutRemplir()} renvoit false.
     * 
     * @throws TerrainIncorrectException
     */
    public void remplirReservoir() throws TerrainIncorrectException {
        if (!this.peutRemplir()) {
            throw new TerrainIncorrectException("Il n'y a pas d'eau autour du robot");
        } else {
            this.volumeEau = volumeEauMax;
        }

    }
}