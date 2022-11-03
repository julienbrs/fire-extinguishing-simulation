package Robot;

import java.util.NoSuchElementException;

import Carte.*;
import Simulation.DonneesSimulation;


class VitesseIncorrectException extends Exception {
    public VitesseIncorrectException(String message) {
        super(message);
    }
}

class VolumeEauIncorrectException extends Exception {
    public VolumeEauIncorrectException(String message) {
        super(message);
    }
}

class TerrainIncorrectException extends Exception {
    public TerrainIncorrectException(String message) {
        super(message);
    }
}

public class Chenilles extends Robot
{   
    static double vitesseDefaut =  60;
    static double vitesseMax = 80;
    static int volumeEauMax = 2000;
    public Chenilles(Case position, int volumeEau, double vitesse, DonneesSimulation donnees)

    {
        super(position, volumeEau, vitesse, donnees);
        try {
            if (vitesse < 0) {
                throw new VitesseIncorrectException("La vitesse ne peut pas être négative");
            }
            if (vitesse > vitesseMax) {
                throw new VitesseIncorrectException("La vitesse ne peut pas être supérieure à 80km/h");
            }
            /* Si la vitesse n'a pas été spécifiée, la mettre par défaut */
            if (Double.isNaN(vitesse)) {
                vitesse = vitesseDefaut;
            }
            this.setVitesse(vitesse);
        } catch (VitesseIncorrectException e) {
            System.out.println(e.getMessage());
        }
        }

    public double getVitesseOnTerrain(NatureTerrain nature)
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
                try {
                    throw new TerrainIncorrectException("Le terrain n'est pas correct");
                } catch (TerrainIncorrectException e) {
                    System.out.println(e.getMessage());
                }
        }
        /* todo: on ne devrait jamais arriver ici  mais ok de laisser le return 0 ? si on le met pas 
        ca compile pas*/
        return 0;
    }
    
    public void deverserEau(int vol)
    {
        try {
            if (vol < 0) {
                throw new VolumeEauIncorrectException("Le volume d'eau ne peut pas être négatif");
            }
            /* Si le volume d'eau est supérieur à la capacité du robot, on le met à la capacité max */
            if (vol > this.getVolumeEau()) {
                vol = this.getVolumeEau();
            }
            /* on regarde si la case est un incendie */
            Incendie incendie = this.getDonnees().getIncendie(this.getPosition());
            if (incendie != null) {
                this.setVolumeEau(this.getVolumeEau() - vol);
                /* on déverse l'eau sur la position du robot */
                System.out.println("Déversement d'eau sur l'incendie en " + this.getPosition());
                incendie.decreaseIntensite(vol);             
            }
        } catch (VolumeEauIncorrectException e) {
            System.out.println(e.getMessage());
        }
    }

    public void remplirReservoir()
    {
        try {
            /* variable si condition respectée ou non */
            boolean peuxRemplir = false;
            /* vérifie si une des cases alentours est de type eau */
            for (Direction dir : Direction.values()) {
                try {
                    Carte carte = this.getDonnees().getCarte();
                    Case caseVoisine = carte.getVoisin(this.getPosition(), dir);

                    if (caseVoisine.getNature() == NatureTerrain.EAU) {
                        peuxRemplir = true;
                    }
                } catch (IllegalArgumentException e) {
                    /* on ne fait rien, on continue la boucle, pour éviter de raise une erreur si on est sur un bord */
                }
            }
            if (!peuxRemplir) 
            {
                throw new NoSuchElementException("Il n'y a pas d'eau autour du robot");
            }
            else {
                this.setVolumeEau(volumeEauMax);
            }

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }
}