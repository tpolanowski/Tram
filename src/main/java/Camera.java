import javax.media.opengl.GL2;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener{

    private static final float yawMilli = 0.1f;
    private static final float pitchMilli = 0.1f;

    private float pitch = 45;
    private float yaw = 0;
    private float centerDist = 500;

    private boolean u, l, d, r;
    private long lastMoveProc = -1;

    public void modPitch(float val) {
        if (pitch + val >= 0 && pitch + val <= 90) {
            pitch += val;
        }
    }

    public void modYaw(float val) {
        yaw += val;
        if (yaw < 0)
            yaw += 360;
        if (yaw >= 360)
            yaw -= 360;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void translate(GL2 gl) {
        if (lastMoveProc != -1) {
            long passed = System.currentTimeMillis() - lastMoveProc;
            if (u)
                modPitch(-passed * pitchMilli);
            else if (d)
                modPitch(passed * pitchMilli);
            if (l)
                modYaw(-passed * yawMilli);
            else if (r)
                modYaw(passed * yawMilli);
        }
        lastMoveProc = System.currentTimeMillis();
        gl.glTranslatef(0, 0, -centerDist);
        gl.glRotatef(pitch, 1, 0, 0);
        gl.glRotatef(360 - yaw, 0, 1, 0);
    }

    @Override
    public String toString() {
        return "Camera: Yaw: " + yaw + " Pitch: " + pitch;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            u = true;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            d = true;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            l = true;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            r = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            u = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            d = false;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            l = false;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            r = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
