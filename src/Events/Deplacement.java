package Events;

import Robot.Robot;
import Simulation.Simulateur;
import Strategie.Chemin;

import java.util.Iterator;

import Carte.Case;
import Carte.Direction;
import Carte.Carte;

public class Deplacement extends Evenement {
    Iterator<Case> iterator;
    Chemin chemin;
    Case casePrecedente;

    public Deplacement(long date, Robot robot, Simulateur simulateur, Chemin chemin, Iterator<Case> iterator,
            Case casePrecedente) {
        super(date, robot, simulateur);
        this.chemin = chemin;
        this.iterator = iterator;
        this.casePrecedente = casePrecedente;
    }

    /**
     * Le robot se tourne vers le Nord et avance.
     */
    public void execute() {
        /* Il y a au moins deux éléments dans le chemin */

        /*
         * Trick: affecteIncendie(null) renvoie true si le robot n'as pas d'incendie
         * False sinon. Cet appel ne va pas vraiment affecter un incendie
         */
        if (this.robot.checkIncendie() || !iterator.hasNext()) {
            this.simulateur.ajouteEvenement(new FinAction(0, robot, simulateur));
            return;
        }
        Case caseCourante = iterator.next();
        Evenement evenement = null;
        long cout = (long) Math.ceil((this.chemin.getCout(caseCourante) - this.chemin.getCout(casePrecedente)));
        Direction dir = Carte.getDirection(casePrecedente, caseCourante);

        if (dir == null) {
            this.simulateur.ajouteEvenement(new FinAction(0, robot, simulateur));
            return;
        }

        switch (dir) {
            case NORD:
                evenement = new DeplacementNord(cout, this.robot, simulateur);
                break;
            case EST:
                evenement = new DeplacementEst(cout, this.robot, simulateur);
                break;
            case OUEST:
                evenement = new DeplacementOuest(cout, this.robot, simulateur);
                break;
            case SUD:
                evenement = new DeplacementSud(cout, this.robot, simulateur);
                break;
            default:
                // cela n'arrive jamais
                return;
        }
        simulateur.ajouteEvenement(new Deplacement(cout + 1, robot, simulateur, chemin, iterator, caseCourante));
        simulateur.ajouteEvenement(evenement);

    }
}
