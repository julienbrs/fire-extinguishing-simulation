package Robot;

import Carte.*;

public abstract class Robot
{
    private Case position;
    private int volumeEau;
    protected double vitesse;

    public Robot(Case position, int volumeEau, double vitesse)
    {
        this.position = position;
        this.volumeEau = volumeEau;
        this.vitesse = vitesse;
    }
    public Case getPosition() {
        return position;
    }
    public void setPosition(Case positionCase)
    {
        this.position = positionCase;
    }
    // Crée le robot du bon type et le renvoie
    public Robot newRobot(TypeRobot type)
    {
        switch(type)
        {
            case DRONE:
            //appeller le constructeur du robot drone
                break;
            case ROUES:
            //appeller le constructeur du robot roues
                break;
            case PATTES:
            //appeller le constructeur du robot pattes ( pas un copié-collé)
                break;
            default:
                throw new Exception("Le robot n'existe pas!");
        }
    }
    public abstract double getVitesse(NatureTerrain nature);
    public abstract void deverserEau(int vol);
    public abstract void remplirReservoir();
}