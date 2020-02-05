package GFX;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageLoader
{
  private BufferedImage image;
  
  public BufferedImageLoader() {}
  
  public BufferedImage loadImage(String path) throws IOException
  {
    image = javax.imageio.ImageIO.read(getClass().getResource(path));
    return image;
  }
}