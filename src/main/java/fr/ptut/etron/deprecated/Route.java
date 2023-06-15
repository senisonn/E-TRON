package fr.ptut.etron.deprecated;


import java.awt.*;

public class Route extends Element {

    private static final long serialVersionUID = 1L;

    public Route(DeprecatedPosition p) {
        super(p, IConfig.TypesElement.ROUTE, Color.BLACK);
    }
}
