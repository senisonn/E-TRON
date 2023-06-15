package fr.ptut.etron.deprecated;

import java.awt.*;
import java.util.LinkedList;

public class Joueur extends Element implements IConfig, Runnable {

    private int id;
    private DeprecatedPosition p;
    private int idGroupe;
    private int nbVie;
    private LinkedList<Trainee> tab_trainee;
    private int etat;
    private int vitesseD;
    private boolean visible;
    private char direction;

    public Joueur(int id, DeprecatedPosition p, Color c) {
        super(p, IConfig.TypesElement.JOUEUR, c);

        this.id = id;

        this.tab_trainee = new LinkedList<>();

        this.etat = 0;                  // 0 = non boosted, 1 = boosted, 2 = mort
        this.nbVie = 3;
        this.setVisible(true);
        this.direction = 'd';
    }

    public Joueur(int id, int idGroupe, DeprecatedPosition p, Color c) {
        this(id, p, c);
        this.idGroupe = idGroupe;
    }


    public synchronized int collision() {
        setNbVie(this.nbVie--);

        if (this.nbVie <= 0) {
            this.visible = false;
            this.etat = 2;
            return 0;
        }

        // fantomatique(j1);
        return 1;

    }

    public synchronized void vitesseDBoost() {
        if (this.etat == 1)
            setVitesseD(2);
    }


    public synchronized void ressuciter() {
        this.setVisible(false);
        this.etat = 2;
        Thread t = new Thread(this);
        t.start();


    }

    @Override
    public void run() {
        try {
            // le joueur est rendu invisible pendant 2sec avant qu'il soit ressuscité
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setVisible(true);
        this.setEtat(0);
    }

    public synchronized void seDeplacer(int newX, int newY) {
        this.tab_trainee.add(new Trainee(this.getP(), this.getCouleur()));
        this.setP(new DeprecatedPosition(newX, newY));
    }

    public synchronized void majTabTrainee(int i, DeprecatedPosition p) {
        this.tab_trainee.get(i).setP(p);
    }

    public synchronized void seDessiner(Graphics g) {
        g.setColor(Color.BLACK); // Parce qu'on est forcément sur une route
        g.fillRect(this.getP().getX() * IConfig.NB_PIX_CASE, this.getP().getY() * IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE); // On dessine la route
        g.setColor(this.getCouleur()); // On change la couleur pour dessiner le joueur
        g.fillOval(this.getP().getX() * IConfig.NB_PIX_CASE, this.getP().getY() * IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE); // On dessine le joueur
        for (Trainee t : this.tab_trainee) {
            t.seDessiner(g);
        }
    }


    // GETTER AND SETTER

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbVie() {
        return nbVie;
    }

    public void setNbVie(int nbVie) {
        this.nbVie = nbVie;
    }

    public int getVitesseD() {
        return vitesseD;
    }

    public void setVitesseD(int vitesseD) {
        this.vitesseD = vitesseD;
    }

    public char getDirection() {
        return this.direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public int getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(int idGroupe) {
        this.idGroupe = idGroupe;
    }

    public LinkedList<Trainee> getT() {
        return tab_trainee;
    }

    public void setT(LinkedList<Trainee> t) {
        this.tab_trainee = t;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
}