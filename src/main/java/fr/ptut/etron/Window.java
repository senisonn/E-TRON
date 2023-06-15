package fr.ptut.etron;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import org.java_websocket.WebSocket;

import fr.ptut.etron.particle.explosion.ExplosionParticleEmitter;
import fr.ptut.etron.particle.smoke.SmokeParticleEmitter;
import fr.ptut.etron.particle.ParticleEmitter;
import fr.ptut.etron.player.Player;
import fr.ptut.etron.map.Map;
import fr.ptut.etron.utils.*;
import fr.ptut.etron.bonus.*;

public class Window extends JPanel implements Runnable, KeyListener {

    public static Font OPENSANS_FONT;
    public static Font OPENSANS_FONT_12;
    public static FontMetrics OPENSANS_FONT_METRICS_12;
    public static Font OPENSANS_FONT_32;
    public static FontMetrics OPENSANS_FONT_METRICS_32;
    public static Font OPENSANS_FONT_64;
    public static FontMetrics OPENSANS_FONT_METRICS_64;
    static {
        InputStream fontStream = Window.class.getResourceAsStream("/fonts/OpenSans-Bold.ttf");
        try {
            OPENSANS_FONT = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        OPENSANS_FONT_12 = OPENSANS_FONT.deriveFont(12f);
        OPENSANS_FONT_32 = OPENSANS_FONT.deriveFont(32f);
        OPENSANS_FONT_64 = OPENSANS_FONT.deriveFont(64f);
    }

    private GameManager gameManager;
    private boolean running;
    private boolean debug;
    private JFrame frame;
    private Map map;
    private Clip clip;


    private int fps;
    private int tps;
    private int tickTime;
    private long invincibilityCurrentTime;

    private List<ParticleEmitter> particleEmitters;

    public Window(Map map) {
        this.gameManager = App.getGameManager();
        this.running = false;
        this.debug = true;
        this.map = map;
        setMinimumSize(new Dimension(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT));
        setMaximumSize(new Dimension(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT));
        setPreferredSize(new Dimension(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT));
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File("/home/senisonn/Bureau/Ptut/ptut_etron/src/main/resources/music/MainGame.wav")));
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

        setBackground(Color.BLACK);

        frame = new JFrame(Config.WINDOW_TITLE);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });
        frame.setLayout(new BorderLayout());
        
        frame.add(this, BorderLayout.CENTER);
        frame.pack();
        
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        OPENSANS_FONT_METRICS_12 = getGraphics().getFontMetrics(OPENSANS_FONT_12);
        OPENSANS_FONT_METRICS_32 = getGraphics().getFontMetrics(OPENSANS_FONT_32);
        OPENSANS_FONT_METRICS_64 = getGraphics().getFontMetrics(OPENSANS_FONT_64);

        this.particleEmitters = new ArrayList<>();
    }

    public synchronized void start() {
        new Thread(this).start();
        running = true;
    }
    public synchronized void stop() {
        running = false;
        System.out.println("Game stopped");
    }

    @Override
    public void run() {
        final float tickRate = 0.06f;
        boolean shouldRender = false;

        clip.loop(Clip.LOOP_CONTINUOUSLY);
        
        long previousCurrentTime = System.currentTimeMillis();
        long currentTime;
        float ticksToCompute = 0;

        long previousMonitoringTime = System.currentTimeMillis();
        long currentMonitoringTime;
        long bonusSpawningTime = System.currentTimeMillis();
        long bonusTimeUpdater;
        long invincibilityStart = System.currentTimeMillis();
        long invincibilityCheck;
        int tps = 0;
        int fps = 0;

        long previousTickTime = System.currentTimeMillis();
        long currentTickTime;

        while (running) {
            currentTime = System.currentTimeMillis();
            bonusTimeUpdater = System.currentTimeMillis();
            invincibilityCheck = System.currentTimeMillis();
            ticksToCompute += (currentTime - previousCurrentTime) * tickRate;
            previousCurrentTime = currentTime;
            
            while (ticksToCompute > 0) {
                tps++;

                update();
                ticksToCompute--;

                shouldRender = true;
            }

            currentTickTime = System.currentTimeMillis();
            tickTime = (int) (currentTickTime - previousTickTime);
            previousTickTime = currentTickTime;
            this.invincibilityCurrentTime = invincibilityCheck - invincibilityStart;

            if(bonusTimeUpdater - bonusSpawningTime == 20000)
            {
                Bonus b = new Bonus();
                this.gameManager.bonusList.add(b);
                bonusSpawningTime = bonusTimeUpdater;
            }

            if (invincibilityCheck - invincibilityStart > 60000) {
                final List<Player> players = new ArrayList<>(this.gameManager.getPlayerMap().values());
                for (Player player : players) {
                    player.isOnStart = false;
                }
            }
            
            if (shouldRender) {
                fps++;

                paintImmediately(0, 0, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);

                shouldRender = false;
            }
            
            currentMonitoringTime = System.currentTimeMillis();
            if (currentMonitoringTime - previousMonitoringTime >= 1000) {
                previousMonitoringTime = currentMonitoringTime;
                this.fps = fps;
                this.tps = tps;
                fps = 0;
                tps = 0;
            }
        }
    }
    
    public void update() {
        if (this.gameManager.getGameState() == GameManager.GameState.STARTED) {
            new HashMap<>(this.gameManager.getPlayerMap()).forEach((id, player) -> player.update());
        }

        for (ParticleEmitter particleEmitter : new LinkedList<>(particleEmitters)) {
            particleEmitter.update();
            if (particleEmitter.getLife() <= 0)
                particleEmitters.remove(particleEmitter);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Toolkit.getDefaultToolkit().sync();
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setFont(OPENSANS_FONT_12);

        if (this.gameManager.getGameState() != GameManager.GameState.ENDED) {
            renderMap(g);
            renderGame(g);
        } else {
            renderWin(g);
        }


        for (ParticleEmitter particleEmitter : new LinkedList<>(particleEmitters)) {
            particleEmitter.render(g);
        }
        g.setColor(Color.white);
        g.drawString("Temps: " + this.invincibilityCurrentTime/1000, Config.WINDOW_WIDTH/2-20, 20);

        if (debug) {
            g.setColor(Color.WHITE);
            g.drawString("FPS: " + fps, 10, 20);
            g.drawString("TPS: " + tps, 10, 40);
            g.drawString("Tick time: " + tickTime, 10, 60);
            g.drawString("Window particle emitters: " + particleEmitters.size(), 10, 80);
        }
    }
    public void renderGame(Graphics g) {
        new HashMap<>(this.gameManager.getPlayerMap()).forEach((id, player) -> player.render(g));
        for(int i = 0; i < this.gameManager.bonusList.size(); i++){
            this.gameManager.bonusList.get(i).renderBonus(g);
        }
    }

    public void renderMap(Graphics g) {
        this.map.renderMap(g);
    }

    public void renderWin(Graphics g) {
        g.setColor(Config.WIN_BACKGROUND_COLOR);
        g.fillRect(0, 0, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(OPENSANS_FONT_64);

        final Player winner = this.gameManager.getPlayerMap().values().iterator().next();
        String winText = "Bravo " + winner.getName() + " !";
        int textWidth = OPENSANS_FONT_METRICS_64.stringWidth(winText);
        g.drawString(winText, (Config.WINDOW_WIDTH - textWidth) / 2, (Config.WINDOW_HEIGHT + 64) / 2);

        g.setFont(OPENSANS_FONT_32);
        String statsText = "Statistiques : " + winner.getKills() + " kills pour " + winner.getDeaths() + " morts";
        textWidth = OPENSANS_FONT_METRICS_32.stringWidth(statsText);
        g.drawString(statsText, (Config.WINDOW_WIDTH - textWidth) / 2, (Config.WINDOW_HEIGHT + 32) / 2 + 64);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        final int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE) {
            stop();
            try {
                Thread.sleep(500);
                System.exit(0);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (keyCode == KeyEvent.VK_F) {
            debug = !debug;
        }
        else if (keyCode == KeyEvent.VK_P) {
            this.gameManager.start();
        }
        else if (keyCode == KeyEvent.VK_E) {
            this.particleEmitters.add(new ExplosionParticleEmitter(new Vector2(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT / 2)));
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }

}