package Events;

import Robot.Robot;
import Simulation.DonneesSimulation;
import Simulation.Simulateur;
import Carte.Incendie;
import Exception.*;

public class DeversementEau extends Evenement {
    int volume;
    int periode;

    /* Robot est ici sur un incendie */

    public DeversementEau(long date, Robot robot, int vol, Simulateur simulateur, int periode) {
        super(date, robot, simulateur);
        this.volume = vol;
        System.out.println("WE ARE CHANGING THE VALUE \n");
        robot.setVerseEau(true);
    }

    /**
     * Le robot verse de l'eau sur l'incendie autant de fois que nécessaire.
     * Jette {@link VolumeEauIncorrectException} si le robot n'est pas sur une case
     * {@link Incendie}.
     */
    public void execute() {
        try {
            DonneesSimulation donnees = this.simulateur.getDonnees();
            Incendie incendie = donnees.getIncendie(this.robot.getPosition());

            this.robot.deverserEau(this.volume);

            /*
             * Si il reste moins d'une interventionUnitaire, on ne prévoit pas de continuerà
             * verser de l'eau.
             */
            if (incendie.estEteint() || this.robot.getVolumeEau() < volume) {
                this.robot.checkIncendie();
                this.simulateur.ajouteEvenement(new FinAction(0, robot, simulateur));
            } else {
                this.simulateur.ajouteEvenement(new DeversementEau(periode, robot, volume, simulateur, periode));
            }
            robot.setVerseEau(false);

        } catch (VolumeEauIncorrectException e) {
            System.out.println(e);
        }

    }
}