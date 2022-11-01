import Carte.Carte;
import Carte.Incendie;
import Robot.Robot;
// For now we don't need anything
// Will have to check how to display 
// In the following part of the subject
class DonneesSimulation
{
    private Carte carte;
    private Incendie[] incendies;
    private Robot[] robots;
    public DonneesSimulation(Carte carte, Incendie[] incendies, Robot[] robots)
    {
        this.carte = carte;
        this.incendies = incendies;
        this.robots = robots;
    }
}