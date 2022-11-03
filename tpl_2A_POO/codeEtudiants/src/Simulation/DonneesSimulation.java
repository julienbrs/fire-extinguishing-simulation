package Simulation;
import Carte.Carte;
import Carte.Case;
import Carte.Incendie;
import Exception.*;
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


    public HashMap<Case, Incendie> getIncendies()
    {
        return this.incendies;
    }

    public HashMap<Case, Robot> getRobots()
    {
        return this.robots;
    }

    /* check if there is an incendie on the case */
    private boolean isThereFire(int lig, int col)
    {
        if (this.incendies.containsKey(this.carte.getCase(lig, col)))
        {
            return true;
        }
        return false;
    }
    
    // Renvoie l'incendie à la position position, sinon renvoie null
    public Incendie getIncendie(Case position)
    {
        int lig = position.getLigne();
        int col = position.getColonne();
        if (this.isThereFire(lig, col)) {
            return this.incendies.get(this.carte.getCase(lig, col));
        }
        else return null;
    }

    public Robot getRobot(Case position)
    {
        int lig = position.getLigne();
        int col = position.getColonne();
        if (this.robots.containsKey(this.carte.getCase(lig, col))) return this.robots.get(this.carte.getCase(lig, col));
        else return null;
    }

    public void removeIncendie(Case position)
    {

        if (this.incendies.containsKey(position))
        {
            this.incendies.remove(position);
        }
    }

    public void addRobot(TypeRobot type, Case position, double vitesse) throws VitesseIncorrectException
    {
        Robot robot = Robot.newRobot(type, position, vitesse, this);
        this.robots.put(position, robot);
    }
}