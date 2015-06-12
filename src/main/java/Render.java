import com.jogamp.common.nio.Buffers;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Render implements GLEventListener{

    private DisplayManager dm;
    private TerrainMesh terrain;
    private boolean[][] mapping;
    private ArrayList<Coord> path;
    private float s;

    public Render(DisplayManager dm) {
        this.dm = dm;
        float[][] hMap = new float[40][40];
        terrain = new TerrainMesh(hMap
                                 ,25
                                 ,new Vector3D(-25 * (hMap.length / 2f)
                                               ,0
                                               ,-25 * (hMap[0].length / 2f)
                                               )
                                 );
        try {
            mapping = PathLoader.loadPath(new File("path.png"));
            path = PathLoader.orderPath(mapping);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Set background color to black and opaque
        gl.glClearDepth(1.0f);                   // Set background depth to farthest
        gl.glEnable(gl.GL_DEPTH_TEST);   // Enable depth testing for z-culling
        gl.glDepthFunc(gl.GL_LEQUAL);    // Set the type of depth-test
        gl.glShadeModel(gl.GL_SMOOTH);   // Enable smooth shading
        gl.glHint(gl.GL_PERSPECTIVE_CORRECTION_HINT, gl.GL_NICEST);  // Nice perspective corrections
        //gl.glEnable(GL2.GL_LIGHTING);
        //gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = GLU.createGLU(gl);

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // world projection
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(drawable.getWidth() / -2
                  ,drawable.getWidth() / 2
                  ,drawable.getHeight() / 2
                  ,drawable.getHeight() / -2
                  ,100000
                  ,1
                  );

        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
        dm.getCamera().translate(gl);

        // rendering
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);

        // terrain
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL.GL_FLOAT, 0, terrain.getVertexBuffer());
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
        gl.glColorPointer(3, GL.GL_FLOAT, 0, terrain.getColorBuffer());
        gl.glDrawElements(GL.GL_TRIANGLES, terrain.getIndexCount(), GL2.GL_UNSIGNED_INT, terrain.getIndexBuffer());
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);



        // update
        update(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }

    private void update(GL2 gl) {
        s += 0.01;
        drawTram(gl);
    }

    private void drawTram(GL2 gl) {
        // test - triangle
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glPushMatrix();
        gl.glBegin(gl.GL_TRIANGLES);
        gl.glColor3f(1.0f, 0.0f, 0.0f); // Red
        gl.glVertex3f(0.0f, -1.0f, -2.0f*s);
        gl.glColor3f(0.0f, 1.0f, 0.0f); // Green
        gl.glVertex3f(-1.0f, 0.0f, 2.0f*-s);
        gl.glColor3f(0.0f, 1.0f, 1.0f); // Cyan
        gl.glVertex3f(1.0f, 0.0f, 2.0f*-s);
        gl.glEnd();
        gl.glPopMatrix();
    }
}
