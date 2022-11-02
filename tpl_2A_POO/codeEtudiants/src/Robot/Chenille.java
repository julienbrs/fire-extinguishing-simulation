package Robot;

import java.util.NoSuchElementException;

import Carte.*;


/* todo: faire une variable VitesseMax pour vitesseincorrectexception ? */

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
    static double vitesseMax = 60;
    static double volumeEauMax = 2000;
    public Chenilles(Case position, int volumeEau, double vitesse)
    {
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
        } catch (VitesseIncorrectException e) {
            System.out.println(e.getMessage());
        }
        //MARCHE PO
        super(position, volumeEau, vitesse);
    }

    public double getVitesse(NatureTerrain nature)
    {
        switch(nature)
        {
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
                /* on throw une exception */
                try {
                    throw new TerrainIncorrectException("Le terrain n'est pas correct");
                } catch (TerrainIncorrectException e) {
                    System.out.println(e.getMessage());
                }
        }
    }
    
    public void deverserEau(int vol)
    {
        try {
            if (vol < 0) {
                throw new VolumeEauIncorrectException("Le volume d'eau ne peut pas être négatif");
            }
            if (vol > this.volumeEau) {
                vol = this.volumeEau;
            }
            /* on regarde si la case est un incendie */
            // boolean peuxDeverser = this.donnees.isThereFire(this.position);
            Incendie incendie = this.donnees.getIncendie(this.position);
            
            if (incendie != null) {
                this.volumeEau -= vol;
                /* on déverse l'eau sur la position du robot */
                incendie.decreaseIntensity(vol);             
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
                if (getVoisin(this.position, dir).getNature() == NatureTerrain.EAU) {
                    boolean condition = true;
                }
            }
            if (!peuxRemplir) 
            {
                throw new NoSuchElementException("Il n'y a pas d'eau autour du robot");
            }
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        this.volumeEau = volumeEauMax;
    }
}