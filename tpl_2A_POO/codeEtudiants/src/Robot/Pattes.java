package Robot;

import java.util.NoSuchElementException;

import Carte.*;

public class Pattes extends Robot {
    static double vitesseDefaut = 30;

    public Pattes(Case position){
        super(position, null, vitesseDefaut);
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
                break;
            default:
                //sinon on throw une exception
                // try {
                throw new TerrainIncorrectException("Le terrain n'est pas correct");
                break;
                // } catch (TerrainIncorrectException e) {
                //     System.out.println(e.getMessage());
                // }
        }

    }

    public void deverserPoudre(int vol) throws VolumePoudreIncorrectException {
        // try {
        if (vol < 0) throw new VolumePoudreIncorrectException("Le volume est incorrect")
        Incendie incendie = this.donnees.getIncendie(this.position);
        if (incendie != null) incendie.decreaseIntensity(vol);
        // } catch (VolumeEauIncorrectException e){
        //     System.out.println(e.getMessage());
        // }
    }    

}