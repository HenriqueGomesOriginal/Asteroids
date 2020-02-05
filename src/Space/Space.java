package Space;

import GFX.BufferedImageLoader;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;


public class Space
  extends Canvas
  implements Runnable, KeyListener
{
  public static int width = 1250; public static int height = 1000;
  


  private int Max_Asteroid = 3;
  private final int type = 2;
  private boolean gameover = false;
  
  public boolean W = false; public boolean S = false; public boolean A = false; public boolean D = false; public boolean SPACE = true;
  private static final long serialVersionUID = 1L;
  public static Space space;
  private static JFrame frame;
  
  public Space() {}
  
  public static void Frame() {
    frame = new JFrame();
    frame.setDefaultCloseOperation(3);
    frame.setResizable(false);
    frame.setSize(width + 6, height + 29);
    frame.setVisible(true);
  }
  
  public void Start()
  {
    if (running) return;
    running = true;
    
    new Thread(this, "Main Thread").start();
    addKeyListener(this);
  }
  
  public void Sprite()
  {
    random = new Random();
    
    BufferedImageLoader loader = new BufferedImageLoader();
    try {
      spritesheet = loader.loadImage("SpriteSheet.png");
    }
    catch (IOException localIOException) {}
    
    background = new Background(this);
    bullets = new ArrayList();
    asteroid = new ArrayList();
    player = new Player(bullets, this);
    
    for (int i = 0; i < Max_Asteroid; i++) {
      asteroid.add(new Asteroid(this, 2, random.nextInt(width), random.nextInt(height)));
    }
  }
  
  public void tick()
  {
    if (!gameover) {
      for (int i = 0; i < bullets.size(); i++)
      {
        ((Bullet)bullets.get(i)).tick();
      }
      
      for (int i = 0; i < asteroid.size(); i++)
      {
        ((Asteroid)asteroid.get(i)).tick(this);
      }
      player.tick(this);
      
      if (CollisionA()) {
        gameover = true;
      }
      if (!CollisionB()) {}
    }
  }
  
  public void render() {
    BufferStrategy bs = getBufferStrategy();
    
    if (bs == null)
    {
      createBufferStrategy(3);
      return;
    }
    
    Graphics2D g = (Graphics2D)bs.getDrawGraphics();
    
    background.render(g);
    
    if (gameover) {
      g.setFont(new Font("Arial", 100, 40));
      g.drawString("GAME OVER", width / 2 - 100, height / 2);
    }
    else {
      for (int i = 0; i < asteroid.size(); i++)
      {
        ((Asteroid)asteroid.get(i)).render(g);
      }
      
      for (int i = 0; i < bullets.size(); i++)
      {
        ((Bullet)bullets.get(i)).render(g);
        if (((Bullet)bullets.get(i)).ShouldRemove()) {
          bullets.remove(i);
          i--;
        }
      }
      g.setColor(Color.white);
      g.setFont(new Font("Arial", 100, 40));
      g.drawString("" + score, 100, 50);
      
      player.render(g);
    }
    g.dispose();
    bs.show();
  }
  
  private Random random;
  private int score;
  private int iscore;
  public void run() { Sprite();
    
    double target = 60.0D;
    double NanoTick = 1.0E9D / target;
    long lastTime = System.nanoTime();
    long timer = System.currentTimeMillis();
    double unprocessed = 0.0D;
    int fps = 0;
    int tps = 0;
    

    while (running)
    {
      long now = System.nanoTime();
      unprocessed += (now - lastTime) / NanoTick;
      lastTime = now;
      boolean canRender;
      if (unprocessed >= 1.0)
      {
        tick();
        unprocessed -= 1.0;
        tps++;
        canRender = true;
      } else { canRender = false;
      }
      try {
        Thread.sleep(1);
      }
      catch (InterruptedException localInterruptedException) {}
      
      if (canRender) {
        render();
        fps++;
      }
      
      if (System.currentTimeMillis() - 1000L > timer)
      {
        timer += 1000L;
        System.out.printf("FPS = %d | TPS = %d\n", new Object[] { Integer.valueOf(fps), Integer.valueOf(tps) });
        fps = 0;
        tps = 0;
      }
    }
  }
  
  public void splitAsteroids(Asteroid a) {
    if (a.getType() == 2)
    {
      asteroid.add(new Asteroid(this, 1, a.getX(), a.getY()));
      asteroid.add(new Asteroid(this, 1, a.getX(), a.getY()));
    } else if (a.getType() == 1)
    {
      asteroid.add(new Asteroid(this, 0, a.getX(), a.getY()));
      asteroid.add(new Asteroid(this, 0, a.getX(), a.getY()));
    }
  }
  
  public boolean CollisionA()
  {
    Rectangle rect1 = player.getBounds();
    
    for (int i = 0; i < asteroid.size(); i++) {
      if (rect1.intersects(((Asteroid)asteroid.get(i)).getBounds())) { return true;
      }
    }
    return false;
  }
  
  public boolean CollisionB()
  {
    for (int i = 0; i < bullets.size(); i++) {
      for (int j = 0; j < asteroid.size(); j++) {
        if (((Bullet)bullets.get(i)).getBounds().intersects(((Asteroid)asteroid.get(j)).getBounds())) {
          Asteroid a = (Asteroid)asteroid.get(j);
          int tip = asteroid.get(j).type;
          asteroid.remove(j);
          bullets.remove(i);
          splitAsteroids(a);
          
          Score(tip);
          return true;
        }
      }
    }
    
    return false;
  }
  
  private void Score(int type)
  {
    if (type == 0) { score += 100;iscore += 100; }
    if (type == 1) { score += 50;iscore += 50; }
    if (type == 2) { score += 20;iscore += 20;
    }
    if (Max_Asteroid * 520 == iscore)
    {
      iscore = 0;
      player = null;
      player = new Player(bullets, this);
      Max_Asteroid += 1;
      
      for (int i = 0; i < Max_Asteroid; i++) {
        asteroid.add(new Asteroid(this, 2, random.nextInt(width), random.nextInt(height)));
      }
    }
  }
  
  public BufferedImage getSpriteSheet() {
    return spritesheet;
  }
  
  public void keyPressed(KeyEvent e)
  {
    int key = e.getKeyCode();
    
    if (key == 87)
    {
      W = true;
    } else if (key == 83)
    {
      S = true;
    }
    if (key == 65)
    {
      A = true;
    }
    if (key == 68)
    {
      D = true;
    }
    if (key == 32)
    {
      SPACE = true;
    }
  }
  
  public void keyReleased(KeyEvent e)
  {
    int key = e.getKeyCode();
    
    if (key == 87)
    {
      W = false;
    } else if (key == 83)
    {
      S = false;
    }
    if (key == 65)
    {
      A = false;
    }
    if (key == 68)
    {
      D = false;
    }
    if (key == 32)
    {
      SPACE = false; } }
  
  private boolean running;
  private BufferedImage spritesheet;
  private Player player;
  private Background background;
  private ArrayList<Asteroid> asteroid;
  private ArrayList<Bullet> bullets;
  public void keyTyped(KeyEvent e) {}
  
  public static void main(String[] args) { Frame();
    Space space = new Space();
    frame.add(space);
    space.Start();
  }
}
