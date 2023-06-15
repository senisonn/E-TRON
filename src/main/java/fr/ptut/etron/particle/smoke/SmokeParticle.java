package fr.ptut.etron.particle.smoke;

import fr.ptut.etron.particle.Particle;
import fr.ptut.etron.particle.ParticleEmitter;
import fr.ptut.etron.player.Player;
import fr.ptut.etron.utils.Vector2;

import java.util.Random;

public class SmokeParticle extends Particle {

    private static final Random random = new Random();

    private int halfLife;
    public SmokeParticle(Player player) {
        super(player.getPosition().clone(), null, random.nextInt(15) + 5, random.nextInt(9) + 1);
        this.halfLife = (int) (getMaxLife() * 0.5f);

        double angle = random.nextDouble() * ParticleEmitter.PI_6 * 2 - ParticleEmitter.PI_6;
        switch (player.getDirection()) {
            case EAST:
                angle += ParticleEmitter.PI;
                break;
            case SOUTH:
                angle += ParticleEmitter.PI_2;
                break;
            case NORTH:
                angle -= ParticleEmitter.PI_2;
                break;
        }
        setVelocity(new Vector2((float) Math.cos(angle) * 5f, (float) -Math.sin(angle) * 5f));

        setRGBA(255, random.nextInt(200), 0, 255);
    }

    @Override
    public void update() {
        super.update();

        if (getLife() <= halfLife)
            setA((int) (getLife() / (float) halfLife * 255));
        setR(getR() - 1);
        setSize((int) (getLife() / (float) getMaxLife() * getMaxSize()));
    }

}
