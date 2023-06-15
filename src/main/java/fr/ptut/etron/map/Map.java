package fr.ptut.etron.map;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;


import fr.ptut.etron.utils.*;

public class Map extends Config {
    public static int MAX = 100;
    public static int MIN = 0;
    public int[][] matriceCarte;
    public LinkedList<Vector2> obstacleList;

    public Map() {

        this.matriceCarte = new int[Config.MAP_WIDTH][Config.MAP_HEIGHT];
        this.obstacleList = new LinkedList<Vector2>();
        for (int i = 0; i < Config.MAP_WIDTH; i++) {
            for (int j = 0; j < Config.MAP_HEIGHT; j++) {
                matriceCarte[i][j] = 0;
            }
        }

        genereVille();
        initObstacleList();
    }

    public void initObstacleList() {
        float x, y;
        x = 100;
        for (int i = 0; i < Config.MAP_WIDTH; i++) {
            for (int j = 0; j < Config.MAP_HEIGHT; j++) {
                y = 100 + j * Config.NB_PIX_CASE_HEIGHT;
                if(matriceCarte[i][j] == 1){
                    Vector2 v = new Vector2(x, y);
                    this.obstacleList.add(v);    
                }
            }
            x += Config.NB_PIX_CASE_WIDTH;
        }
    }


    public void genereVille() {
        // Initialisation de la carte avec des murs et des routes
        for (int i = 0; i < Config.MAP_WIDTH; i++) {
            for (int j = 0; j < Config.MAP_HEIGHT; j++) {
                this.matriceCarte[i][j] = 1; // mur
                if (i >= 0 && i < Config.MAP_HEIGHT && j >= 0 && j < Config.MAP_WIDTH) {
                    this.matriceCarte[i][j] = 0;// route
                }
            }
        }

        // Ajout de zones d'immeubles
        int TailleGrille = 5; // Taille de la grille de carrés
        int nbZones = (int) (Config.MAP_HEIGHT * Config.MAP_WIDTH * Config.CITY_CONCENTRATION / (TailleGrille * TailleGrille)); // Nombre de
        // zones
        // d'immeubles
        Random rand = new Random();
        for (int i = 0; i < nbZones; i++) {
            int debutLigne = rand.nextInt(Config.MAP_HEIGHT - TailleGrille) + 1;
            int debutColonne = rand.nextInt(Config.MAP_WIDTH - TailleGrille) + 1;
            int finLigne = debutLigne + rand.nextInt(TailleGrille - 2) + 1;
            int finColonne = debutColonne + rand.nextInt(TailleGrille - 2) + 1;
            for (int ligne = debutLigne; ligne <= finLigne; ligne++) {
                for (int col = debutColonne; col <= finColonne; col++) {
                    if(ligne >= 0 && ligne < Config.MAP_WIDTH && col >= 0 && col < Config.MAP_HEIGHT)
                        this.matriceCarte[ligne][col] = 1; // Immeuble
                }
            }
        }
    }


    public void propageObstacle(int largeurObstacle) {
        int x = (int) (Math.random() * (MAX - MIN));
        int y = (int) (Math.random() * (MAX - MIN));
        for (int i = x; i < (x + largeurObstacle); i++) {
            for (int j = y; j < y + largeurObstacle; j++) {

                if (i < Config.MAP_WIDTH && j < Config.MAP_HEIGHT) {
                    this.matriceCarte[i][j] = 1;
                }
            }
        }
    }


    public void renderMap(Graphics g) {
        int x = 100;
        int y;
        for (int i = 0; i < Config.MAP_WIDTH; i++) {
            for (int j = 0; j < Config.MAP_HEIGHT; j++) {
                y = 100 + j * Config.NB_PIX_CASE_HEIGHT;
                if(this.matriceCarte[i][j] == 1){
                    g.setColor(Color.gray);
                    g.fillRect(x, y, Config.NB_PIX_CASE_WIDTH, Config.NB_PIX_CASE_HEIGHT);
                }
            }
            x += Config.NB_PIX_CASE_WIDTH;
        }
    }
}
