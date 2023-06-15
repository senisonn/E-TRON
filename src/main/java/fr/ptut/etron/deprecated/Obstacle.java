package fr.ptut.etron.deprecated;

import java.awt.*;

public class Obstacle extends Element {
    private static final long serialVersionUID = 1L;                                                      // Position de l'obstacle

    public Obstacle(DeprecatedPosition pos) {
        super(pos, IConfig.TypesElement.OBSTACLE, Color.DARK_GRAY);
    }

    public String toString() {
        return "Obstacle en " + this.getP();
    }

}
