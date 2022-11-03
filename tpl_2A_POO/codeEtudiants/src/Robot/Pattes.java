package Robot;

import Carte.*;
import Simulation.DonneesSimulation;
import Exception.*;

public class Pattes extends Robot {
    static double vitesseDefaut = 30;

    public Pattes(Case position, DonneesSimulation donnees){
        super(position, 0, vitesseDefaut, donnees);
    }

    public double getVitesse(NatureTerrain nature) throws TerrainIncorrectException {
        switch(nature)
        {
            case EAU:
                // Ne peut pas se déplacer sur l'eau
                return 0;
            case ROCHE:
                // Vitesse réduite à 10 km/h
                return 10;
            case FORET:
            case TERRAIN_LIBRE:
            case HABITAT:
                return this.vitesse;
            default:
                //sinon on throw une exception
                throw new TerrainIncorrectException("Le terrain n'est pas correct");
        }

    }


    public boolean peutRemplir(){
        return false;
    }

    public void remplirReservoir() {
        return;
        }


    public void deverserEau(int vol) throws VolumeEauIncorrectException {
        if (vol < 0) throw new VolumeEauIncorrectException("Le volume est incorrect");
        Incendie incendie = this.donnees.getIncendie(this.position);
        if (incendie != null) incendie.decreaseIntensite(vol);
    }    

}