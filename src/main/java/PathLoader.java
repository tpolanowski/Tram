import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PathLoader {
    public static boolean[][] loadPath(File img) throws IOException {

        BufferedImage path = ImageIO.read(img);
        final boolean[][] mapping = new boolean[path.getWidth()][path.getHeight()];

        for (int x = 0; x < path.getWidth(); x++) {
            for (int y = 0; y < path.getHeight(); y++) {
                if(path.getRGB(x, y) == -1) {
                    mapping[x][y] = true;
                    System.out.println("x: " + x + " y: " + y + "getRGB: " + path.getRGB(x, y));
                }
                else {
                    mapping[x][y] = false;
                }
            }
        }
        return mapping;
    }
}