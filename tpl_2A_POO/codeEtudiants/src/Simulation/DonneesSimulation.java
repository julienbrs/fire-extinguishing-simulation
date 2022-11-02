package Simulation;
import Carte.Carte;
import Carte.Case;
import Carte.Incendie;
import Robot.Robot;
import java.util.HashMap;
// For now we don't need anything
// Will have to check how to display 
// In the following part of the subject
public class DonneesSimulation
{
    private Carte carte;
    private HashMap<Case, Incendie> incendies;
    private Robot[] robots;
    public DonneesSimulation(Carte carte, HashMap<Case, Incendie> incendies, Robot[] robots)
    {
        this.carte = carte;
        this.incendies = incendies;
        this.robots = robots;
    }
    // Possibly temporary method for testing
    public Carte getCarte()
    {
        return this.carte;
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

}