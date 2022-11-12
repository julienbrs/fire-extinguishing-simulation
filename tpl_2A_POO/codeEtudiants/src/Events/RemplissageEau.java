package Events;

import Robot.Robot;
import Exception.*;

public class RemplissageEau extends Evenement {
    public RemplissageEau(long date, Robot robot){
        super(date, robot);
    }

    public void execute() {
        try{
            this.robot.remplirReservoir();
        } catch (TerrainIncorrectException e){
                System.out.println(e);
            }
        
    }
}