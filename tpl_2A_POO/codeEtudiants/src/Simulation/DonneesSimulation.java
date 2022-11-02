package Simulation;
import Carte.Carte;
import Carte.Case;
import Carte.Incendie;
import Robot.Robot;
import Robot.TypeRobot;
import java.util.HashMap;
import java.util.NoSuchElementException;
// For now we don't need anything
// Will have to check how to display 
// In the following part of the subject
public class DonneesSimulation
{
    private Carte carte;
    private HashMap<Case, Incendie> incendies;
    private HashMap<Case, Robot> robots;

    public DonneesSimulation()
    {
        this.incendies = new HashMap<Case, Incendie>();
        this.robots = new HashMap<Case, Robot>();
    }
    // Possibly temporary method for testing
    public Carte getCarte()
    {
        return this.carte;
    }

    public void setCarte(Carte carte)
    {
        this.carte = carte;
        this.incendies = new HashMap<Case, Incendie>();
        this.robots = new HashMap<Case, Robot>();
    }
    public void addIncendie(Case position, int intensite)
    {
        Incendie incendie = new Incendie(position, intensite, this);
        this.incendies.put(position, incendie);
    }


    /* check if there is an incendie on the case */
    public boolean isThereFire(int lig, int col)
    {
        if (this.incendies.containsKey(this.carte.getCase(lig, col)))
        {
            return true;
        }
        return false;
    }

    public boolean getIncendie()
    {
        if (this.incendies.containsKey(this))
        {
            return this.incendies.get(this);
        }
        return false;
    }

    public void removeIncendie()
    {
        if (this.incendies.containsKey(this))
        {
            this.incendies.remove(this);
        }
    }

    public void addRobot(TypeRobot type, Case position, double vitesse) throws NoSuchElementException
    {
        Robot robot = Robot.newRobot(type, position, vitesse, this);
        this.robots.put(position, robot);
    }
}