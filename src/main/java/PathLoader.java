import sun.util.BuddhistCalendar;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PathLoader {
    public static boolean[][] loadPath(File img) throws IOException {

        BufferedImage path = ImageIO.read(img);
        final boolean[][] mapping = new boolean[path.getWidth()][path.getHeight()];

        for (int x = 0; x < path.getWidth(); x++) {
            for (int y = 0; y < path.getHeight(); y++) {
                if (path.getRGB(x, y) == -1) {
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

    public static ArrayList<Coord> orderPath(boolean[][] mapping) {

        ArrayList<Coord> path = new ArrayList<Coord>();

        // 1. Find first element
        Coord first = new Coord();
        outerloop:
        for (int y = 0; y < mapping.length; y++) {
            for (int x = 0; x < mapping[0].length; x++) {
                if (mapping[y][x] == true) {
                    first.setX(x);
                    first.setY(y);
                    break outerloop;
                }
            }
        }

        // test the array
        System.out.println("First element found at: " + first.toString());
        BufferedImage img = new BufferedImage(mapping.length, mapping[0].length, BufferedImage.TYPE_INT_ARGB);
        File file = null;
        for (int y = 0; y < mapping.length; y++) {
            for (int x = 0; x < mapping[0].length; x++) {
                if (mapping[y][x] == true) {
                    img.setRGB(y, x, new Color(255,255,255).getRGB());
                }
                else {
                    img.setRGB(y, x, new Color(0,0,0).getRGB());
                }
            }
        }

        img.setRGB(first.getY(), first.getX(), new Color(255,0,0).getRGB());



        // 2. Look for neighbours and form a path, stop at one full loop

        Coord current = new Coord(first);
        Coord previous = new Coord(first);

        path.add(first);
        img.setRGB(first.getY(), first.getX()-1, new Color(0,255,0).getRGB());
//        if (mapping[][]) {
//
//        } else if (mapping[][]) {
//
//        }


        while(current.getX() != first.getX() && current.getY() != first.getY() ) {
            break;
        }


        try {
            file = new File("array.png");
            ImageIO.write(img, "png", file);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        // 3. GO

        return path;
    }
}