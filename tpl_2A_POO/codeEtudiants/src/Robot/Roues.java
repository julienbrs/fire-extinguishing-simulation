package Robot;

import java.util.NoSuchElementException;

import Carte.*;

public class Roues extends Robot {
    static double vitesseDefaut = 80;
    static double volumeEauMax = 5000;

    public Roues(Case position, int volumeEau, double vitesse) throws VitesseIncorrectException {
        // try {
        if (vitesse < 0) throw new VitesseIncorrectExcpetion("La vitesse ne peut pas être négative");
        if (Double.isNaN(vitesse)) vitesse = vitesseDefaut; 
        // } catch (VitesseIncorrectExcpetion e) {
        //     System.out.println(e.getMessage());
        // }
        super(position, volumeEau, vitesse);
    }

    public double getVitesse(NatureTerrain nature) throws TerrainIncorrectException {
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
                return this.getVitesse();
                break;
            default:
                // sinon on throw une exception
                // try {
                throw new TerrainIncorrectException("Le terrain n'est pas correct");
                // } catch (TerrainIncorrectException e) {
                    // System.out.println(e.getMessage());
                // }
                break;
        }

    }

    public void deverserEau(int vol) throws VolumeEauIncorrectException {
        // try {
            // boolean deverse;
            if (vol < 0) throw new VolumeEauIncorrectException("Le volume d'eau est incorrect");
            // Si volume d'eau indiqué supérieur au volume total, on déverse tout ce qu'il reste
            if (vol > this.volumeEau) vol = this.volumeEau;
            // Check si incendie sur la case courante
            Incendie incendie = this.donnees.getIncendie(this.position);
            if (incendie != null) {
                this.setVolumeEau(this.getVolumeEau() - vol);
                System.out.println("Déversement d'eau sur l'incendie en " + this.getPosition());
                incendie.decreaseIntensity(vol);
            }
        // } catch (VolumeEauIncorrectException e){
        //     System.out.println(e.getMessage());
        // }
    }

public void remplirReservoir() throws TerrainIncorrectException {
        // try {
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
                throw new TerrainIncorrectException("Il n'y a pas d'eau autour du robot");
            }
            else {
                this.setVolumeEau(volumeEauMax);
            }

        // } catch (NoSuchElementException e) {
        //     System.out.println(e.getMessage());
        // }
    }



    

}