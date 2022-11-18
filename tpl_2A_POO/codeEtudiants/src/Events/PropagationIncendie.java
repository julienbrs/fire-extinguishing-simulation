package Events;

import Simulation.DonneesSimulation;
import Simulation.Simulateur;

import java.util.HashMap;
import java.util.Iterator;
import Carte.Carte;
import Carte.Case;
import Carte.Incendie;
import Carte.NatureTerrain;

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
                Incendie incendieVoisin = donnees.getIncendie(voisin);
                /* Si il n'y a pas d'incendie, ou si il y a un incendie éteint, on se propage */
                if ((incendieVoisin == null && voisin.getNature() != NatureTerrain.EAU)
                        || (incendieVoisin != null && incendieVoisin.estEteint())) {
                    double intensite = incendie.getIntensite();

                    if (intensite >= 4000)
                        incendiesARajouter.put(voisin, intensite / 2);
                }
            }
        }
        for (Case positionIncendie : incendiesARajouter.keySet())
            donnees.addIncendie(positionIncendie, incendiesARajouter.get(positionIncendie).intValue());
        simulateur.ajouteEvenement(new PropagationIncendie(periode, simulateur, periode));
    }
}
