package Robot;

import java.util.NoSuchElementException;
import Exception.*;
import Carte.*;
import Simulation.DonneesSimulation;

public class Chenilles extends Robot
{   
    static double vitesseDefaut =  60;
    static double vitesseMax = 80;
    static int volumeEauMax = 2000;
    public Chenilles(Case position, int volumeEau, double vitesse, DonneesSimulation donnees) throws VitesseIncorrectException

    {
        super(position, volumeEau, vitesse, donnees);
        if (vitesse < 0) {
            throw new VitesseIncorrectException("La vitesse ne peut pas être négative");
        }
        if (vitesse > vitesseMax) {
            throw new VitesseIncorrectException("La vitesse ne peut pas être supérieure à 80km/h pour un robot Chenilles");
        }
        /* Si la vitesse n'a pas été spécifiée, la mettre par défaut */
        if (Double.isNaN(vitesse)) {
            vitesse = vitesseDefaut;
        }
        this.setVitesse(vitesse);
        }

    public double getVitesseOnTerrain(NatureTerrain nature) throws TerrainIncorrectException
    {
        switch(nature)
        {
            case EAU:
            /* ne peut pas se déplacer sur l'eau */
                return 0;
            case FORET:
            /* vitesse diminuée de 50% en forêt */
                return this.getVitesse() * 0.5;
            case ROCHE:
            /* ne peut pas se déplacer sur la roche */
                return 0;
            case TERRAIN_LIBRE:
                return this.getVitesse() ;
            case HABITAT:
                return this.getVitesse() ;
            default:
                /* on throw une exception */
                throw new TerrainIncorrectException("Le terrain n'est pas correct");
        }
    }
    
    public void deverserEau(int vol) throws VolumeEauIncorrectException
    {
        if (vol < 0) {
            throw new VolumeEauIncorrectException("Le volume d'eau ne peut pas être négatif");
        }
        /* Si le volume d'eau est supérieur à la capacité du robot, on le met à la capacité max */
        if (vol > this.getVolumeEau()) {
            vol = this.getVolumeEau();
        }
        /* on regarde si la case est un incendie */
        Incendie incendie = this.donnees.getIncendie(this.getPosition());
        if (incendie != null) {
            this.setVolumeEau(this.getVolumeEau() - vol);
            /* on déverse l'eau sur la position du robot */
            System.out.println("Déversement d'eau sur l'incendie en " + this.getPosition());
            incendie.decreaseIntensite(vol);             
        }
    }

    @Override
    public boolean peutRemplir()
    {
        /* variable si condition respectée ou non */
        boolean peuxRemplir = false;
        /* vérifie si une des cases alentours est de type eau */
        for (Direction dir : Direction.values()) {
            try {
                Carte carte = this.donnees.getCarte();
                Case caseVoisine = carte.getVoisin(this.getPosition(), dir);

                if (caseVoisine.getNature() == NatureTerrain.EAU) {
                    peuxRemplir = true;
                }
            } catch (IllegalArgumentException e) {
                /* on ne fait rien, on continue la boucle, pour éviter de raise une erreur si on est sur un bord */
            }
        }
        return peuxRemplir;
    }
    public void remplirReservoir() throws TerrainIncorrectException
    {
        if (!this.peutRemplir()) 
        {
            throw new TerrainIncorrectException("Il n'y a pas d'eau autour du robot");
        }
        else {
            this.setVolumeEau(volumeEauMax);
        }

    }
}