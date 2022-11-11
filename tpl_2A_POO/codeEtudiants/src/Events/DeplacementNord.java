package Events;

public class DeplacementNord {
    public DeplacementNord(long date) {
        super(date);
    }

    public void execute() {
        this.robot.moveRobotDirection(Direction.NORD);
        // DonneesSimulation donnees = this.simulateur.getDonnees();
        // Iterator<Robot> robots = donnees.getRobots();
        // Robot robot = robots.next();
        // // faut verifier que c pas nul!
        // // Robot robot = donnees.getRobot();
    }
}
