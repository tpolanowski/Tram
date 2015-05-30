import com.jogamp.opengl.util.FPSAnimator;

import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import java.awt.*;

public class DisplayManager {

    private static final int MAX_FPS = 50;
    private final GLCanvas canvas;
    private final GLProfile profile;
    private final GLCapabilities caps;
    private final FPSAnimator animator;
    private final Render render;

    public DisplayManager() {
        GLProfile.initSingleton();
        profile = GLProfile.getDefault();
        caps = new GLCapabilities(profile);
        canvas = new GLCanvas(caps);
        render = new Render(this);
        canvas.addGLEventListener(render);
        animator = new FPSAnimator(MAX_FPS);
        animator.add(canvas);
    }

    public Component getGLCanvas() {
        return canvas;
    }

    public void start() {
        if (animator.isStarted() == false) {
            animator.start();
        }
    }

    public void dispose() {
        if (animator.isStarted() == true) {
            animator.stop();
        }
        canvas.destroy();
    }
}
