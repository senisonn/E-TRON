package fr.ptut.etron.deprecated;

import java.awt.*;
import java.util.Random;

public class Carte implements IConfig {
    public static int MAX = 100;
    public static int MIN = 0;
    public Element[][] matriceCarte;

    public Carte() {
        DeprecatedPosition pos;
        Element e;

        this.matriceCarte = new Element[LARGEUR_CARTE][HAUTEUR_CARTE];
        for (int i = 0; i < LARGEUR_CARTE; i++) {
            for (int j = 0; j < HAUTEUR_CARTE; j++) {
                pos = new DeprecatedPosition(i, j);                                // On crée une position
                e = new Route(pos);                                      // On crée une route à cette position
                matriceCarte[i][j] = e;
            }
        }

        genereVille();
    }


    public Joueur trouveJoueur(int id) {
        for (int i = 0; i < LARGEUR_CARTE; i++) {
            for (int j = 0; j < HAUTEUR_CARTE; j++) {
                if (matriceCarte[i][j] instanceof Joueur) {
                    Joueur joueur = (Joueur) this.matriceCarte[i][j];
                    if (joueur.getId() == id) {
                        return joueur;
                    }
                }
            }
        }
        return null;
    }


    public void genereVille() {
        // Initialisation de la carte avec des murs et des routes
        for (int i = 0; i < LARGEUR_CARTE; i++) {
            for (int j = 0; j < HAUTEUR_CARTE; j++) {
                this.matriceCarte[i][j] = new Obstacle(new DeprecatedPosition(i, j)); // mur
                if (i > 0 && i < HAUTEUR_CARTE - 1 && j > 0 && j < LARGEUR_CARTE - 1) {
                    this.matriceCarte[i][j] = new Route(new DeprecatedPosition(i, j));// route
                }
            }
        }

        // Ajout de zones d'immeubles
        int TailleGrille = 5; // Taille de la grille de carrés
        int nbZones = (int) (HAUTEUR_CARTE * LARGEUR_CARTE * DENSITEVILLE / (TailleGrille * TailleGrille)); // Nombre de
        // zones
        // d'immeubles
        Random rand = new Random();
        for (int i = 0; i < nbZones; i++) {
            int debutLigne = rand.nextInt(HAUTEUR_CARTE - TailleGrille) + 1;
            int debutColonne = rand.nextInt(LARGEUR_CARTE - TailleGrille) + 1;
            int finLigne = debutLigne + rand.nextInt(TailleGrille - 2) + 1;
            int finColonne = debutColonne + rand.nextInt(TailleGrille - 2) + 1;
            for (int ligne = debutLigne; ligne <= finLigne; ligne++) {
                for (int col = debutColonne; col <= finColonne; col++) {
                    this.matriceCarte[ligne][col] = new Obstacle(new DeprecatedPosition(ligne, col)); // Immeuble
                }
            }
        }
    }

    public void ajouterElement(Joueur j, DeprecatedPosition p) {
        this.matriceCarte[p.getX()][p.getY()] = j;
    }

    public void deplacer(Joueur j1, char d) {
        int xP = j1.getP().getX();
        int yP = j1.getP().getY();
        int xNewP = xP;
        int yNewP = yP;

        if (d == 'u')
            yNewP++;
        if (d == 'd')
            yNewP--;
        if (d == 'r')
            xNewP++;
        if (d == 'l')
            xNewP--;

        if (estValide(xNewP, yNewP)) {
            if (this.matriceCarte[xNewP][yNewP] instanceof Obstacle) {
                j1.collision();
            } else if (this.matriceCarte[xNewP][yNewP] instanceof Joueur) {
                Joueur j2 = (Joueur)this.matriceCarte[xNewP][yNewP];
                if (j1.getNbVie() > 0)
                    j1.collision();
                else
                    mort(j1);
                if (j2.getNbVie() > 0)
                    j2.collision();
                else
                    mort(j2);
            } else if (this.matriceCarte[xNewP][yNewP] instanceof Trainee) {
                j1.collision();
            } else {
                j1.seDeplacer(xNewP, yNewP);
                this.matriceCarte[xNewP][yNewP] = j1;
                this.matriceCarte[xP][yP] = new Route(new DeprecatedPosition(xP, yP));

            }
        } else {
            j1.collision();
        }
    }


    public boolean estValide(int x, int y) {
        return x < IConfig.LARGEUR_CARTE && x > 0 && y < IConfig.HAUTEUR_CARTE && y > 0;
    }


    public void mort(Joueur j) {
        int jx = j.getP().getX();
        int jy = j.getP().getY();
        DeprecatedPosition p = new DeprecatedPosition(jx, jy);

        j.setNbVie(j.getNbVie() - 1);

        if (j.getNbVie() <= 0) {
            j.setVisible(false);
            j.setEtat(2);
            this.matriceCarte[p.getX()][p.getY()] = new Route(p);

        } else {
            this.matriceCarte[p.getX()][p.getY()] = new Route(p);
            j.ressuciter();
            //remise du joueur à sa position
            p.setX(j.getP().getX());
            p.setY(j.getP().getY());
        }

    }

    public void propageObstacle(int largeurObstacle) {
        int x = (int) (Math.random() * (MAX - MIN));
        int y = (int) (Math.random() * (MAX - MIN));

        for (int i = x; i < (x + largeurObstacle); i++) {
            for (int j = y; j < y + largeurObstacle; j++) {

                if (i < LARGEUR_CARTE && j < HAUTEUR_CARTE) {
                    Obstacle o = new Obstacle(new DeprecatedPosition(i, j));
                    this.matriceCarte[i][j] = o;
                }
            }
        }
    }

    public void toutDessiner(Graphics g) {
        int x = IConfig.NB_PIX_CASE;
        int y;

        for (int i = 0; i < IConfig.LARGEUR_CARTE; i++) {
            for (int j = 0; j < IConfig.HAUTEUR_CARTE; j++) {
                y = j * IConfig.NB_PIX_CASE;
                switch (this.matriceCarte[i][j].getType()) {
                    case ROUTE: g.setColor(this.matriceCarte[i][j].getCouleur());
                                g.fillRect(x, y, IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE);
                                break;
                    case OBSTACLE:
                                g.setColor(this.matriceCarte[i][j].getCouleur());
                                g.fillRect(x, y, IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE);
                                break;
                    case NUAGE:
                                g.setColor(Color.WHITE);
                                g.fillRect(x, y, IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE);
                                break;
                    case JOUEUR: ((Joueur) this.matriceCarte[i][j]).seDessiner(g);
                                break;
                    case BONUS: ((Bonus) this.matriceCarte[i][j]).seDessiner(g);
                                break;
                }
            }
            x += IConfig.NB_PIX_CASE;
        }
    }

    // GETTER && SETTER
    public Element getTypePosition(int x, int y) {
        return this.matriceCarte[x][y];
    }

    public Element typeCaseCarte(int x, int y) {
        return matriceCarte[x][y];
    }

    public void setTypeCase(int x, int y, Element t) {
        this.matriceCarte[x][y] = t;
    }

}
