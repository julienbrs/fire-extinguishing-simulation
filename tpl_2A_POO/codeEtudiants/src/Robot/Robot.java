package Robot;

import java.util.NoSuchElementException;
import Simulation.DonneesSimulation;
import Carte.*;
import Exception.*;

public abstract class Robot
{
    private Case position;
    private int volumeEau;
    private double vitesse;

    private DonneesSimulation donnees;

    public Robot(Case position, int volumeEau, double vitesse, DonneesSimulation donnees)
    {
        this.position = position;
        this.volumeEau = volumeEau;
        this.vitesse = vitesse;
        this.donnees = donnees;
    }

    // Crée le robot du bon type et le renvoie
    public static Robot newRobot(TypeRobot type, Case position, double vitesse, DonneesSimulation donnees) 
        throws NoSuchElementException, VitesseIncorrectException
    {
        switch(type)
        {
            case CHENILLES:
            /* Volume d'eau temporairement à 0, changé dans le constructeur */
                return new Chenilles(position, 0, vitesse, donnees);
            case DRONE:
            //appeller le constructeur du robot drone
                return null;
            case ROUES:
            //appeller le constructeur du robot roues
                return null;
            case PATTES:
            //appeller le constructeur du robot pattes ( pas un copié-collé)
                return null;                
            default:
                throw new NoSuchElementException("Le  type robot " + type.toString() + " n'existe pas!");
        }
    }

    public Case getPosition() {
        return this.position;
    }
    public void setPosition(Case positionCase) throws TerrainIncorrectException
    {
        if (this.getVitesseOnTerrain((positionCase).getNature()) == 0)
        {
            System.out.println("Le robot ne peut pas se déplacer sur ce terrain");
        }
        else
        {
            //A changer pue le seum cette partie du code
            this.donnees.getRobots().remove(this.position);
            this.position = positionCase;
            this.donnees.getRobots().put(this.position, this);
        }
    }
    
    public boolean moveRobot(Case destination)
    {
        try
        {
            this.setPosition(destination);
            return true;
        } catch (TerrainIncorrectException e)
        {
            return false;
        }
    }
    public boolean moveRobotDirection(Direction direction)
    {
        Carte carte = this.donnees.getCarte();
        if (carte.voisinExiste(this.position, direction)) return moveRobot(carte.getVoisin(this.position, direction));
        else return false;
    }

    public int getVolumeEau()
    {
        return this.volumeEau;
    }

    public void setVolumeEau(int volumeEau)
    {
        this.volumeEau = volumeEau;
    }

    public double getVitesse()
    {
        return this.vitesse;
    }

    public void setVitesse(double vitesse)
    {
        this.vitesse = vitesse;
    }
    
    public abstract double getVitesseOnTerrain(NatureTerrain nature) throws TerrainIncorrectException;
    public abstract boolean peutRemplir();
    public abstract void deverserEau(int vol) throws VolumeEauIncorrectException;
    public abstract void remplirReservoir() throws TerrainIncorrectException;
}