package Robot;

import java.util.NoSuchElementException;

import Carte.*;

public class Roues extends Robot {

    public Roues(Case position, int volumeEau, double vitesse){
        static double vitesseDefaut = 80;
        static double volumeEauMax = 5000;
        try {
            if (vitesse < 0) throw new VitesseIncorrectExcpetion("La vitesse ne peut pas être négative");
            if (Double.isNaN(vitesse)) vitesse = vitesseDefaut; 
        } catch (VitesseIncorrectExcpetion e) {
            System.out.println(e.getMessage());
        }
        super(position, volumeEau, vitesse);
    }

    public double getVitesse(NatureTerrain nature){
        switch(nature)
        {
            case EAU:
            case FORET:
            case ROCHE:
                // Ne peut pas se déplacer sur la roche, l'eau ou dans la foret
                return 0;
                break;
            case TERRAIN_LIBRE:
            case HABITAT:
                return this.vitesse;
                break;
            default:
                //sinon on throw une exception
                try {
                    throw new TerrainIncorrectException("Le terrain n'est pas correct");
                } catch (TerrainIncorrectException e) {
                    System.out.println(e.getMessage());
                }
        }

    }

    public void deverserEau(int vol){
        try {
            // boolean deverse;
            if (vol < 0) throw new VolumeEauIncorrectException("Le volume d'eau est incorrect");
            if (vol > this.volumeEau) vol = this.volumeEau;
            Incendie incendie = this.donnees.getIncendie(this.position);
            if (incendie != null) {
                this.volumeEau -= volume;
                incendie.decreaseIntensity(vol);
            }
        } catch (VolumeEauIncorrectException e){
            System.out.println(e.getMessage());
        }
    }

    public void remplirReservoir() throws NoSuchElementException
    {
        //Où gerer le temps de remplissage?
        try {
            boolean remplir = false;
            for (Direction d : Direction.values()){
                if (getVoisin(this.position, d).getNature == NatureTerrain.EAU) remplir = true;
            }
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