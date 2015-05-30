import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import com.jogamp.common.nio.Buffers;

public class TerrainMesh {
    private float[][] heightMap;
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private IntBuffer indexBuffer;
    private Vector3D offset;
    private float spacing;

    public TerrainMesh(float[][] heightMap, float spacing, Vector3D off) {
        this.heightMap = heightMap;
        this.spacing = spacing;
        this.offset = off;
        createBuffers();
    }

    public void createBuffers() {
        vertexBuffer = Buffers.newDirectFloatBuffer(heightMap.length
                * heightMap[0].length * 3);
        colorBuffer = Buffers.newDirectFloatBuffer(heightMap.length
                * heightMap[0].length * 3);
        indexBuffer = Buffers.newDirectIntBuffer((heightMap.length - 1)
                * (heightMap[0].length - 1) * 6);
        for (int x = 0; x < heightMap.length; x++) {
            for (int z = 0; z < heightMap[0].length; z++) {
                vertexBuffer.put(((float) x) * spacing + offset.x);
                vertexBuffer.put(heightMap[x][z] + offset.y);
                vertexBuffer.put(((float) z) * spacing + offset.z);
                colorBuffer.put(1);
                colorBuffer.put(1);
                colorBuffer.put(1);

                if (x > 0 && z > 0) {
                    indexBuffer.put(z + (x * heightMap[0].length));
                    indexBuffer.put(z - 1 + (x * heightMap[0].length));
                    indexBuffer.put(z + ((x - 1) * heightMap[0].length));

                    indexBuffer.put(z - 1 + (x * heightMap[0].length));
                    indexBuffer.put(z + ((x - 1) * heightMap[0].length));
                    indexBuffer.put(z - 1 + ((x - 1) * heightMap[0].length));
                }
            }
        }
        colorBuffer = (FloatBuffer) colorBuffer.flip();
        vertexBuffer = (FloatBuffer) vertexBuffer.flip();
        indexBuffer = (IntBuffer) indexBuffer.flip();
    }

    public int getIndexCount() {
        return indexBuffer.limit();
    }

    public FloatBuffer getVertexBuffer() {
        return vertexBuffer;
    }

    public FloatBuffer getColorBuffer() {
        return colorBuffer;
    }

    public IntBuffer getIndexBuffer() {
        return indexBuffer;
    }
}