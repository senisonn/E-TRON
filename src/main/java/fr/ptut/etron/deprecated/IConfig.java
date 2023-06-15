package fr.ptut.etron.deprecated;

import java.awt.*;

public interface IConfig {

    // Informations sur la génération de la carte
    int LARGEUR_CARTE = 50;
    int HAUTEUR_CARTE = 50;
    int NB_PIX_CASE = 20;
    int POSITION_X = 0;
    int POSITION_Y = 0; // Position de la fen�tre
    int NB_JOUEUR = 20;
    double DENSITEVILLE = 0.5;
    Color[] tabCouleur = {Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW};
    Color violetFluo = new Color(255, 0, 255, 200);// R:100, V:0 , B:100, Transparence:78
    /**
     * Type Element
     */
    enum TypesElement {
        OBSTACLE, JOUEUR, ROUTE, BONUS,
        /* TYPE RELATIF AU RETRECISSEMENT DE LA MAP */NUAGE,
        /* TYPE INDIQUANT LA TRAINEE D'UN JOUEUR */ TRAINEE
    }
}