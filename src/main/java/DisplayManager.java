import com.jogamp.opengl.util.FPSAnimator;

import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import java.awt.*;

public class DisplayManager {

    private static final int MAX_FPS = 60;
    private final GLCanvas canvas;
    private final GLProfile profile;
    private final GLCapabilities caps;
    private final FPSAnimator animator;
    private final Render render;
    private final Camera cam;

    public DisplayManager() {
        GLProfile.initSingleton();
        profile = GLProfile.getDefault();
        caps = new GLCapabilities(profile);
        canvas = new GLCanvas(caps);
        render = new Render(this);
        canvas.addGLEventListener(render);
        animator = new FPSAnimator(MAX_FPS);
        cam = new Camera();
        canvas.addKeyListener(cam);
        animator.add(canvas);
    }

    public Component getGLCanvas() {
        return canvas;
    }

    public Camera getCamera(){
        return cam;
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
