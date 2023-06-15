package fr.ptut.etron.deprecated;

import java.awt.*;

public class Bonus extends Element {
    private DeprecatedPosition p;

    public Bonus() {
        super(new DeprecatedPosition((int) (Math.random() * IConfig.NB_PIX_CASE), (int) (Math.random() * IConfig.NB_PIX_CASE)), IConfig.TypesElement.BONUS, IConfig.violetFluo);
    }

    public void seDessiner(Graphics g) {
        int demiTaille = IConfig.NB_PIX_CASE / 2;
        g.setColor(this.getCouleur());
        g.drawLine(this.getP().getX() - demiTaille, this.getP().getY(), this.getP().getX() + demiTaille, this.getP().getY());
        g.drawLine(this.getP().getX(), this.getP().getY() - demiTaille, this.getP().getX(), this.getP().getY() + demiTaille);
    }
}
