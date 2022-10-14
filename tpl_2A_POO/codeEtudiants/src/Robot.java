public abstract class Robot
{
    private Case position;
    private int volumeEau;
    protected double vitesse;

    public Robot(Case position, int volumeEau, double vitesse)
    {
        this.position = position;
        this.volumeEau = volumeEau;
        this.vitesse = vitesse;
    }
    public Case getPosition() {
        return position;
    }
    public setPosition(Case positionCase)
    {
        this.position = positionCase;
    }
    public abstract double getVitesse(NatureTerrain nature);
    public abstract deverserEau(int vol);
    public abstract remplirReservoir();
}