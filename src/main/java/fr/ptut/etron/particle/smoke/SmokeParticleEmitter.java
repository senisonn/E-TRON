package fr.ptut.etron.particle.smoke;

import fr.ptut.etron.particle.ParticleEmitter;
import fr.ptut.etron.player.Player;
import fr.ptut.etron.utils.Vector2;

public class SmokeParticleEmitter extends ParticleEmitter {

    private Player player;
    public SmokeParticleEmitter(Player player) {
        super(player.getPosition());

        this.player = player;
    }

    @Override
    public void update() {
        super.update();

        if (getTicks() == 1) {
            getParticles().add(new SmokeParticle(player));
        } else if (getTicks() == 2) {
            setTicks(0);
        }
    }

}
