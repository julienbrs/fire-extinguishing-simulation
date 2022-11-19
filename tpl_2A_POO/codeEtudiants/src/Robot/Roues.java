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
        this.volumeEau = this.volumeEauMax;
        this.type = TypeRobot.ROUES;
        if (vitesse < 0)
            throw new VitesseIncorrectException("La vitesse ne peut pas être négative");
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
                // Ne peut pas se déplacer sur la roche, l'eau ou dans la foret
                return 0;
            case TERRAIN_LIBRE:
            case HABITAT:
                return this.vitesse;
            default:
                // should not happpen
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
            incendie.decreaseIntensite(vol);
        }
    }

    @Override
    public boolean peutRemplir(Case position) {
        /* vérifie si une des cases alentours est de type eau */
        // for (Direction dir : Direction.values()) {
        // try {
        // Carte carte = this.donnees.getCarte();
        // Case caseVoisine = carte.getVoisin(this.position, dir);

        // if (caseVoisine.getNature() == NatureTerrain.EAU) {
        // return true;
        // }
        // } catch (IllegalArgumentException e) {
        // /*
        // * on ne fait rien, on continue la boucle, pour éviter de raise une erreur si
        // on
        // * est sur un bord
        // */
        // }
        // }
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

    public void remplirReservoir() throws TerrainIncorrectException {
        if (peutRemplir()) {
            this.volumeEau = volumeEauMax;
        } else {
            throw new TerrainIncorrectException("Il n'y a pas d'eau autour du robot");
        }
    }

}