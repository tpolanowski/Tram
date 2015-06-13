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

        // test array as img
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

        if (mapping[current.getY()][current.getX()+1]) {
            previous.set(current);
            current.setX(current.getX()+1);
        } else if (mapping[current.getY()-1][current.getX()+1]) {
            previous.set(current);
            current.setX(current.getX()+1);
            current.setY(current.getY()-1);
        } else if (mapping[current.getY()+1][current.getX()+1]) {
            previous.set(current);
            current.setX(current.getX()+1);
            current.setY(current.getY()+1);
        }
        path.add(current);
        img.setRGB(current.getY(), current.getX(), new Color(0,255,0).getRGB());

        while(!current.equals(first)) {
            System.out.println("while: current: " + current.toString() + "previous: " + previous.toString());
            Coord[] candidates = new Coord[8];
            for (int i = 0; i<8; i++){
                candidates[i] = new Coord();
            }
            // SW
            candidates[0].setX(current.getX() - 1);
            candidates[0].setY(current.getY() - 1);
            // S
            candidates[1].setX(current.getX());
            candidates[1].setY(current.getY() - 1);
            // SE
            candidates[2].setX(current.getX() + 1);
            candidates[2].setY(current.getY() - 1);
            // E
            candidates[3].setX(current.getX() + 1);
            candidates[3].setY(current.getY());
            // NE
            candidates[4].setX(current.getX() + 1);
            candidates[4].setY(current.getY() + 1);
            // N
            candidates[5].setX(current.getX());
            candidates[5].setY(current.getY() + 1);
            // NW
            candidates[6].setX(current.getX() - 1);
            candidates[6].setY(current.getY() + 1);
            // W
            candidates[7].setX(current.getX() - 1);
            candidates[7].setY(current.getY());

            for (Coord candidate : candidates) {
                if (mapping[candidate.getY()][candidate.getX()]) {
                    if (!candidate.equals(previous)) {
                        System.out.println("FOUND MATCH, candidate:" + candidate.toString() + "previous: " + previous.toString());
                        previous.set(current);
                        current.set(candidate);
                        path.add(current);
                        img.setRGB(current.getY(), current.getX(), new Color(0,255,0).getRGB());
                        break;
                    }
                }
            }
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