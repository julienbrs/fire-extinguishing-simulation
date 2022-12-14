package Robot;

import java.util.NoSuchElementException;
import Simulation.DonneesSimulation;
import Simulation.Simulateur;
import Strategie.Chemin;
import Strategie.Graphe;
import Carte.*;
import Events.DebutAction;
import Events.DeversementEau;
import Events.FinAction;
import Events.RemplissageEau;
import Exception.*;

public abstract class Robot {

    protected Case position;
    protected Incendie incendie;
    protected int volumeEau;
    protected double vitesse;
    protected TypeRobot type;
    protected DonneesSimulation donnees;
    protected boolean disponible;
    // interventionUnitaire est un diviseur de volume
    protected int interventionUnitaire;
    protected int tempsInterventionUnitaire;
    protected int tempsRemplissage;
    // pour l'affichage du robot
    protected Direction directionImage;

    protected Robot(Case position, int volumeEau, double vitesse, DonneesSimulation donnees, int interventionUnitaire,
            int tempsInterventionUnitaire, int tempsRemplissage) {
        this.position = position;
        this.volumeEau = volumeEau;
        this.vitesse = vitesse;
        this.donnees = donnees;
        this.incendie = null;
        this.disponible = true;
        this.interventionUnitaire = interventionUnitaire;
        this.tempsInterventionUnitaire = tempsInterventionUnitaire;
        this.tempsRemplissage = tempsRemplissage;
        this.directionImage = Direction.SUD;
    }

    /**
     * Renvoie la direction actuelle du {@link Robot}.
     * 
     * @return {@link Direction}
     */
    public Direction getDirectionImage() {
        return this.directionImage;
    }

    /**
     * Change la valeur {@link Direction} du {@link Robot} par
     * {@link Direction DirectionImage}.
     */
    public void setDirectionImage(Direction directionImage) {
        this.directionImage = directionImage;
    }

    /**
     * @param incendie
     * @return boolean
     */
    public boolean peutEteindre(Incendie incendie) {
        return this.getVitesse(incendie.getPosition().getNature()) != 0;
    }

    /**
     * On regarde si le robot est disponible, si oui on regarde si il peut
     * intervenir sur l'incendie, puis on renvoie un boolean.
     * 
     * @param incendie
     * @return boolean
     */
    public boolean affecteIncendie(Incendie incendie) {
        /* Si il a d??j?? un incendie, on renvoie false */
        if (this.incendie == null) {
            this.incendie = incendie;
            return true;
        }
        return false;
    }

    /**
     * On ajoute un ??v??nement de remplissage de l'eau au simulateur au temps
     * ad??quat, ainsi que les ??v??nements de d??but et de fin d'action.
     * 
     * @param simulateur
     */
    private void stepRemplir(Simulateur simulateur) {
        simulateur.ajouteEvenement(new DebutAction(0, this, simulateur));
        simulateur.ajouteEvenement(new RemplissageEau(this.tempsRemplissage, this, simulateur));
        simulateur.ajouteEvenement(new FinAction(this.tempsRemplissage + 1, this, simulateur));
    }

    /**
     * On regarde si l'incendie est ??teint, si oui on le supprime du simulateur et
     * on le met ?? null. Renvoie vrai si il y avait un incendie affect?? qui a ??t??
     * ??teint, false sinon.
     */
    public boolean checkIncendie() {
        if (this.incendie != null && this.incendie.estEteint()) {
            this.incendie = null;
            return true;
        }
        return false;
    }

    /**
     * On ajoute un ??v??nement de d??versement de l'eau au simulateur au temps
     * ad??quat, ainsi que les ??v??nements de d??but et de fin d'action.
     * 
     * @param simulateur
     */
    private void stepEteindre(Simulateur simulateur) {
        simulateur.ajouteEvenement(new DebutAction(0, this, simulateur));
        simulateur.ajouteEvenement(
                new DeversementEau(this.tempsInterventionUnitaire, this, this.interventionUnitaire, simulateur,
                        this.tempsInterventionUnitaire));
    }

    /**
     * On passe ?? l'??tape suivante du {@link Robot}, on regarde s'il a encore de
     * l'eau, si non on trouve le plus proche point d'eau et on se d??place vers lui,
     * sinon on regarde si on est sur un {@link Incendie}, si oui on l'??teint, sinon
     * on trouve le plus proche {@link Incendie} et on se d??place vers lui.
     * 
     * @param simulateur
     */
    public void nextStep(Simulateur simulateur) {
        this.disponible = false;
        Graphe graphe = new Graphe(this.donnees.getCarte(), this);
        Chemin chemin = null;

        /* Si on a pas d'eau, peu importe l'affectation d'incendie */
        if (this.volumeEau == 0) {
            if (this.peutRemplir()) {
                this.stepRemplir(simulateur);
                return;
            }
            /* On cherche le chemin vers l'eau la plus proche */
            chemin = graphe.cheminRemplir();
            if (chemin != null) {
                chemin.cheminToEvent(simulateur);
                return;
            }
        }

        /* Si on se trouve sur l'incendie affect?? */
        if (this.incendie != null && this.position == this.incendie.getPosition()) {
            this.stepEteindre(simulateur);
            return;
        } else if (this.incendie != null && !this.incendie.estEteint()) {
            /* Sinon on se rend sur l'incendie en question */
            chemin = graphe.cheminDestination(this.incendie.getPosition());
            if (chemin != null) {
                chemin.cheminToEvent(simulateur);
                return;
            }
        }

        /* Volume eau n'est pas vide, et pas d'incendie affect?? */
        this.disponible = true;
        return;
    }

    /**
     * Si l'incendie n'est pas ??teint et que le robot se trouve sur l'incendie, on
     * d??verse de l'eau.
     * 
     * @param vol
     * @throws VolumeEauIncorrectException
     */
    public void eteinsIncendie(int vol) throws VolumeEauIncorrectException {
        if (this.incendie != null && this.incendie.getPosition() == this.position) {
            this.deverserEau(vol);
        }
    }

    /**
     * On d??finit {@link Robot#disponible} ?? la valeur du param??tre.
     * 
     * @param disponible
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * On renvoie {@link Robot#disponible}.
     * 
     * @return boolean
     */
    public boolean isDisponible() {
        return this.disponible;
    }

    /**
     * Renvoie un Robot du {@link TypeRobot} donn??e, ?? la {@link Case} position,
     * ?? la {@link #vitesse} donn??e et un champ {@link DonneesSimulation
     * DonneesSimulation}.
     * <p>
     * Throws:
     * <p>
     * {@link VitesseIncorrectException} si,
     * pour un type donn??e, la vitesse est incorrecte.
     * <p>
     * {@link NoSuchElementException} si le type du robot est incorrect.
     * 
     * @param type
     * @param position
     * @param vitesse
     * @param donnees
     * @return Robot
     * @throws NoSuchElementException
     * @throws VitesseIncorrectException
     */
    public static Robot newRobot(TypeRobot type, Case position, double vitesse, DonneesSimulation donnees)
            throws NoSuchElementException, VitesseIncorrectException {
        switch (type) {
            case CHENILLES:
                // appeller le constructeur du robot chenilles
                return new Chenilles(position, vitesse, donnees);
            case DRONE:
                // appeller le constructeur du robot drone
                return new Drone(position, vitesse, donnees);
            case ROUES:
                // appeller le constructeur du robot roues
                return new Roues(position, vitesse, donnees);
            case PATTES:
                // appeller le constructeur du robot pattes
                return new Pattes(position, donnees);
            default:
                // N'arrive jamais car on ne cr??e des robots que dans LecteurDonnees
                // o?? les erreurs sont d??j?? g??r??es
                throw new NoSuchElementException("Le  type robot " + type.toString() + " n'existe pas!");
        }
    }

    /**
     * Renvoie la position du {@link Robot}.
     * 
     * @return {@link Case}
     */
    public Case getPosition() {
        return this.position;
    }

    /**
     * Renvoie le type du {@link Robot}.
     * 
     * @return {@link TypeRobot}
     */
    public TypeRobot getType() {
        return this.type;
    }

    /**
     * Renvoie l'incendie affect?? au {@link Robot}.
     * 
     * @return {@link Incendie}
     */
    public Incendie getIncendie() {
        return this.incendie;
    }

    /**
     * Renvoie un boolean indiquant si le {@link Robot} peut se d??placer
     * sur la {@link Case} donn??e.
     * 
     * @param positionCase
     * @return boolean
     */
    public boolean canMove(Case positionCase) {
        return this.getVitesse((positionCase).getNature()) != 0;
    }

    /**
     * Change la position du {@link Robot} ?? la {@link Case} donn??e si celle-ci est
     * accessible.
     * 
     * @param positionCase
     * @throws TerrainIncorrectException
     */
    private void setPosition(Case positionCase) throws TerrainIncorrectException {
        if (!this.canMove(positionCase)) {
            System.out.println("Le robot ne peut pas se d??placer sur ce terrain");
        } else {
            // A changer pue le seum cette partie du code
            this.position = positionCase;
        }
    }

    /**
     * Renvoie true si le Robot est deplac?? ?? la {@link Case} destination, sinon
     * false.
     * 
     * @param destination
     * @return boolean
     */
    public boolean moveRobot(Case destination) {
        try {
            this.setPosition(destination);
            return true;
        } catch (TerrainIncorrectException e) {
            return false;
        }
    }

    /**
     * Renvoie true si le Robot est deplac?? ?? la case dans la {@link Direction} dir,
     * sinon false.
     * 
     * @param direction
     * @return boolean
     */
    public boolean moveRobotDirection(Direction direction) {
        Carte carte = this.donnees.getCarte();
        if (carte.voisinExiste(this.position, direction))
            return moveRobot(carte.getVoisin(this.position, direction));
        else
            return false;
    }

    /**
     * Renvoie le volume d'eau du Robot.
     * 
     * @return int
     */
    public int getVolumeEau() {
        return this.volumeEau;
    }

    /**
     * Renvoie la vitesse du Robot.
     * 
     * @return double
     */
    public double getVitesse() {
        return this.vitesse;
    }

    /**
     * Renvoie un boolean indiquant si le {@link Robot} peut remplir son r??servoir
     * d'eau ?? sa {@link Case} actuelle.
     * 
     * @return boolean
     */
    public boolean peutRemplir() {
        return this.peutRemplir(this.position);
    }

    /**
     * Renvoie la vitesse du {@link Robot} selon le {@link NatureTerrain}.
     * Le parametre nature doit ??tre non null.
     * 
     * @param nature
     * @return double
     */
    public abstract double getVitesse(NatureTerrain nature);

    /**
     * Renvoie vrai si le {@link Robot} peut remplir son r??servoir sur la
     * {@link Case} position, sinon renvoie false.
     * 
     * @param position
     * @return boolean
     */
    public abstract boolean peutRemplir(Case position);

    /**
     * V??rifie s'il y a bien un {@link Incendie} sur la case courante. Si oui, on
     * diminue son intensit?? avec la m??thode {@link Incendie#decreaseIntensite
     * decreaseIntensite}.
     * <p>
     * Jette {@link VolumeEauIncorrectException} si le volume d'eau
     * disponible est n??gatif.
     * 
     * @param vol
     * @throws VolumeEauIncorrectException
     */
    public abstract void deverserEau(int vol) throws VolumeEauIncorrectException;

    /**
     * Remplit compl??tement le r??servoir du {@link Robot}
     * 
     * Jette {@link TerrainIncorrectException} s'il ne peut pas remplir son
     * r??servoir si {@link #peutRemplir()} renvoit false.
     * 
     * @throws TerrainIncorrectException
     */
    public abstract void remplirReservoir() throws TerrainIncorrectException;
}