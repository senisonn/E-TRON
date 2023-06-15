package fr.ptut.etron.deprecated;

import java.awt.Dimension;
import java.awt.BorderLayout;

import javax.swing.JFrame;

public class FenetreJeu extends JFrame {

    private static final long serialVersionUID = 1L;
    private PanneauJeu pj;

    public FenetreJeu() {
        super();
        
        /* Window informations */
        this.setTitle("E-Tron");
        this.setPreferredSize(new Dimension(1280, 720));

        /* Game panel */
        this.pj = new PanneauJeu();
        this.getContentPane().add(pj, BorderLayout.CENTER);

        this.pack();

        /* Window settings */
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.setVisible(true);
    }

    public PanneauJeu getPj() {
        return pj;
    }

}
