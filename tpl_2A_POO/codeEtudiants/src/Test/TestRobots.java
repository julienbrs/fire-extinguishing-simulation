package Test;
import io.LecteurDonnees;
import Simulation.DonneesSimulation;
import Carte.*;
import Robot.*;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

public class TestRobots {

    public static void main(String[] args) {
        System.out.println("\n \n Lecture des donnees...\n");
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            DonneesSimulation donnees = LecteurDonnees.creeDonneesSimulation(args[0]);
            System.out.println("Lecture des donnees terminee");
            Carte carte = donnees.getCarte();
            /* Affichage de la carte */
            System.out.println(carte);

            /* on récupere les incendies */
            HashMap<Case, Incendie> incendies = donnees.getIncendies();

            /* On récupère les robots de la simulation */
            HashMap<Case, Robot> robots = donnees.getRobots();

            /* On récupère le premier robot de la liste */
            Robot robot = robots.values().iterator().next();
            Case caseRobot = robot.getPosition();

            System.out.println(robot.toString() + robots.get(caseRobot).toString());

            Scanner scanner = new Scanner(System.in);

            System.out.println("STOP to stop");
            // System.out.print("EST/OUEST/NORD/SUD: ");
            String chaine = "";
            Direction dir = null;
            while(!chaine.equals("STOP"))
            {
                System.out.print("EST/OUEST/NORD/SUD: ");
                chaine = scanner.nextLine();
                try {
                    dir = Direction.valueOf(chaine);
                    // if(carte.voisinExiste(caseRobot, dir))
                    // {
                    //     caseRobot = carte.getVoisin(caseRobot, dir);
                    //     robot.setPosition(caseRobot);
                    //     System.out.println("Case robot: "+ caseRobot);
                    // }

                    robot.moveRobotDirection(dir);
                } catch (IllegalArgumentException e) {
                    System.out.println(chaine + " n'est pas un input valide! STOP pour arreter.");
                }
                System.out.println(carte);
            }
            /* On test la méthode getVoisin() */
/*             
            System.out.println("Le robot est dans le coin en bas à droite de la carte");
            System.out.println("getVoisin Nord");
            carte.getVoisin(robot.getPosition(), Direction.NORD);
            System.out.println("get Voisin Ouest");
            carte.getVoisin(robot.getPosition(), Direction.OUEST);
            System.out.println("get Voisin Est, on doit avoir une exception");
            carte.getVoisin(robot.getPosition(), Direction.EST);
            System.out.println("getVoisin Sud, on doit avoir une exception");
            carte.getVoisin(robot.getPosition(), Direction.SUD);
 */
            
            /* On test la méthode moveRobotDirection pour bouger le robot */
/*        
            System.out.println("Initialement, le robot est en " + caseRobot);
            System.out.println("On bouge le robot d'une case vers le nord");
            robot.moveRobotDirection(Direction.NORD);
            System.out.println("Le robot est maintenant en " + robot.getPosition() + "\n");
            System.out.println("On bouge le robot d'une case vers l'ouest" );
            robot.moveRobotDirection(Direction.OUEST);
            System.out.println("Le robot est maintenant en " + robot.getPosition() + "\n");
            System.out.println("On bouge le robot d'une case vers l'est" );
            robot.moveRobotDirection(Direction.EST);
            System.out.println("Le robot est maintenant en " + robot.getPosition() + "\n");
            //todo : peut etre ici ne pas lever une exception mais juste ne pas bouger le robot ? comme ce que j'ai fais ligne 61 pour la roche
            // System.out.println("On rebouge le robot d'une case vers l'est alors qu'il est au bord, ça devrait lever une exception" );
            // robot.moveRobotDirection(Direction.EST);

            System.out.println("On bouge le robot d'une case vers le Nord alors qu'il y a de la roche, le robot ne bougera pas" );
            robot.moveRobotDirection(Direction.NORD);
            System.out.println("Le robot est maintenant en " + robot.getPosition() + "\n");
*/
            

            /* On test la méthode moveRobot */
/* 
            System.out.println("Initialement, le robot est en " + caseRobot);
            System.out.println("On 'teleporte' le robot en (0,0)");
            robot.moveRobot(carte.getCase(0, 1));
            System.out.println("Le robot est maintenant en " + robot.getPosition() + "\n");
            System.out.println("On 'teleporte' le robot en (1,1) alors qu'il y a de l'eau, le robot ne bougera pas");
            robot.moveRobot(carte.getCase(1, 1));
            System.out.println("Le robot est maintenant en " + robot.getPosition() + "\n");
 */

            /* On test la méthode RemplirReservoir */
/* 
            System.out.println("Initialement, le robot a " + robot.getVolumeEau() + " litres d'eau");
            System.out.println("On veut remplir le reservoir du robot sans être proche d'une source");
            robot.remplirReservoir();
            System.out.println("Le robot a maintenant " + robot.getVolumeEau() + " litres d'eau\n");

            System.out.println("On teleporte le robot à côté d'une source");
            robot.moveRobot(carte.getCase(0, 1));
            System.out.println("On remplit le reservoir du robot");
            robot.remplirReservoir();
            System.out.println("Le robot a maintenant " + robot.getVolumeEau() + " litres d'eau\n");
 */

            /* On test la méthode deverserEau */
/* 
            //On affiche d'abord tous les incendies
            System.out.println("Les incendies sont : ");
            for (Incendie incendie : incendies.values()) {
                System.out.println(incendie);
            }

            System.out.println("On teleporte le robot à côté d'une case avec de l'eau");
            robot.moveRobot(carte.getCase(0, 1));
            System.out.println("Le robot est maintenant en " + robot.getPosition() + "\n");
            System.out.println("On remplit le reservoir du robot");
            robot.remplirReservoir();
            System.out.println("Le robot a maintenant " + robot.getVolumeEau() + " litres d'eau\n");
            System.out.println("On teleporte le robot sur une case incendie");
            robot.moveRobot(carte.getCase(2, 0));
            System.out.println("Le robot est maintenant en " + robot.getPosition() + "\n");


            Incendie incendie = donnees.getIncendie(carte.getCase(2, 0));
            System.out.println("L'incendie à éteindre est: " + incendie);

            System.out.println("On essaie de deverser tout le reservoir du robot sur l'incendie");
            robot.deverserEau(robot.getVolumeEau());
            System.out.println("Le robot a maintenant " + robot.getVolumeEau() + " litres d'eau\n");
*/


        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}