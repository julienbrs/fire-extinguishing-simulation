package Robot;

import Carte.*;
import Simulation.DonneesSimulation;
import Exception.*;

public class Drone extends Robot {
    
    static double vitesseDefaut = 100;
    static double vitesseMax = 150;
    static int volumeEauMax = 10000;

    static int interventionUnitaire = volumeEauMax;
    static int tempsInterventionUnitaire = 30;
    static int tempsRemplissage = 30 * 60;

    public Drone(Case position, double vitesse, DonneesSimulation donnees)
            throws VitesseIncorrectException {
        super(position, 0, vitesse, donnees, interventionUnitaire, tempsInterventionUnitaire, tempsRemplissage);
        this.volumeEau = volumeEauMax;
        this.type = TypeRobot.DRONE;
        
        if (vitesse < 0)
            throw new VitesseIncorrectException("La vitesse ne peut pas être négative");
        if (vitesse > vitesseMax)
            throw new VitesseIncorrectException(
                    "La vitesse ne peut pas être supérieure à " + vitesseMax + "km/h pour un robot Drone");
        if (Double.isNaN(vitesse))
            /* Si la vitesse n'a pas été spécifiée, la mettre par défaut */
            this.vitesse = vitesseDefaut;
    }

    /**
     * Renvoie la vitesse du {@link Drone} qui est indépendante du terrain.
     * 
     * @param nature
     * @return double
     */
    public double getVitesse(NatureTerrain nature) {
        return this.vitesse;
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
     * Pour le robot {@link Drone}, on vérifie si la case courante du {@link Robot}
     * est de type EAU.
     * 
     * @param position
     * @return boolean
     */
    @Override
    public boolean peutRemplir(Case position) {
        return (position.getNature() == NatureTerrain.EAU);
    }

    /**
     * Remplit complètement le réservoir du robot {@link Drone}.
     * 
     * Jette {@link TerrainIncorrectException} s'il ne peut pas remplir son
     * réservoir grâce à la méthode {@link #peutRemplir()}
     * 
     * @throws TerrainIncorrectException
     */
    public void remplirReservoir() throws TerrainIncorrectException {
        if (peutRemplir()) {
            this.volumeEau = volumeEauMax;
        } else {
            throw new TerrainIncorrectException(
                    "Impossible de remplir le réservoir : il n'y a pas d'eau sur cette case !");
        }
    }
}