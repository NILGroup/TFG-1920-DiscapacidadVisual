package beacons.server_v1.routes;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public class Posicion {
    private double pX;
    private double pY;

    public Posicion() {
        this.pX = 0.0D;
        this.pY = 0.0D;
    }

    public Posicion(double x, double y) {
        this.pX = x;
        this.pY = y;
    }

    public Posicion(Posicion p) {
        this.pX = p.pX;
        this.pY = p.pY;
    }

    public Posicion clone() {
        Posicion p = new Posicion();
        p.setPX(this.pX);
        p.setPY(this.pY);
        return p;
    }

    public boolean equals(Object o) {
        Posicion p = (Posicion)o;
        return this.pX == p.getPX() && this.pY == p.getPY();
    }

    public void modificar(double pX, double pY) {
        this.pX = pX;
        this.pY = pY;
    }

    public double getPX() {
        return this.pX;
    }

    public double getPY() {
        return this.pY;
    }

    public void setPX(double x) {
        this.pX = x;
    }

    public void setPY(double y) {
        this.pY = y;
    }
}