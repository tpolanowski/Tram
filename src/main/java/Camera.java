import javax.media.opengl.GL2;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener{

    private static final float yawMilli = 0.1f;
    private static final float pitchMilli = 0.1f;
    private static final float zoomMilli = 0.1f;

    private float pitch = 45;
    private float yaw = 0;
    private float zoom = 85;
    private float x = 0;
    private float y = 0;
    private float centerDist = 500;

    private boolean u, l, d, r, zUp, zDown, xUp, xDown, yUp, yDown;
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
    public void modX(float val) {
        x += val;
    }

    public void modY(float val) {
        y += val;
    }

    public void modZoom(float val) {
        zoom += val;
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
            if (zUp)
                modZoom(passed + zoomMilli);
            else if(zDown)
                modZoom(-passed + zoomMilli);
            if (xUp)
                modX(passed + zoomMilli);
            else if(xDown)
                modX(-passed + zoomMilli);
            if (yUp)
                modY(passed + zoomMilli);
            else if(yDown)
                modY(-passed + zoomMilli);

        }
        lastMoveProc = System.currentTimeMillis();
        gl.glTranslatef(x,y,0);
        gl.glScalef(zoom/100,zoom/100,zoom/100);
        gl.glTranslatef(0, 0, -centerDist);
        gl.glRotatef(pitch, 1, 0, 0);
        gl.glRotatef(360 - yaw, 0, 1, 0);
        //System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "Camera: Yaw: " + yaw + " Pitch: " + pitch + " X: " + x + " Y: " + y + " Zoom: " + zoom;
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
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            zUp = true;
        } else if (e.getKeyCode() == KeyEvent.VK_F) {
            zDown = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            xUp = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            xDown = true;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            yUp = true;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            yDown = true;
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
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            zUp = false;
        } else if (e.getKeyCode() == KeyEvent.VK_F) {
            zDown = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            xUp = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            xDown = false;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            yUp = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            yDown = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
