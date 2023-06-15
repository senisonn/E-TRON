package fr.ptut.etron.player;

import java.awt.*;
import java.util.*;
import java.util.List;

import fr.ptut.etron.GameManager;
import fr.ptut.etron.particle.ParticleEmitter;
import fr.ptut.etron.particle.smoke.*;
import fr.ptut.etron.player.trail.Trail;
import fr.ptut.etron.bonus.*;
import fr.ptut.etron.utils.*;
import fr.ptut.etron.map.*;
import org.java_websocket.WebSocket;

public class Player {

    public static final int PLAYER_WIDTH = 10;
    public static final int PLAYER_HEIGHT = 25;
    public static final int PLAYER_INVINCIBILITY_MAX_TICKS = 180;
    public static final int PLAYER_FLASH_FLASHING_TICKS = 6;
    public static final int PLAYER_FLASH_VISIBLE_TICKS = 16;

    private static final Random rand = new Random();

    private GameManager gameManager;
    public fr.ptut.etron.map.Map map;
    private WebSocket webSocket;
    private int botId;
    private String name;
    private Color color;
    private Color flashingColor;
    private Vector2 position;
    private Direction direction;
    private Trail trail;
    private ParticleEmitter particleEmitter;
    private int lives;
    private int kills;
    private int deaths;
    private float speed;
    private int invincibilityTicks;
    private int flashTicks;
    private boolean isDead;
    public boolean isOnStart;

    public Player(GameManager gameManager, WebSocket webSocket, fr.ptut.etron.map.Map map) {
        this.gameManager = gameManager;
        this.map = map;
        this.webSocket = webSocket;
        this.botId = -1;
        this.name = Config.USERNAMES.get(rand.nextInt(Config.USERNAMES.size()));
        this.color = new Color(rand.nextInt(128) + 64, rand.nextInt(128) + 64, rand.nextInt(128) + 64);
        this.flashingColor = new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), 96);

        this.position = new Vector2(0, 0);
        this.direction = Direction.NORTH;
        this.trail = null;
        this.particleEmitter = new SmokeParticleEmitter(this);

        this.lives = 3;
        this.speed = 5f;
        this.invincibilityTicks = 0;
        this.flashTicks = 0;
        this.isDead = false;
        this.isOnStart = true;
    }
    public Player(GameManager gameManager, int botId, fr.ptut.etron.map.Map map) {
        this(gameManager, null, map);
        this.botId = botId;
    }

    public void update() {
        if (isDead)
            return;

        if(isOnStart)
            this.invincibilityTicks = PLAYER_INVINCIBILITY_MAX_TICKS;

        if (webSocket == null) {
            int shouldMove = rand.nextInt(20);
            if (shouldMove == 0)
                turnLeft();
            else if (shouldMove == 1)
                turnRight();
        }

        Vector2 vector = direction.getForwardVector().mult(speed);
        this.position.add(vector);

        if (trail == null) {
            trail = new Trail(this.position, this.direction);
        }
        final int length = (int) vector.simpleLength();
        trail.addLength(length);
        trail.getLastPart().addLength(length);
        trail.update();

        particleEmitter.update();

        collide();
    }
    private void collide() {
        final Vector2 playerTopLeft = getTopLeftCorner();
        final Vector2 playerBottomRight = getBottomRightCorner();
        int i;
        for(Vector2 v : this.map.obstacleList){
            double x = v.getX() + Config.NB_PIX_CASE_WIDTH;
            double y = v.getY() + Config.NB_PIX_CASE_HEIGHT;
            if (playerBottomRight.getX() >= v.getX()
            && playerTopLeft.getX() <= x
            && playerBottomRight.getY() >= v.getY()
            && playerTopLeft.getY() <= y) {
                kill();
                turnLeft();
                turnLeft();
                break;
            }
            if (invincibilityTicks > 0)
                break;
        }


        if (playerBottomRight.getX() >= Config.WINDOW_WIDTH || playerTopLeft.getX() <= 0
                || playerBottomRight.getY() >= Config.WINDOW_HEIGHT || playerTopLeft.getY() <= 0) {
            if (invincibilityTicks <= 0)
                kill();

            turnLeft();
            turnLeft();
            return;
        }

        for(i = 0; i < this.gameManager.bonusList.size(); i++){
            Bonus bonus = this.gameManager.bonusList.get(i);
            Vector2 v1 = new Vector2(0, 0);
            Vector2 v2 = new Vector2(0, 0);
            switch(this.direction){
                case NORTH:
                    v1 = new Vector2(playerTopLeft.getX(), playerTopLeft.getY());
                    v2 = new Vector2(playerBottomRight.getX(), playerTopLeft.getY());
                    break;
                case EAST:
                    v1 = new Vector2(playerBottomRight.getX(), playerTopLeft.getY());
                    v2 = new Vector2(playerBottomRight.getX(), playerBottomRight.getY());
                    break;
                case SOUTH:
                    v1 = new Vector2(playerTopLeft.getX(), playerBottomRight.getY());
                    v2 = new Vector2(playerBottomRight.getX(), playerBottomRight.getY());
                    break;
                case WEST:
                    v1 = new Vector2(playerTopLeft.getX(), playerBottomRight.getY());
                    v2 = new Vector2(playerTopLeft.getX(), playerTopLeft.getY());
                    break;
            }

            double x1 = Math.abs(bonus.getCenter().getX() - v1.getX());
            double x2 = Math.abs(bonus.getCenter().getX() - v2.getX());
            double y1 = Math.abs(bonus.getCenter().getY() - v1.getY());
            double y2 = Math.abs(bonus.getCenter().getY() - v2.getY());

            double distance1 = x1*x1 + y1*y1;
            double distance2 = x2*x2 + y2*y2;
            double distance3 = bonus.getRadius() * bonus.getRadius();
            if( distance1 <= distance3 || distance2 <= distance3){
                bonus.effect(this);
                this.gameManager.bonusList.remove(i);
                break;
            }
        }

        


        if (invincibilityTicks > 0) {
            invincibilityTicks--;
            return;
        }

        final List<Player> players = new ArrayList<>(this.gameManager.getPlayerMap().values());

        for (Player player : players) {
            if (player == this || player.getInvincibilityTicks() > 0 || player.getTrail() == null)
                continue;

            for (Trail.TrailPart trailPart : player.getTrail().getParts()) {
                final Vector2 trailTopLeft = trailPart.getTopLeftCorner();
                final Vector2 trailBottomRight = trailPart.getBottomRightCorner();

                if (playerBottomRight.getX() >= trailTopLeft.getX()
                    && playerTopLeft.getX() <= trailBottomRight.getX()
                    && playerBottomRight.getY() >= trailTopLeft.getY()
                    && playerTopLeft.getY() <= trailBottomRight.getY()) {
                    kill();
                    player.addLives(1);
                    break;
                }
            }

            if (invincibilityTicks > 0)
                break;
        }
    }
    public void render(Graphics g) {
        Color trailPlayerColor = color;
        if (invincibilityTicks > 0) {
            flashTicks++;
            if (flashTicks <= PLAYER_FLASH_FLASHING_TICKS) {
                trailPlayerColor = flashingColor;
            }
            if (flashTicks == PLAYER_FLASH_VISIBLE_TICKS + PLAYER_FLASH_FLASHING_TICKS)
                flashTicks = 0;
        }

        g.setColor(trailPlayerColor);
        if (this.trail != null)
            this.trail.render(g);

        particleEmitter.render(g);

        g.setColor(trailPlayerColor);
        switch (direction) {
            case NORTH:
              g.fillRect((int) (position.getX() - PLAYER_WIDTH * 0.5), (int) (position.getY() - PLAYER_HEIGHT), PLAYER_WIDTH, PLAYER_HEIGHT);
              break;
            case EAST:
              g.fillRect((int) (position.getX()), (int) (position.getY() - PLAYER_WIDTH * 0.5), PLAYER_HEIGHT, PLAYER_WIDTH);
              break;
            case SOUTH:
              g.fillRect((int) (position.getX() - PLAYER_WIDTH * 0.5), (int) (position.getY()), PLAYER_WIDTH, PLAYER_HEIGHT);
              break;
            case WEST:
              g.fillRect((int) (position.getX() - PLAYER_HEIGHT), (int) (position.getY() - PLAYER_WIDTH * 0.5), PLAYER_HEIGHT, PLAYER_WIDTH);
              break;
        }

        g.setColor(Color.WHITE);
        final Vector2 topLeftCorner = getTopLeftCorner();
        g.drawString(name + " (" + this.lives + ")", (int) topLeftCorner.getX(), (int) topLeftCorner.getY() + PLAYER_WIDTH);
    }

    public void turnLeft() {
        this.direction = this.direction.getAnticlockwise();

        if (this.trail != null) {
            this.trail.getLastPart().setStart(position.clone());
            this.trail.addPart(new Trail.TrailPart(position, this.direction));
        }
    }
    public void turnRight() {
        this.direction = this.direction.getClockwise();

        if (this.trail != null) {
            this.trail.getLastPart().setStart(position.clone());
            this.trail.addPart(new Trail.TrailPart(position, this.direction));
        }
    }

    public void kill() {
        invincibilityTicks = PLAYER_INVINCIBILITY_MAX_TICKS;

        subLives(1);

        if (lives <= 0) {
            isDead = true;
            this.gameManager.playerDiedEvent(this);
            trail = null;
        } else {
            trail = new Trail(this.position, this.direction);
        }
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }
    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }
    public int getBotId() {
        return botId;
    }
    public String getName() {
        return name;
    }
    public Color getColor() {
        return color;
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public Vector2 getTopLeftCorner() {
        switch (this.direction) {
            case NORTH: return new Vector2(position.getX() - PLAYER_WIDTH * 0.5f, position.getY() - PLAYER_HEIGHT);
            case EAST: return new Vector2(position.getX(), position.getY() - PLAYER_WIDTH * 0.5f);
            case SOUTH: return new Vector2(position.getX() - PLAYER_WIDTH * 0.5f, position.getY());
            case WEST: return new Vector2(position.getX() - PLAYER_HEIGHT, position.getY() - PLAYER_WIDTH * 0.5f);
        }
        return null;
    }
    public Vector2 getBottomRightCorner() {
        switch (this.direction) {
            case NORTH: return new Vector2(position.getX() + PLAYER_WIDTH * 0.5f, position.getY());
            case EAST: return new Vector2(position.getX() + PLAYER_HEIGHT, position.getY() + PLAYER_WIDTH * 0.5f);
            case SOUTH: return new Vector2(position.getX() + PLAYER_WIDTH * 0.5f, position.getY() + PLAYER_HEIGHT);
            case WEST: return new Vector2(position.getX(), position.getY() + PLAYER_WIDTH * 0.5f);
        }
        return null;
    }
    public Direction getDirection() {
        return direction;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public Trail getTrail() {
        return trail;
    }
    public int addLives(int amount) {
        this.lives += amount;
        this.kills += amount;
        if (this.webSocket != null)
            this.webSocket.send("lives:" + this.lives);
        return this.lives;
    }
    public int subLives(int amount) {
        this.lives -= amount;
        this.deaths += amount;
        if (this.webSocket != null)
            this.webSocket.send("lives:" + this.lives + ":" + this.gameManager.getPlayerMap().size());
        return this.lives;
    }
    public int getLives() {
        return lives;
    }
    public int getKills() {
        return kills;
    }
    public int getDeaths() {
        return deaths;
    }
    public float getSpeed() {
        return speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public int getInvincibilityTicks() {
        return invincibilityTicks;
    }
    public synchronized void setInvincibilityTicks(int value) {
        this.invincibilityTicks = value;
    }
    public boolean isDead() {
        return isDead;
    }
    public void setDead(boolean dead) {
        isDead = dead;
    }
}