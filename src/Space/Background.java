package Space;

import java.awt.Graphics;
import java.util.Random;

public class Background
{
  private int width;
  private int height;
  private int i;
  private int[] x = new int[30];
  private int[] y = new int[30];
  
  private Random random;
  
  public Background(Space space)
  {
    random = new Random();
    
    for (int i = 0; i <= 29; i++)
    {
      x[i] = random.nextInt(Space.width - 7);
    }
    
    for (int i = 0; i <= 29; i++)
    {
      y[i] = random.nextInt(Space.height - 7);
    }
    
    width = Space.width;
    height = Space.height;
  }
  
  public void render(Graphics g)
  {
    g.setColor(java.awt.Color.black);
    g.fillRect(0, 0, width, height);
    
    g.setColor(java.awt.Color.white);
    
    for (i = 0; i <= 29; i += 1)
    {
      g.fillOval(x[i], y[i], 4, 4);
    }
  }
}