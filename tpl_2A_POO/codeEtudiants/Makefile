# Ensimag 2A POO - TP 2018/19
# ============================
#
# Ce Makefile permet de compiler le test de l'ihm en ligne de commande.
# Alternative (recommandee?): utiliser un IDE (eclipse, netbeans, ...)
# Le but est ici d'illustrer les notions de "classpath", a vous de l'adapter
# a votre projet.
#
# Organisation:
#  1) Les sources (*.java) se trouvent dans le repertoire src
#     Les classes d'un package toto sont dans src/toto
#     Les classes du package par defaut sont dans src
#
#  2) Les bytecodes (*.class) se trouvent dans le repertoire bin
#     La hierarchie des sources (par package) est conservee.
#     L'archive bin/gui.jar contient les classes de l'interface graphique
#
# Compilation:
#  Options de javac:
#   -d : repertoire dans lequel sont places les .class compiles
#   -classpath : repertoire dans lequel sont cherches les .class deja compiles
#   -sourcepath : repertoire dans lequel sont cherches les .java (dependances)

all: testInvader testLecture io Simulation Robot Carte

Exception:
	javac -d bin -classpath bin -sourcepath src src/Exception/*.java
io:
	javac -d bin -classpath bin -sourcepath src src/io/*.java
Simulation:
	javac -d bin -classpath bin:bin/gui.jar -sourcepath src src/Simulation/*.java
Robot:
	javac -d bin -classpath bin -sourcepath src src/Robot/*.java
Carte:
	javac -d bin -classpath bin -sourcepath src src/Carte/*.java
Events:
	javac -d bin -classpath bin:bin/gui.jar -sourcepath src src/Events/*.java
testInvader:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/Test/TestInvader.java

testLecture:
	javac -d bin -classpath bin -sourcepath src src/Test/TestLecteurDonnees.java

testRobots:
	javac -d bin -classpath bin -sourcepath src src/Test/TestRobots.java
testSimulateurKO:
	javac -d bin -classpath bin:bin/gui.jar -sourcepath src src/Test/TestSimulateurKO.java
testSimulateurOK:
	javac -d bin -classpath bin:bin/gui.jar -sourcepath src src/Test/TestSimulateurOK.java
testSimulateurBonus:
	javac -d bin -classpath bin:bin/gui.jar -sourcepath src src/Test/TestSimulateurBonus.java


# Execution:
# on peut taper directement la ligne de commande :
#   > java -classpath bin:bin/gui.jar TestInvader
# ou bien lancer l'execution en passant par ce Makefile:
#   > make exeInvader
exeSimulateurKO: testSimulateurKO
	java -classpath bin:bin/gui.jar Test/TestSimulateurKO cartes/carteSimulation.map

exeSimulateurOK: testSimulateurOK
	java -classpath bin:bin/gui.jar Test/TestSimulateurOK cartes/carteSimulation.map

exeSimulateurBonus: testSimulateurBonus
	java -classpath bin:bin/gui.jar Test/TestSimulateurBonus cartes/carteSimulation.map

exeInvader:
	java -classpath bin:bin/gui.jar Test/TestInvader

exeLecture:
	java -classpath src:bin Test/TestLecteurDonnees cartes/carteSujet.map

exeRobots: testRobots
	java -classpath src:bin Test/TestRobots cartes/cartePattes.map

clean:
	find  bin/ -not \( -name 'gui.jar' -or -name 'bin' \) -delete
