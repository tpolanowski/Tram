import javax.swing.*;

public class Tram extends JFrame {
    DisplayManager dm;

    public Tram() {
        super("Tram");
        setSize(1280, 720);
        setLocation(0, 0);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        dm = new DisplayManager();
        dm.getGLCanvas().setSize(1280, 720);
        dm.getGLCanvas().setLocation(0, 0);
        add(dm.getGLCanvas());
        setVisible(true);
        dm.start();
    }

    @Override
    public void dispose() {
        dm.dispose();
        super.dispose();
    }

    public static void main(String[] args) {
        new Tram();
    }
}
