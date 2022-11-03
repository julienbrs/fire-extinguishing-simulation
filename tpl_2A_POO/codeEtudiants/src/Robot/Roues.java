package Robot;

import Carte.*;
import Simulation.DonneesSimulation;
import Exception.*;

public class Roues extends Robot {
    static double vitesseDefaut = 80;
    static int volumeEauMax = 5000;

    public Roues(Case position, int volumeEau, double vitesse, DonneesSimulation donnees) throws VitesseIncorrectException {
        super(position, volumeEau, vitesse, donnees);
        if (vitesse < 0) throw new VitesseIncorrectException("La vitesse ne peut pas être négative");
        if (Double.isNaN(vitesse)) this.vitesse = vitesseDefaut; 
    }

    public double getVitesse(NatureTerrain nature) throws TerrainIncorrectException {
        switch(nature)
        {
            case EAU:
            case FORET:
            case ROCHE:
                // Ne peut pas se déplacer sur la roche, l'eau ou dans la foret
                return 0;
            case TERRAIN_LIBRE:
            case HABITAT:
                return this.vitesse;
            default:
                // sinon on throw une exception
                throw new TerrainIncorrectException("Le terrain n'est pas correct");
        }

    }

    public void deverserEau(int vol) throws VolumeEauIncorrectException {
            if (vol < 0) throw new VolumeEauIncorrectException("Le volume d'eau est incorrect");
            // Si volume d'eau indiqué supérieur au volume total, on déverse tout ce qu'il reste
            if (vol > this.volumeEau) vol = this.volumeEau;
            // Check si incendie sur la case courante
            Incendie incendie = this.donnees.getIncendie(this.position);
            if (incendie != null) {
                this.volumeEau -= vol;
                System.out.println("Déversement d'eau sur l'incendie en " + this.position);
                incendie.decreaseIntensite(vol);
            }
    }

    @Override
    public boolean peutRemplir(){
        /* vérifie si une des cases alentours est de type eau */
        for (Direction dir : Direction.values()) {
            try {
                Carte carte = this.donnees.getCarte();
                Case caseVoisine = carte.getVoisin(this.position, dir);

                if (caseVoisine.getNature() == NatureTerrain.EAU) {
                    return true;
                }
            } catch (IllegalArgumentException e) {
                /* on ne fait rien, on continue la boucle, pour éviter de raise une erreur si on est sur un bord */
            }
        }
        return false;
    
}

    public void remplirReservoir() throws TerrainIncorrectException {
        if (peutRemplir()) 
        {
            this.setVolumeEau(volumeEauMax);  
        } else {
            throw new TerrainIncorrectException("Il n'y a pas d'eau autour du robot");
        }
        }

}