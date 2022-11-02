package Robot;

import java.util.NoSuchElementException;

import Carte.*;

public class Roues extends Robot {

    public Roues(Case position, int volumeEau, double vitesse){
        static double vitesseDefaut = 80;
        static double vitesseMax = 80;
        static double volumeEauMax = 5000;
        try {
            if (vitesse < 0){
                throw new VitesseIncorrectExcpetion("La vitesse ne peut pas être négative");
            }
            if (Double.isNaN(vitesse)){
                vitesse = vitesseDefaut;
            } 
        } catch (VitesseIncorrectExcpetion e) {
            System.out.println(e.getMessage());
        }
        super(position, volumeEau, vitesse);
    }

    public double getVitesse(NatureTerrain nature){
        switch(nature)
        {
            case EAU:
                // Ne peut pas se déplacer sur l'eau
                return 0;
            case FORET:
                // Ne peut pas se déplacer dans la forêt
                return 0;
            case ROCHE:
                // Ne peut pas se déplacer sur la roche
                return 0
            case TERRAIN_LIBRE:
                return this.vitesse;
            case HABITAT:
                return this.vitesse;
            default:
                try {
                    throw new TerrainIncorrectException("Le terrain n'est pas correct");
                } catch (TerrainIncorrectException e) {
                    System.out.println(e.getMessage());
                }
        }

    }

    public void deverserEau(int vol){
        try {
            boolean deverse;
            if (vol < 0) {
                throw new VolumeEauIncorrectException("Le volume d'eau est incorrect")
            }
            if (vol > this.volumeEau) {
                vol = this.volumeEau;
            }
            Incendie incendie = this.donnees.getIncendie(this.)
            if (deverse) {
                this.volumeEau -= volume;
            }
        } catch (VolumeEauIncorrectException e){
            System.out.println(e.getMessage());
        }
    }

    public void remplirReservoir(){
        try {
            boolean remplir;
            //TO DO : check si les cases autour ont de l'eau
            if (remplir){
                this.volumeEau = 5000;
            } else {
                throw new NoSuchElementException("Impossible de remplir le réservoir : il n'y a pas d'eau aux alentours !")
            }
        } catch (NoSuchElementException e){
            System.out.println(e.getMessage())
        }
    }



    

}