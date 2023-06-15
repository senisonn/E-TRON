package fr.ptut.etron.particle;

import fr.ptut.etron.utils.Vector2;

import java.awt.*;

public class Particle {
    private Vector2 position;
    private Vector2 velocity;
    private int life, maxLife;
    private float size, maxSize;
    private int r, g, b, a;

    public Particle(Vector2 position, Vector2 velocity, int life, float size) {
        this.position = position;
        this.velocity = velocity;
        this.life = life;
        this.maxLife = life;
        this.size = size;
        this.maxSize = size;

        this.r = 255;
        this.g = 255;
        this.b = 255;
        this.a = 255;
    }

    public void update() {
        position.add(velocity);
        life--;
    }
    public void render(Graphics g) {
        g.setColor(new Color(this.r, this.g, this.b, this.a));
        g.fillRect((int) (position.getX() - size * 0.5f), (int) (position.getY() - size * 0.5f), (int) size, (int) size);
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public Vector2 getVelocity() {
        return velocity;
    }
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
    public int getLife() {
        return life;
    }
    public void setLife(int life) {
        this.life = life;
    }
    public int getMaxLife() {
        return maxLife;
    }
    public float getSize() {
        return size;
    }
    public void setSize(float size) {
        this.size = size;
    }
    public float getMaxSize() {
        return maxSize;
    }
    public int getR() {
        return r;
    }
    public void setR(int r) {
        this.r = r;
    }
    public int getG() {
        return g;
    }
    public void setG(int g) {
        this.g = g;
    }
    public int getB() {
        return b;
    }
    public void setB(int b) {
        this.b = b;
    }
    public int getA() {
        return a;
    }
    public void setA(int a) {
        this.a = a;
    }
    public void setRGBA(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}
