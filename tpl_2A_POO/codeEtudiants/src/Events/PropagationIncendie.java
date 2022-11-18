package Events;

import Robot.Robot;
import Simulation.DonneesSimulation;
import Simulation.Simulateur;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import Carte.Carte;
import Carte.Case;
import Carte.Direction;
import Carte.Incendie;

public class PropagationIncendie extends Evenement {

    int periode;

    public PropagationIncendie(long date, Simulateur simulateur, int periode) {
        super(date, null, simulateur);
        this.periode = periode;
    }

    /**
     * Le robot se tourne vers le Nord et avance.
     */
    public void execute() {
        DonneesSimulation donnees = this.simulateur.getDonnees();
        Carte carte = donnees.getCarte();
        Iterator<Incendie> incendies = donnees.getIncendies();
        Incendie incendie = null;
        HashMap<Case, Double> incendiesARajouter = new HashMap<Case, Double>();

        while (incendies.hasNext()) {
            incendie = incendies.next();
            Iterator<Case> voisins = carte.getVoisins(incendie.getPosition());
            Case voisin = null;

            while (voisins.hasNext()) {
                voisin = voisins.next();
                if (donnees.getIncendie(voisin) == null) {
                    double intensite = incendie.getIntensite();

                    if (intensite >= 4000)
                        incendiesARajouter.put(voisin, intensite / 2);
                }

            }

            for (Case positionIncendie : incendiesARajouter.keySet())
                donnees.addIncendie(positionIncendie, incendiesARajouter.get(positionIncendie));
        }
        simulateur.ajouteEvenement(new PropagationIncendie(periode, simulateur, periode));
    }
}
