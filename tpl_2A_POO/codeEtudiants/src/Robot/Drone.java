package Robot;

import java.util.NoSuchElementException;

import Carte.*;

public class Drone extends Robot {

    public Drone(Case position, int volumeEau, double vitesse){
        static double vitesseDefaut = 100;
        static double vitesseMax = 150;
        static double volumeEauMax = 10000;
        try {
            if (vitesse < 0){
                throw new VitesseIncorrectExcpetion("La vitesse ne peut pas être négative");
            }
            if (vitesse > vitesseMax){
                throw new VitesseIncorrectException("La vitesse du drone ne peut exceder les 150 km/h");
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
            case FORET:
            case ROCHE:
            case TERRAIN_LIBRE:
            case HABITAT:
                return this.vitesse;
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
            //gestion des interventions unitaires?
            if (vol < 0) {
                throw new VolumeEauIncorrectException("Le volume d'eau est incorrect")
            }
            if (vol > this.volumeEau) {
                vol = this.volumeEau;
            }
            Incendie incendie = this.donnees.getIncendie(this.position);
            if (incendie != null) {
                this.volumeEau -= volume;
                incendie.decreaseIntensity(vol);
            }
        } catch (VolumeEauIncorrectException e){
            System.out.println(e.getMessage());
        }
    }

    public void remplirReservoir(){
        try {
            boolean remplir = false;
            if(this.case.getNature == NatureTerrain.EAU){
                remplir = true;
            }
            if (remplir){
                this.volumeEau = 10000;
            } else {
                throw new NoSuchElementException("Impossible de remplir le réservoir : il n'y a pas d'eau sur cette case !")
            }
        } catch (NoSuchElementException e){
            System.out.println(e.getMessage())
        }
    }



    

}