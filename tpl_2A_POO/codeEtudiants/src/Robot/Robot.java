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
    protected boolean verseEau;
    // interventionUnitaire est un diviseur de volume
    protected int interventionUnitaire;
    protected int tempsInterventionUnitaire;
    protected int tempsRemplissage;
    // pour l'affichage du robot
    protected Direction directionImage;

    // todo à expliquer
    protected Robot(Case position, int volumeEau, double vitesse, DonneesSimulation donnees, int interventionUnitaire,
            int tempsInterventionUnitaire, int tempsRemplissage) {
        this(position, volumeEau, vitesse, donnees);
        this.interventionUnitaire = interventionUnitaire;
        this.tempsInterventionUnitaire = tempsInterventionUnitaire;
        this.tempsRemplissage = tempsRemplissage;
        this.directionImage = Direction.SUD;
        this.verseEau = false;
    }

    // todo à expliquer
    public Robot(Case position, int volumeEau, double vitesse, DonneesSimulation donnees) {
        this.position = position;
        this.volumeEau = volumeEau;
        this.vitesse = vitesse;
        this.donnees = donnees;
        this.incendie = null;
        this.disponible = true;
        this.verseEau = false;
    }

    /**
     * Renvoie interventionUnitaireVolume du {@link Robot}, correspondant à un
     * diviseur du volumeMaxRéservoir du {@link Robot}.
     * 
     * @return int
     */
    public int getInterventionUnitaireVolume() {
        return this.interventionUnitaire;
    }

    /**
     * Renvoie le temps nécessaire pour déverser un volume
     * {@link #interventionUnitaire} sur un incendie.
     * 
     * @return int
     */
    public int getTempsInterventionUnitaire() {
        return this.tempsInterventionUnitaire;
    }

    /**
     * Renvoie le temps pour remplir le réservoir.
     * 
     * @return int
     */
    public int getTempsRemplissage() {
        return this.tempsRemplissage;
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

    public boolean peutEteindre(Incendie incendie) {
        return this.getVitesse(incendie.getPosition().getNature()) != 0;
    }

    public boolean getVerseEau() {
        return this.verseEau;
    }

    public void setVerseEau(boolean verseEau) {
        this.verseEau = verseEau;
    }

    /**
     * 
     * @param incendie
     * @return boolean
     */
    public boolean affecteIncendie(Incendie incendie) {
        /* Si il a déjà un incendie, on renvoie false */
        if (this.incendie == null) {
            // Graphe graphe = new Graphe(this.donnees, this.donnees.getCarte());
            // Chemin chemin = graphe.cheminDestination(this.position,
            // incendie.getPosition(), this);

            // /* Si il existe un chemin, on accepte */
            // if (chemin != null) {
            this.incendie = incendie;
            return true;
            // }
        }
        // Sinon false
        return false;
    }

    /**
     * @param simulateur
     */
    private void stepRemplir(Simulateur simulateur) {
        simulateur.ajouteEvenement(new DebutAction(0, this, simulateur));
        simulateur.ajouteEvenement(new RemplissageEau(this.tempsRemplissage, this, simulateur));
        simulateur.ajouteEvenement(new FinAction(this.tempsRemplissage + 1, this, simulateur));
    }

    public void checkIncendie() {
        if (this.incendie.estEteint()) {
            this.incendie = null;
        }
    }

    /**
     * @param simulateur
     */
    private void stepEteindre(Simulateur simulateur) {
        int dateCumule = 0;
        int eauVerse = 0;
        simulateur.ajouteEvenement(new DebutAction(dateCumule, this, simulateur));

        // Pas besoin de check volumeEau >= interventionUnitaire car son multiple
        // while (eauVerse < volumeEau && this.incendie.getIntensite() - eauVerse > 0) {
        // eauVerse += this.interventionUnitaire;
        // dateCumule += this.tempsInterventionUnitaire;
        simulateur.ajouteEvenement(
                new DeversementEau(this.tempsInterventionUnitaire, this, this.interventionUnitaire, simulateur,
                        this.tempsInterventionUnitaire));
        // }

        // if (this.incendie.getIntensite() - eauVerse <= 0) {
        // this.incendie = null;
        // }
        // simulateur.ajouteEvenement(new FinAction(dateCumule + 1, this, simulateur));
    }

    /**
     * @param simulateur
     */
    public void nextStep(Simulateur simulateur) {
        this.disponible = false;
        Graphe graphe = new Graphe(this.donnees, this.donnees.getCarte(), this);
        Chemin chemin = null;

        /* Si on a pas d'eau, peu importe l'affectation d'incendie */
        if (this.volumeEau == 0) {
            if (this.peutRemplir()) {
                this.stepRemplir(simulateur);
                return;
            }
            /* On cherche le chemin vers l'eau la plus proche */
            chemin = graphe.cheminRemplir();
            chemin.cheminToEvent(simulateur);
            return;
        }

        /* Si on se trouve sur l'incendie affecté */
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

        /* Volume eau pas vide, et pas d'incendie affecté */
        this.disponible = true;
        return;
    }

    /**
     * @param vol
     * @throws VolumeEauIncorrectException
     */
    public void eteinsIncendie(int vol) throws VolumeEauIncorrectException {
        if (this.incendie != null && this.incendie.getPosition() == this.position) {
            this.deverserEau(vol);
        }
    }

    /**
     * @param disponible
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * @return boolean
     */
    public boolean isDisponible() {
        return this.disponible;
    }

    /**
     * Renvoie un Robot du {@link TypeRobot} donnée, à la {@link Case} position,
     * et avec la vitesse donnée.
     * <p>
     * Throws:
     * <p>
     * {@link VitesseIncorrectException} si,
     * pour un type donnée, la vitesse est incorrecte.
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
                /* Volume d'eau temporairement à 0, changé dans le constructeur */
                /* Enlevé vitesse à 0, on suppose qu'on commence avec réservoir plein */
                // todo à voir
                return new Chenilles(position, vitesse, donnees);
            case DRONE:
                // appeller le constructeur du robot drone
                return new Drone(position, vitesse, donnees);
            case ROUES:
                // appeller le constructeur du robot roues
                return new Roues(position, vitesse, donnees);
            case PATTES:
                // appeller le constructeur du robot pattes ( pas un copié-collé)
                return new Pattes(position, donnees);
            default:
                // SI TYPE EST NULL CA VA BUG ici
                // todo
                throw new NoSuchElementException("Le  type robot " + type.toString() + " n'existe pas!");
        }
    }

    /**
     * Renvoie la position du robot.
     * 
     * @return Case
     */
    public Case getPosition() {
        return this.position;
    }

    /**
     * @return TypeRobot
     */
    public TypeRobot getType() {
        return this.type;
    }

    /**
     * @param positionCase
     * @return boolean
     */
    public boolean canMove(Case positionCase) {
        return this.getVitesse((positionCase).getNature()) != 0;
    }

    /**
     * Change la position du Robot.
     * 
     * @param positionCase
     * @throws TerrainIncorrectException
     */
    private void setPosition(Case positionCase) throws TerrainIncorrectException {
        if (!this.canMove(positionCase)) {
            System.out.println("Le robot ne peut pas se déplacer sur ce terrain");
        } else {
            // A changer pue le seum cette partie du code
            this.position = positionCase;
        }
    }

    /**
     * Renvoie true si le Robot est deplacé à la {@link Case} destination, sinon
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
     * Renvoie true si le Robot est deplacé à la case dans la {@link Direction} dir,
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
     * Set le volume d'eau du Robot.
     * 
     * @param volumeEau
     */
    public void setVolumeEau(int volumeEau) {
        this.volumeEau = volumeEau;
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
     * Set la vitesse du Robot.
     * 
     * @param vitesse
     */
    public void setVitesse(double vitesse) {
        this.vitesse = vitesse;
    }

    /**
     * @return boolean
     */
    public boolean peutRemplir() {
        return this.peutRemplir(this.position);
    }

    /**
     * @return double
     */
    // ME TAPEZ PAS
    // NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
    // public DonneesSimulation getDonnees(){
    // return this.donnees;
    // }
    /**
     * Renvoie la vitesse du {@link Robot} selon le {@link NatureTerrain}.
     * Le parametre nature doit être non null.
     * 
     * @param nature
     * @return double
     */
    public abstract double getVitesse(NatureTerrain nature);

    public abstract boolean peutRemplir(Case position);

    public abstract void deverserEau(int vol) throws VolumeEauIncorrectException;

    public abstract void remplirReservoir() throws TerrainIncorrectException;
}