package Events;

import Robot.Robot;
import Exception.*;

public class DeversementEau extends Evenement {
    int volume;
    public DeversementEau(long date, Robot robot, int vol){
        super(date, robot);
        this.volume = vol;
    }

    public void execute() {
        try{
            this.robot.deverserEau(this.volume);
        } catch (VolumeEauIncorrectException e){
            System.out.println(e);
        }
        
    }
}