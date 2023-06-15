package fr.ptut.etron.particle.explosion;

import fr.ptut.etron.particle.Particle;
import fr.ptut.etron.particle.ParticleEmitter;
import fr.ptut.etron.utils.Vector2;

import java.util.Random;

public class ExplosionParticleEmitter extends ParticleEmitter {

    public static final int MAX_PARTICLES = 60;
    private static final Random random = new Random();

    public ExplosionParticleEmitter(Vector2 position) {
        super(position, 90);

        for (int i = 0; i < MAX_PARTICLES; i++) {
            final Vector2 velocity = new Vector2(random.nextFloat() * 10f - 5f, random.nextFloat() * 10f - 5f);
            getParticles().add(new Particle(position.clone(), velocity, getLife(), 10));
        }
    }
}
