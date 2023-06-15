package fr.ptut.etron.deprecated;

import java.io.Serializable;

/**
 * La classe Position nous permet de donner des positions spécifiques aux
 * elements de la carte et de pouvoir se situer dans la matrice
 *
 * @author Asmae NOUFOUSSI, Soheil BENABIDA, Lina BENALI
 */
public class DeprecatedPosition implements Serializable {

    private static final long serialVersionUID = 1L;
    private int x, y; // Coordonnées x et y de la position

    /**
     * Constructeur de base de Position
     */
    public DeprecatedPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean estVoisine(DeprecatedPosition pos) {
        return ((Math.abs(x - pos.x) <= 1) && (Math.abs(y - pos.y) <= 1));
    }

    public int distance(DeprecatedPosition p) {
        return (int) (Math.round((Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2))) * 100.0) / 100.0);
    }
}
