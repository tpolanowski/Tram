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
    private int t = 0;

    public Render(DisplayManager dm) {
        this.dm = dm;
        float[][] hMap = new float[40][40];
        terrain = new TerrainMesh(hMap
                                 ,30
                                 ,new Vector3D( (hMap.length / 2f -150f)
                                               ,0
                                               ,(hMap[0].length / 2f -80f)
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

        // map position corrections
        gl.glRotatef(90f, 0, 1, 0);
        gl.glTranslatef(-500, 0, -500);

        // terrain
//        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
//        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
//        gl.glVertexPointer(3, GL.GL_FLOAT, 0, terrain.getVertexBuffer());
//        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
//        gl.glColorPointer(3, GL.GL_FLOAT, 0, terrain.getColorBuffer());
//        gl.glDrawElements(GL.GL_TRIANGLES, terrain.getIndexCount(), GL2.GL_UNSIGNED_INT, terrain.getIndexBuffer());
//        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
//        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);

        // road
        drawTramRoad(gl, 4f);

        // update
        update(gl);

        //test
        gl.glPushMatrix();
        //gl.glRotatef(90,0,1,0);
        gl.glBegin(gl.GL_TRIANGLES);
        gl.glColor3f(1.0f, 0.0f, 0.0f); // Red
        gl.glVertex3f(0.0f, -1.0f, -20.0f);
        gl.glColor3f(0.0f, 1.0f, 0.0f); // Green
        gl.glVertex3f(-10.0f, 0.0f, 20.0f);
        gl.glColor3f(0.0f, 1.0f, 1.0f); // Cyan
        gl.glVertex3f(10.0f, 0.0f, 20.0f);
        gl.glEnd();
        gl.glPopMatrix();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }

    private void update(GL2 gl) {
        t++;
        if (t == path.size()) {
            t = 0;
        }
        drawTram(gl,0.04f);

        drawTest2(gl);


    }

    private void drawTest(GL2 gl) {
        // test - triangle
        System.out.println("t: " + t + " tram at: " + path.get(t).toString());
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glPushMatrix();


        float[] f = new float[16];
        gl.glGetFloatv(gl.GL_MODELVIEW_MATRIX, f, 0);
        System.out.println("MV");
        for (int i = 0; i < 16; i++)
        { System.out.print(" " + f[i]); if (i % 4 == 3) System.out.println(); }

        gl.glTranslatef(500, 0, 500);
        gl.glRotatef(90, 0, 1, 0);
        gl.glTranslatef(-500, 0, -500);
        gl.glRotatef(-90, 0, 1, 0);

        gl.glBegin(gl.GL_TRIANGLES);
        gl.glColor3f(1.0f, 0.0f, 0.0f); // Red
        gl.glVertex3f(path.get(t).getX() + 0.0f, -1.0f, path.get(t).getY() - 20.0f);
        gl.glColor3f(0.0f, 1.0f, 0.0f); // Green
        gl.glVertex3f(path.get(t).getX() - 10.0f, 0.0f, path.get(t).getY() + 20.0f);
        gl.glColor3f(0.0f, 1.0f, 1.0f); // Cyan
        gl.glVertex3f(path.get(t).getX() + 10.0f, 0.0f, path.get(t).getY() + 20.0f);
        gl.glEnd();

        gl.glPopMatrix();
    }

    private void drawTest2(GL2 gl) {
        // test - triangle
        System.out.println("t: " + t + " tram at: " + path.get(t).toString());
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glPushMatrix();


        float[] f = new float[16];
        gl.glGetFloatv(gl.GL_MODELVIEW_MATRIX, f, 0);
        System.out.println("MV");
        for (int i = 0; i < 16; i++)
        { System.out.print(" " + f[i]); if (i % 4 == 3) System.out.println(); }

        gl.glTranslatef(path.get(t).getX(), 0, path.get(t).getY());
        gl.glRotatef(90,0,1,0);
       //gl.glTranslatef(-path.get(t).getX(), 0, -path.get(t).getY());
        gl.glBegin(gl.GL_TRIANGLES);
        gl.glColor3f(1.0f, 0.0f, 0.0f); // Red
        gl.glVertex3f( + 0.0f, -1.0f,- 20.0f);
        gl.glColor3f(0.0f, 1.0f, 0.0f); // Green
        gl.glVertex3f(- 10.0f, 0.0f,  20.0f);
        gl.glColor3f(0.0f, 1.0f, 1.0f); // Cyan
        gl.glVertex3f(10.0f, 0.0f,  20.0f);
        gl.glEnd();

        gl.glPopMatrix();
    }

    private void drawTram(GL2 gl, float s) {
        int x = path.get(t).getX();
        int y = path.get(t).getY();

        //gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        gl.glPushMatrix();

        //gl.glRotatef(90,0,1,0);

        gl.glColor3f(0.1f, 0.2f, 1f); // Dark Blue

        // front window
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex3f(x+300*s, -600*s, y);
        gl.glVertex3f(x, -200*s, y);
        gl.glVertex3f(x, -200*s, y-200*s);
        gl.glVertex3f(x+300*s, -600*s, y-200*s);
        gl.glEnd();

        gl.glColor3f(0.6f, 0.7f, 1f); // Light Blue

        int r = 30; // frame around the window

        // front window glass
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex3f(x+(300-r)*s, (-600+r)*s, y-r*s);
        gl.glColor3f(0.4f, 0.6f, 0.9f); // Light Blue Tint 1
        gl.glVertex3f(x, -200*s, y-r*s);
        gl.glColor3f(0.4f, 0.8f, 1.0f); // Light Blue Tint 2
        gl.glVertex3f(x, -200*s, y+(-200+r)*s);
        gl.glVertex3f(x+(300-r)*s, (-600+r)*s, y+(-200+r)*s);
        gl.glEnd();

        gl.glColor3f(0.1f, 0.2f, 1f); // Dark Blue

        // front - right window
        gl.glBegin(gl.GL_TRIANGLES);
        gl.glVertex3f(x,-200*s,y);
        gl.glVertex3f(x+300*s,-200*s,y);
        gl.glVertex3f(x+300*s,-600*s,y);
        gl.glEnd();

        // front - left window
        gl.glBegin(gl.GL_TRIANGLES);
        gl.glVertex3f(x,-200*s,y-200*s);
        gl.glVertex3f(x+300*s,-200*s,y-200*s);
        gl.glVertex3f(x+300*s,-600*s,y-200*s);
        gl.glEnd();

        // front - right window glass
        gl.glBegin(gl.GL_TRIANGLES);
        gl.glColor3f(0.6f, 0.7f, 1f); // Light Blue
        gl.glVertex3f(x+r*s,-200*s,y+0.1f*s);
        gl.glColor3f(0.4f, 0.6f, 0.9f); // Light Blue Tint 1
        gl.glVertex3f(x+(300-r)*s,-200*s,y+0.1f*s);
        gl.glColor3f(0.4f, 0.8f, 1.0f); // Light Blue Tint 2
        gl.glVertex3f(x+(300-r)*s,(-600+2*r)*s,y+0.1f*s);
        gl.glEnd();

        // front - left window glass
        gl.glBegin(gl.GL_TRIANGLES);
        gl.glColor3f(0.6f, 0.7f, 1f); // Light Blue
        gl.glVertex3f(x+r*s,-200*s,y-200.1f*s);
        gl.glColor3f(0.4f, 0.6f, 0.9f); // Light Blue Tint 1
        gl.glVertex3f(x+(300-r)*s,-200*s,y-200.1f*s);
        gl.glColor3f(0.4f, 0.8f, 1.0f); // Light Blue Tint 2
        gl.glVertex3f(x+(300-r)*s,(-600+2*r)*s,y-200.1f*s);
        gl.glEnd();

        gl.glColor3f(0.1f, 0.2f, 1f); // Dark Blue

        // front
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex3f(x,0,y);
        gl.glVertex3f(x, -200*s,y);
        gl.glVertex3f(x, -200*s,y-200*s);
        gl.glVertex3f(x,0,y-200*s);
        gl.glEnd();

        // front - back wall
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex3f(x+300*s,0,y);
        gl.glVertex3f(x+300*s,-600*s,y);
        gl.glVertex3f(x+300*s,-600*s,y-200*s);
        gl.glVertex3f(x+300*s,0,y-200*s);
        gl.glEnd();

        // front - right driver door
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex3f(x,0,y);
        gl.glVertex3f(x+300*s,0,y);
        gl.glVertex3f(x+300*s,-200*s,y);
        gl.glVertex3f(x,-200*s,y);
        gl.glEnd();

        // front - left driver door
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex3f(x,0,y-200*s);
        gl.glVertex3f(x+300*s,0,y-200*s);
        gl.glVertex3f(x+300*s,-200*s,y-200*s);
        gl.glVertex3f(x,-200*s,y-200*s);
        gl.glEnd();

        // 1st wagon - right wall
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex3f(x+300*s,0,y);
        gl.glVertex3f(x+1500*s,0,y);
        gl.glVertex3f(x+1500*s,-600*s,y);
        gl.glVertex3f(x+300*s,-600*s,y);
        gl.glEnd();

        // 1st wagon - left wall
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex3f(x+300*s,0,y-200*s);
        gl.glVertex3f(x+1500*s,0,y-200*s);
        gl.glVertex3f(x+1500*s,-600*s,y-200*s);
        gl.glVertex3f(x+300*s,-600*s,y-200*s);
        gl.glEnd();

        // 1st wagon - roof
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex3f(x+300*s,-600*s,y);
        gl.glVertex3f(x+1500*s,-600*s,y);
        gl.glVertex3f(x+1500*s,-600*s,y-200*s);
        gl.glVertex3f(x+300*s,-600*s,y-200*s);
        gl.glEnd();

        // 1st wagon - right windows
        for (int i=0; i<5; i++) {
            gl.glBegin(gl.GL_QUADS);
            gl.glColor3f(0.6f, 0.7f, 1f); // Light Blue
            gl.glVertex3f(x+(350+200*i)*s,-200*s,y+0.1f*s);
            gl.glColor3f(0.4f, 0.6f, 0.9f); // Light Blue Tint 1
            gl.glVertex3f(x+(500+200*i)*s,-200*s,y+0.1f*s);
            gl.glColor3f(0.4f, 0.8f, 1.0f); // Light Blue Tint 2
            gl.glVertex3f(x+(500+200*i)*s,-500*s,y+0.1f*s);
            gl.glColor3f(0.6f, 0.7f, 1f); // Light Blue
            gl.glVertex3f(x+(350+200*i)*s,-500*s,y+0.1f*s);
            gl.glEnd();
        }

        // 1st wagon - left windows
        for (int i=0; i<5; i++) {
            gl.glBegin(gl.GL_QUADS);
            gl.glColor3f(0.6f, 0.7f, 1f); // Light Blue
            gl.glVertex3f(x+(450+200*i)*s,-200*s,y-200.1f*s);
            gl.glColor3f(0.4f, 0.6f, 0.9f); // Light Blue Tint 1
            gl.glVertex3f(x+(600+200*i)*s,-200*s,y-200.1f*s);
            gl.glColor3f(0.4f, 0.8f, 1.0f); // Light Blue Tint 2
            gl.glVertex3f(x+(600+200*i)*s,-500*s,y-200.1f*s);
            gl.glColor3f(0.6f, 0.7f, 1f); // Light Blue
            gl.glVertex3f(x+(450+200*i)*s,-500*s,y-200.1f*s);
            gl.glEnd();
        }
        gl.glPopMatrix();
    }

    private void drawTramRoad(GL2 gl, float size) {
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glPushMatrix();

        for(Coord coord : path) {
            gl.glBegin(gl.GL_QUADS);
            gl.glColor3f(0.6f, 0.6f, 0.6f); // Gray
            gl.glVertex3f(coord.getX()+size,0f,coord.getY()+size);
            gl.glColor3f(0.6f, 0.6f, 0.6f); // Gray
            gl.glVertex3f(coord.getX()+size,0f,coord.getY()-size);
            gl.glColor3f(0.6f, 0.6f, 0.6f); // Gray
            gl.glVertex3f(coord.getX()-size,0f,coord.getY()-size);
            gl.glColor3f(0.6f, 0.6f, 0.6f); // Gray
            gl.glVertex3f(coord.getX()-size,0f,coord.getY()+size);
            gl.glEnd();
        }

        gl.glPopMatrix();
    }
}
