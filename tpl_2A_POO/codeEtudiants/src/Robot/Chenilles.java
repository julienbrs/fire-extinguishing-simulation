package Robot;

import java.util.Iterator;
import java.util.NoSuchElementException;
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
        this.volumeEau = this.volumeEauMax;
        this.type = TypeRobot.CHENILLES;
        if (vitesse < 0) {
            throw new VitesseIncorrectException("La vitesse ne peut pas être négative");
        }
        if (vitesse > vitesseMax) {
            throw new VitesseIncorrectException(
                    "La vitesse ne peut pas être supérieure à 80km/h pour un robot Chenilles");
        }
        /* Si la vitesse n'a pas été spécifiée, la mettre par défaut */
        if (Double.isNaN(vitesse)) {
            this.vitesse = vitesseDefaut;
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
                /* ne peut pas se déplacer sur l'eau */
                return 0;
            case FORET:
                /* vitesse diminuée de 50% en forêt */
                return this.vitesse * 0.5;
            case ROCHE:
                /* ne peut pas se déplacer sur la roche */
                return 0;
            case TERRAIN_LIBRE:
                return this.vitesse;
            case HABITAT:
                return this.vitesse;
            default:
                // no
                return Double.NaN;
        }
    }

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
        /* on regarde si la case est un incendie */
        Incendie incendie = this.donnees.getIncendie(this.position);
        if (incendie != null) {
            this.volumeEau -= vol;
            /* on déverse l'eau sur la position du robot */
            System.out.println("Déversement d'eau sur l'incendie en " + this.position);
            incendie.decreaseIntensite(vol);
        }
    }

    @Override
    public boolean peutRemplir(Case position) {
        /* variable si condition respectée ou non */
        boolean peuxRemplir = false;
        /* vérifie si une des cases alentours est de type eau */
        // for (Direction dir : Direction.values()) {
        // try {
        // Carte carte = this.donnees.getCarte();
        // Case caseVoisine = carte.getVoisin(this.position, dir);

        // if (caseVoisine.getNature() == NatureTerrain.EAU) {
        // peuxRemplir = true;
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
        return peuxRemplir;
    }

    public void remplirReservoir() throws TerrainIncorrectException {
        if (!this.peutRemplir()) {
            throw new TerrainIncorrectException("Il n'y a pas d'eau autour du robot");
        } else {
            this.volumeEau = volumeEauMax;
        }

    }
}