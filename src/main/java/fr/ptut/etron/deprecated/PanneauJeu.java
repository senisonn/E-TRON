package fr.ptut.etron.deprecated;

import javax.swing.*;
import java.awt.*;

public class PanneauJeu extends JPanel {

    private static final long serialVersionUID = 1L;

    Carte carte = new Carte();

    public PanneauJeu() {
        super(new BorderLayout());
        this.setBackground(Color.GRAY);
        this.setPreferredSize(new Dimension(800, 600));
        this.setOpaque(true);

        this.setVisible(true);
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        carte.toutDessiner(g);
    }
}

