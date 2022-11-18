#!/bin/bash
if [ $# -lt 2 ];
then 
    echo "Utilisation: ./testerCarte.sh <1 si propagation, 0 sinon> <nom carte>";
else
    cd .;
    make;
    if [ $1 -eq 1 ];
    then
        java -classpath bin:bin/gui.jar Test/TestSimulationAvecPropagation $2;
    else
        java -classpath bin:bin/gui.jar Test/TestSimulation $2;
    fi;
fi;
