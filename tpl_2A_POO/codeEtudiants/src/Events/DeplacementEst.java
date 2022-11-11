package Events;

public class DeplacementEst {
    public DeplacementEst(long date) {
        super(date);
    }

    public void execute() {
        this.robot.moveRobotDirection(Direction.EST);
        // DonneesSimulation donnees = this.simulateur.getDonnees();
        // Iterator<Robot> robots = donnees.getRobots();
        // Robot robot = robots.next();
        // // faut verifier que c pas nul!
        // // Robot robot = donnees.getRobot();
    }

}
