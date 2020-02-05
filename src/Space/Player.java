package Space;

import GFX.SpriteSheet;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;



public class Player
{
  public int width = 64; public int height = 64; public int count = 0;
  private double gravity = 0.99D; private double speedX = 0.0D; private double speedY = 0.0D;
  
  private final int MaxBullets = 6;
  private long lastTime = 0L;
  private double x;
  private double y;
  private double xVel;
  private double yVel;
  
  public Player(ArrayList<Bullet> bullets, Space space) {
    this.bullets = bullets;
    x = (Space.width / 2);
    y = (Space.height / 2);
    
    SpriteSheet ss = new SpriteSheet(space.getSpriteSheet());
    player = ss.grabImage(1, 1, 32, 32); }
  
  private double RotateVel;
  private double rotate;
  
  public void Teleport() { Random random = new Random();
    x = random.nextInt(Space.width - width);
    y = random.nextInt(Space.height - height); }
  
  private long Time;
  private ArrayList<Bullet> bullets;
  private BufferedImage player;
  public void Shoot() { Time = System.currentTimeMillis();
    
    if (lastTime < Time - 100L)
    {
      lastTime = Time;
      lastTime = System.currentTimeMillis();
      
      if (bullets.size() == 6) return;
      bullets.add(new Bullet(x, y, rotate));
    }
  }
  
  public void tick(Space space)
  {
    SpriteSheet ss = new SpriteSheet(space.getSpriteSheet());
    
    if (space.W) {
      speedY = yVel;
      speedX = xVel;
      
      yVel = Math.sin(Math.toRadians(rotate) - Math.toRadians(90.0D));
      xVel = Math.cos(Math.toRadians(rotate) - Math.toRadians(90.0D));
      
      yVel += speedY;
      xVel += speedX;
      
      player = ss.grabImage(2, 1, 32, 32);
    } else if (!space.W)
    {
      xVel *= gravity;
      yVel *= gravity;
      
      player = ss.grabImage(1, 1, 32, 32);
    }
    
    if (xVel > 25.0D) xVel = 25.0D; else if (xVel < -25.0D) xVel = -25.0D;
    if (yVel > 25.0D) yVel = 25.0D; else if (yVel < -25.0D) { yVel = -25.0D;
    }
    y += yVel * 0.3D;
    x += xVel * 0.3D;
    
    if (y + height < 0.0D) y = Space.height; else if (y - height > Space.height) y = (0 - height);
    if (x + width < 0 - width) x = Space.width; else if (x - width > Space.width) { x = (0 - width);
    }
    if ((space.S) && (count < 1))
    {
      Teleport();
      count += 1;
    }
    
    if (space.A) {
      rotate += 1.0D;
      rotate += RotateVel;
      RotateVel += 1.0D;
    } else if (space.D) {
      rotate -= 1.0D;
      rotate += RotateVel;
      RotateVel -= 1.0D;
    } else { RotateVel = 0.0D;
    }
    if (RotateVel > 4.0D) RotateVel = 4.0D; else if (RotateVel < -4.0D) { RotateVel = -4.0D;
    }
    if (!space.SPACE) {
      space.SPACE = true;
      Shoot();
    }
  }
  
  public void render(Graphics2D g)
  {
    g.rotate(Math.toRadians(rotate), x + 32.0D, y + 32.0D);
    g.drawImage(player, (int)x, (int)y, width, height, null);
  }
  
  public Rectangle getBounds() {
    return new Rectangle((int)x - 16, (int)y - 16, width, height);
  }
}
