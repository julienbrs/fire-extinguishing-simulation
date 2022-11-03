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

io:
	javac -d bin -classpath bin -sourcepath src src/io/*.java
Simulation:
	javac -d bin -classpath bin:bin/gui.jar -sourcepath src src/Simulation/*.java
Robot:
	javac -d bin -classpath bin -sourcepath src src/Robot/*.java
Carte:
	javac -d bin -classpath bin -sourcepath src src/Carte/*.java
testInvader:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/Test/TestInvader.java

testLecture:
	javac -d bin -sourcepath src src/Test/TestLecteurDonnees.java

# Execution:
# on peut taper directement la ligne de commande :
#   > java -classpath bin:bin/gui.jar TestInvader
# ou bien lancer l'execution en passant par ce Makefile:
#   > make exeInvader
exeInvader: 
	java -classpath bin:bin/gui.jar TestInvader

exeLecture: 
	java -classpath bin TestLecteurDonnees cartes/carteSujet.map

clean:
	find  bin/ -not \( -name 'gui.jar' -or -name 'bin' \) -delete
