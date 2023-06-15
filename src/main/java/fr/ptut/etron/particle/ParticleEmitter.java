package fr.ptut.etron.particle;

import fr.ptut.etron.utils.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ParticleEmitter {

    public static final float PI = (float) Math.PI;
    public static final float PI2 = PI * 2;
    public static final float PI_2 = PI / 2;
    public static final float PI_4 = PI / 4;
    public static final float PI_6 = PI / 6;
    public static final float PI_8 = PI / 8;

    private Vector2 position;
    private List<Particle> particles;
    private int life, maxLife;
    private int ticks;

    public ParticleEmitter(Vector2 position, int life) {
        this.position = position;
        this.particles = new ArrayList<>();
        this.life = life;
        this.maxLife = life;
        this.ticks = 0;
    }
    public ParticleEmitter(Vector2 position) {
        this(position, 0);
    }

    public void update() {
        for (Particle particle : new LinkedList<>(particles)) {
            particle.update();
            if (particle.getLife() <= 0) {
                particles.remove(particle);
            }
        }
        if (life > 0)
            life--;
        ticks++;
    }
    public void render(Graphics g) {
        for (Particle particle : particles) {
            particle.render(g);
        }
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public List<Particle> getParticles() {
        return particles;
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
    public int getTicks() {
        return ticks;
    }
    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

}
