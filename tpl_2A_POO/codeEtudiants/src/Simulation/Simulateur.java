class Simulateur implements Simulable
{
    private GUISimulator gui;

    private DonneesSimulation donnesSimulation;
    public Simulateur(GUISimulator gui, DonneesSimulation donnees)
    {
        this.gui = gui;
        gui.setSimulable(this);
        this.donnees = donnees;
    }
}