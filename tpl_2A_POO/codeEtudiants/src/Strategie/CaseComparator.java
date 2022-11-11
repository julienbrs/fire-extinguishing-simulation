package Strategie;

import Carte.Case;
import java.util.Comparator;

class CaseComparator implements Comparator<Case> {

    private double couts[][];

    public CaseComparator(int lig, int col) {
        this.couts = new double[lig][col];
    }

    public double getCout(Case case1) {
        return this.couts[case1.getLigne()][case1.getColonne()];
    }

    public void setCout(Case case1, double cout) {
        this.couts[case1.getLigne()][case1.getColonne()] = cout;
    }

    @Override
    public int compare(Case case1, Case case2) {
        double cout1 = this.getCout(case1);
        double cout2 = this.getCout(case2);

        if (cout1 < cout2)
            return -1;
        else if (cout1 == cout2)
            return 0;
        else
            return 1;
    }
}
