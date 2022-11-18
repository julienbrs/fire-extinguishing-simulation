package Robot;

import java.util.Iterator;

import Carte.*;
import Simulation.DonneesSimulation;
import Exception.*;

public class Roues extends Robot {

    static double vitesseDefaut = 80;
    static int volumeEauMax = 5000;
    static int interventionUnitaire = 100;
    static int tempsInterventionUnitaire = 5;
    static int tempsRemplissage = 10 * 60;

    public Roues(Case position, double vitesse, DonneesSimulation donnees)
            throws VitesseIncorrectException {
        super(position, 0, vitesse, donnees, interventionUnitaire, tempsInterventionUnitaire, tempsRemplissage);
        this.volumeEau = volumeEauMax;
        this.type = TypeRobot.ROUES;

        if (vitesse < 0)
            throw new VitesseIncorrectException("La vitesse ne peut pas être négative");
        if (Double.isNaN(vitesse))
            this.vitesse = vitesseDefaut;
    }

    /**
     * Renvoie la vitesse du {@link Robot} selon le {@link NatureTerrain}.
     * 
     * @param nature
     * @return double
     */
    public double getVitesse(NatureTerrain nature) {
        switch (nature) {
            // Ne peut pas se déplacer sur la roche, l'eau ou dans la foret
            case EAU:
            case FORET:
            case ROCHE:
                return 0;
            case TERRAIN_LIBRE:
            case HABITAT:
                return this.vitesse;
            default:
                // should not happpen
                return Double.NaN;
        }

    }

    /**
     * * Vérifie s'il y a bien un {@link Incendie} sur la case courante. Si oui, on
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
            throw new VolumeEauIncorrectException("Le volume d'eau est incorrect");
        // Si volume d'eau indiqué supérieur au volume total, on déverse tout ce qu'il
        // reste
        if (vol > this.volumeEau)
            vol = this.volumeEau;
        // Check si incendie sur la case courante
        Incendie incendie = this.donnees.getIncendie(this.position);
        if (incendie != null) {
            this.volumeEau -= vol;
            incendie.decreaseIntensite(vol);
        }
    }

    /**
     * Vérifie si le {@link Robot} peut remplir son réservoir d'eau.
     * <p>
     * Pour le robot {@link Roues}, on vérifie si l'une des cases voisines est
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
        while (iterator.hasNext()) {
            voisin = iterator.next();
            if (voisin.getNature() == NatureTerrain.EAU)
                return true;
        }
        return false;

    }

    /**
     * Remplit complètement le réservoir du robot {@link Roues}.
     * 
     * Jette {@link TerrainIncorrectException} s'il ne peut pas remplir son
     * réservoir après un appel à la méthode {@link #peutRemplir()}
     * 
     * @throws TerrainIncorrectException
     */
    public void remplirReservoir() throws TerrainIncorrectException {
        if (peutRemplir()) {
            this.volumeEau = volumeEauMax;
        } else {
            throw new TerrainIncorrectException("Il n'y a pas d'eau autour du robot");
        }
    }

}