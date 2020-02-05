package Space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Bullet
{
  private int width = 7; private int height = 7; private int speed = 15;
  


  private long lastTime = 0L;
  private double x;
  private double y;
  private double xVel;
  
  public Bullet(double x, double y, double rotate) {
    radians = rotate;
    this.x = x;
    this.y = y;
    
    Time = System.currentTimeMillis();
  }
  
  public boolean ShouldRemove() { return remove; }
  
  private double yVel;
  
  public void tick() { xVel = Math.cos(Math.toRadians(radians) - 1.5707963267948966D);
    yVel = Math.sin(Math.toRadians(radians) - 1.5707963267948966D);
    
    xVel *= speed;
    yVel *= speed;
    
    if (xVel > 35.0D) xVel = 35.0D; else if (xVel < -35.0D) xVel = -35.0D;
    if (yVel > 35.0D) yVel = 35.0D; else if (yVel < -35.0D) { yVel = -35.0D;
    }
    x += xVel;
    y += yVel;
    
    if (y + height < 0.0D) y = Space.height; else if (y - height > Space.height) y = (0 - height);
    if (x + width < 0.0D) x = Space.width; else if (x - width > Space.width) { x = (0 - width);
    }
    lastTime = System.currentTimeMillis();
    if (lastTime > Time + 800L)
    {
      remove = true; }
  }
  
  private boolean remove;
  private long Time;
  private double radians;
  public void render(Graphics2D g) { g.setColor(new Color(255, 239, 68));
    g.fillOval((int)x + 32, (int)y + 32, width, height);
  }
  
  public Rectangle getBounds() {
    return new Rectangle((int)x, (int)y, width + 12, height + 12);
  }
}