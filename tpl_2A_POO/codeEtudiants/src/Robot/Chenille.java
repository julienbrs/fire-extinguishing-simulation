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

public class Chenilles
{   
    public Chenilles(Case position, int volumeEau, double vitesse)
    {
        try {
            if (vitesse < 0) {
                throw new VitesseIncorrectException("La vitesse ne peut pas être négative");
            }
            if (vitesse > 80) {
                throw new VitesseIncorrectException("La vitesse ne peut pas être supérieure à 80km/h");
            }
        } catch (VitesseIncorrectException e) {
            System.out.println(e.getMessage());
        }
        
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
                throw new VolumeEauIncorrectException("Le volume d'eau ne peut pas être supérieur au volume d'eau restant");
            }
        } catch (VolumeEauIncorrectException e) {
            System.out.println(e.getMessage());
        }
        this.volumeEau -= vol;
    }

    public void remplirReservoir()
    {
        /* vérifie si une des cases alentours est de type eau */
        for (int i = 0; i < 4; i++) {
            if (this.position.getVoisin(i).getNature() == NatureTerrain.EAU) {
                this.volumeEau = 1000;
                return;
            }
        }
        this.volumeEau = 2000;
    }
}