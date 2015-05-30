import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import java.awt.*;

public class Render implements GLEventListener{

    private DisplayManager dm;

    public Render(DisplayManager dm) {
        this.dm = dm;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
//        gl.glEnable(GL.GL_DEPTH_TEST);
//        gl.glEnable(GL2.GL_LIGHTING);
//        gl.glEnable(GL2.GL_LIGHT0);
//        gl.glEnable(GL2.GL_COLOR_MATERIAL);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // world projection
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(drawable.getWidth() / -2
                  ,drawable.getWidth() / 2
                  ,drawable.getHeight() / 2
                  ,drawable.getHeight() / -2
                  ,-500
                  ,500
                  );
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
        dm.getCamera().translate(gl);

        // rendering
        //gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor3f(1f, 0f, 0f);
        gl.glVertex3f(150f, 150f, -20f);
        gl.glColor3f(0f, 1f, 0f);
        gl.glVertex3f(-200f, 0f, -20f);
        gl.glColor3f(0f, 0f, 1f);
        gl.glVertex3f(0f, -200f, -20f);
        gl.glEnd();

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }
}
