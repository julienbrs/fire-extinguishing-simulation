package Carte;

import java.util.ArrayList;
import java.util.Iterator;

import Robot.Robot;
import Simulation.DonneesSimulation;

public class Carte {

    private int tailleCases;
    private int nbLignes, nbColonnes;
    private Case[][] carte;
    private DonneesSimulation donnees;

    public Carte(int tailleCases, int nbLignes, int nbColonnes, Case[][] carte, DonneesSimulation donnees) {
        this.tailleCases = tailleCases;
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        this.carte = carte;
        this.donnees = donnees;
    }

    /**
     * Renvoie le nombre de lignes totales dans une {@link Carte}.
     *
     * @return int
     */
    public int getNbLignes() {
        return this.nbLignes;
    }

    /**
     * Renvoie le nombre de colonnes totales dans une {@link Carte}.
     *
     * @return int
     */
    public int getNbColonnes() {
        return this.nbColonnes;
    }

    /**
     * Renvoie la taille des cases d'une {@link Carte}.
     *
     * @return int
     */
    public int getTailleCases() {
        return this.tailleCases;
    }

    /**
     * Renvoie la {@link Case} de la {@link Carte} à la position (lig, col).
     *
     * @param lig
     * @param col
     * @return Case
     */
    public Case getCase(int lig, int col) {
        return this.carte[lig][col];
    }

    /**
     * Renvoie true si un voisin existe à la {@link Direction} dir de la
     * {@link Case} donnée.
     * <p>
     * Jette {@link NullPointerException} si dir est null.
     *
     * @param src
     * @param dir
     * @return boolean
     * @throws NullPointerException
     */
    public boolean voisinExiste(Case src, Direction dir) throws NullPointerException {
        int lig = src.getLigne();
        int col = src.getColonne();

        switch (dir) {
            case NORD:
                return (lig > 0);
            case EST:
                return (col < nbColonnes - 1);
            case SUD:
                return (lig < nbLignes - 1);
            case OUEST:
                return (col > 0);
            default:
                throw new NullPointerException("La direction ne devrait pas être null!");
        }
    }

    /**
     * Renvoie le voisin de la {@link Case} src à la {@link Direction} dir.
     * <p>
     * Si inexistant, jette {@link IllegalArgumentException}.
     * <p>
     * Si dir est null, jette {@link NullPointerException}.
     *
     * @param src
     * @param dir
     * @return Case
     * @throws IllegalArgumentException
     * @throws NullPointerException
     */
    public Case getVoisin(Case src, Direction dir) throws IllegalArgumentException, NullPointerException {
        if (!this.voisinExiste(src, dir))
            throw new IllegalArgumentException("Il n'existe pas un voisin à la direction demandé.");
        int lig = src.getLigne();
        int col = src.getColonne();

        switch (dir) {
            case NORD:
                return this.carte[lig - 1][col];
            case EST:
                return this.carte[lig][col + 1];
            case OUEST:
                return this.carte[lig][col - 1];
            case SUD:
                return this.carte[lig + 1][col];
            default:
                throw new NullPointerException("La direction ne devrait pas être null!");
        }
    }

    /**
     * Renvoie la direction associée au déplacement {@link Case} courante vers la
     * {@Case} voisin. Renvoie null si les deux {@link Case}s sont les mêmes.
     *
     * @param courante
     * @param voisin
     * @return Direction
     */
    public static Direction getDirection(Case courante, Case voisin) {
        int ligCourante = courante.getLigne();
        int colCourante = courante.getColonne();

        int ligVoisin = voisin.getLigne();
        int colVoisin = voisin.getColonne();

        if (ligCourante < ligVoisin)
            return Direction.SUD;
        else if (ligCourante > ligVoisin)
            return Direction.NORD;
        if (colCourante < colVoisin)
            return Direction.EST;
        else if (colCourante > colVoisin)
            return Direction.OUEST;

        return null;
    }

    /**
     * Renvoie toute les {@link Case}s voisines existantes de src.
     *
     * @param src
     * @return Iterator<Case>
     */
    public Iterator<Case> getVoisins(Case src) {
        ArrayList<Case> voisins = new ArrayList<Case>();

        for (Direction dir : Direction.values()) {
            try {
                if (this.voisinExiste(src, dir))
                    voisins.add(this.getVoisin(src, dir));
            } catch (IllegalArgumentException e) {
                // Ca n'arrive pas car on vérifie en amont si un voisin existe
            } catch (NullPointerException e) {
                // Ça n'arrive pas car on itère sur les valeurs de Direction
            }
        }
        return voisins.iterator();
    }

    /**
     * Affiche la carte, les robots ainsi que les incendies dans un terminal.
     * <p>
     * La superposition des éléments est telle que: {@link Robot} > {@link Incendie}
     * > élément décoratif de la {@link Case} (forêt, roche, etc..).
     *
     * @return String
     */

    /* Avant d'implementer le GUI, premier affichage pour tester les résulats */
    @Override
    public String toString() {
        String chaine = "";
        Incendie incendie = null;
        /* on fait d'abord la map vierge */
        for (int lig = 0; lig < nbLignes; lig++) {
            for (int col = 0; col < nbColonnes; col++) {
                /* On check s'il y a un incendie ici */
                incendie = donnees.getIncendie(this.carte[lig][col]);
                if (incendie != null && !incendie.estEteint()) {
                    chaine += "🔥";
                } else if (donnees.getRobot(this.carte[lig][col]) != null) {
                    chaine += "🤖";
                } else {
                    switch (carte[lig][col].getNature()) {
                        case EAU:
                            // 💧
                            chaine += "💧";
                            break;
                        case FORET:
                            chaine += "🌲";
                            break;
                        case ROCHE:
                            chaine += "⛰️ ";
                            break;
                        case TERRAIN_LIBRE:
                            chaine += "⬜";
                            break;
                        case HABITAT:
                            chaine += "🏠";
                            break;
                        default:
                            break;
                    }
                }
            }
            chaine += '\n';
        }
        System.out.println("Map générée");
        return chaine;
    }
}
