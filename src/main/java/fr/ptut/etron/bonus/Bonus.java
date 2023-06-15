package fr.ptut.etron.bonus;

import java.util.Random;
import java.awt.*;

import fr.ptut.etron.utils.*;
import fr.ptut.etron.player.*;

public class Bonus{

    private Vector2 center;
    private int radius;
    private int bonusId;

    public static int healValue = 1;
    public static int lengthValue = 100;
    public static float speedValue = 1f; 



    public Bonus(){
        Random randomValue = new Random();
        this.center = new Vector2(randomValue.nextFloat()*Config.WINDOW_WIDTH, randomValue.nextFloat()*Config.WINDOW_HEIGHT);
        this.radius = 50;
        this.bonusId = randomValue.nextInt(3);
    }



    public void effect(Player player){
        switch(this.bonusId){
            case 0: heal(player);
                    break;
            case 1: lengthTrailAdd(player);
                    break;
            case 2: speedBoost(player);
                    break;
        }
    }


    public void renderBonus(Graphics g){
        switch (this.bonusId) {
            case 0:
                Color healColor = new Color( 15, 247, 4 , 80);
                g.setColor(healColor);
                g.fillOval((int)this.center.getX() - this.radius, (int)this.center.getY()-this.radius, this.radius*2, this.radius*2);
                break;
            case 1:
                Color lengthColor = new Color(4, 26, 247 , 80);
                g.setColor(lengthColor);
                g.fillOval((int)this.center.getX() - this.radius, (int)this.center.getY()-this.radius, this.radius*2, this.radius*2);
            case 2:
                Color speedColor = new Color(233, 247, 4, 80);
                g.setColor(speedColor);
                g.fillOval((int)this.center.getX() - this.radius, (int)this.center.getY()-this.radius, this.radius*2, this.radius*2);
            default:
                break;
        }
    }


    public void heal(Player player){
        player.addLives(healValue);
    }

    public void lengthTrailAdd(Player player){
        player.getTrail().addLength(lengthValue);
    }

    public void speedBoost(Player player){
        player.setSpeed(player.getSpeed() + speedValue);
    }

    public Vector2 getCenter(){
        return center;
    }

    public int getRadius(){
        return radius;
    }

    public int getBonusId(){
        return bonusId;
    }
}