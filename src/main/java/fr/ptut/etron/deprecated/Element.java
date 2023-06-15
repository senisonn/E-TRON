package fr.ptut.etron.deprecated;

import java.awt.*;
import java.io.Serializable;

public abstract class Element implements IConfig, Serializable {
    private static final long serialVersionUID = 1L;
    private final Color couleur;
    private DeprecatedPosition p;                                            // Position de l'element
    private TypesElement type;                                    // Type de l'element

    public Element(DeprecatedPosition p, TypesElement type, Color couleur) {
        this.p = p;
        this.type = type;
        this.couleur = couleur;
    }


    // GETTER AND SETTER
    public TypesElement getType() {
        return type;
    }

    public void setType(TypesElement type) {
        this.type = type;
    }

    public DeprecatedPosition getP() {
        return p;
    }

    public void setP(DeprecatedPosition p) {
        this.p = p;
    }

    public Color getCouleur() {
        return couleur;
    }
}
