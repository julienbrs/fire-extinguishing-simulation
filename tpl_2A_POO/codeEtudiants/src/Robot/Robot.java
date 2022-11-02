package Robot;

import java.util.NoSuchElementException;
import Simulation.DonneesSimulation;
import Carte.*;

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
    // Will i be able to modify a private attribute with this implementation?
    public Case getPosition() {
        return this.position;
    }
    public void setPosition(Case positionCase)
    {
        this.position = positionCase.copyCase();
    }
    // Crée le robot du bon type et le renvoie
    public static Robot newRobot(TypeRobot type, Case position, double vitesse, DonneesSimulation donnees) throws NoSuchElementException
    {
        switch(type)
        {
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
                throw new NoSuchElementException("Le robot " + type.toString() + " n'existe pas!");
        }
    }
    public abstract double getVitesse(NatureTerrain nature);
    public abstract void deverserEau(int vol);
    public abstract void remplirReservoir();
}