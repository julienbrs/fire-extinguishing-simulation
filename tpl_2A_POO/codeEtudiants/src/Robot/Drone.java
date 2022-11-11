package Robot;

import Carte.*;
import Simulation.DonneesSimulation;
import Exception.*;

public class Drone extends Robot {
    static double vitesseDefaut = 100;
    static double vitesseMax = 150;
    static int volumeEauMax = 10000;

    public Drone(Case position, int volumeEau, double vitesse, DonneesSimulation donnees)
            throws VitesseIncorrectException {
        super(position, volumeEau, vitesse, donnees);
        this.type = TypeRobot.DRONE;
        if (vitesse < 0)
            throw new VitesseIncorrectException("La vitesse ne peut pas être négative");
        if (vitesse > vitesseMax)
            throw new VitesseIncorrectException("La vitesse du drone ne peut exceder les 150 km/h");
        if (Double.isNaN(vitesse))
            this.vitesse = vitesseDefaut;
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
            case FORET:
            case ROCHE:
            case TERRAIN_LIBRE:
            case HABITAT:
                return this.vitesse;
            default:
                // this should not happen
                return Double.NaN;
        }
    }

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
            System.out.println("Déversement d'eau sur l'incendie en " + this.position);
            incendie.decreaseIntensite(vol);
        }
    }

    @Override
    public boolean peutRemplir() {
        return (this.position.getNature() == NatureTerrain.EAU);
    }

    public void remplirReservoir() throws TerrainIncorrectException {
        if (peutRemplir()) {
            this.volumeEau = volumeEauMax;
        } else {
            throw new TerrainIncorrectException(
                    "Impossible de remplir le réservoir : il n'y a pas d'eau sur cette case !");
        }
    }
}