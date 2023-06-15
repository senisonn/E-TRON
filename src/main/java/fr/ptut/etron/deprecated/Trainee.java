package fr.ptut.etron.deprecated;

import java.awt.*;

public class Trainee extends Element {
    private DeprecatedPosition p;
    private int opcacite;

    public Trainee(DeprecatedPosition p1, Color c) {
        super(p1, IConfig.TypesElement.TRAINEE, c);
        this.opcacite = 255;
    }

    public void diminuerOpacite(int points) {
        this.opcacite = Math.max(0, this.opcacite - points);
    }

    public void seDessiner(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) this.opcacite / 255); // Création d'un composite avec une opacité
        g2d.setComposite(comp);
        g.setColor(this.getCouleur());
        g.fillRect(this.getP().getX() * IConfig.NB_PIX_CASE, this.getP().getY() * IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE);
    }
}