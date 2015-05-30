import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import java.awt.*;

/**
 * Created by t on 29.05.15.
 */
public class Render implements GLEventListener{

    private static GraphicsEnvironment graphicsEnvironment;
    private static boolean isFullscreen = false;
    public static DisplayMode displayMode;
    private static Dimension dimension;
    private static Point point = new Point(0,0);

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }
}
