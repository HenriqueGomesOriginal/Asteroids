package Space;

import GFX.SpriteSheet;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

public class Asteroid
{
  public int type;
  public static final int SMALL = 0;
  public static final int MEDIUM = 1;
  public static final int LARGE = 2;
  public double x;
  public double y;
  private int width;
  private int height;
  private float speed;
  private Random random;
  private float radians;
  private float sin;
  private float cos;
  private java.awt.image.BufferedImage asteroid;
  
  public Asteroid(Space space, int type, double x, double y)
  {
    SpriteSheet ss = new SpriteSheet(space.getSpriteSheet());
    asteroid = ss.grabImage(1, 2, 96, 96);
    
    random = new Random();
    
    this.type = type;
    
    radians = (random.nextInt(180) - 90);
    
    sin = ((float)Math.sin(Math.toRadians(radians)));
    cos = ((float)Math.cos(Math.toRadians(radians)));
    
    switch (type) {
    case 0: 
      width = 32;
      height = 32;
      speed = (random.nextInt(8) - 4);
      this.x = (x - width / 3);
      this.y = (y - height / 3);
      break;
    case 1: 
      width = 96;
      height = 96;
      speed = (random.nextInt(6) - 3);
      this.x = (x - width / 2.6D);
      this.y = (y - height / 2.6D);
      break;
    case 2: 
      width = 256;
      height = 256;
      speed = (random.nextInt(4) - 2);
      this.x = x;
      this.y = y;
      while (space.CollisionA()) {
        this.x = random.nextInt(Space.width);
        this.y = random.nextInt(Space.height);
      }
    }
    
    

    if (speed == 0.0F) speed = 1.0F;
  }
  
  public void tick(Space space)
  {
    x += speed;
    y += speed;
    
    x += cos;
    y += sin;
    
    if (x + width < 0.0D) x = (Space.width + width); else if (x - width > Space.width) x = (0 - width);
    if (y + height < 0.0D) y = (Space.height + height); else if (y - height > Space.height) y = (0 - height);
  }
  
  public void render(Graphics2D g)
  {
    g.drawImage(asteroid, (int)x, (int)y, width, height, null);
  }
  
  public int getType() { return type; }
  public double getX() { return x; }
  public double getY() { return y; }
  
  public Rectangle getBounds() {
    return new Rectangle((int)x, (int)y, width - 16, height - 16);
  }
}