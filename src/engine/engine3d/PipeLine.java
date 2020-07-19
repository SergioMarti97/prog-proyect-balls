package engine.engine3d;

import engine.GameContainer;
import engine.gfx.images.Image;

import java.util.ArrayList;
import java.util.Arrays;

public class PipeLine {

    private RenderFlags renderFlag = RenderFlags.RENDER_TEXTURED;

    private Graphics3D gfx;

    private Mat4x4 projectionMatrix;

    private Mat4x4 viewMatrix;

    private Mat4x4 worldMatrix;

    private Image texture;

    private float viewX;

    private float viewY;

    private float viewW;

    private float viewH;

    private Mesh cube;

    public PipeLine(GameContainer gc) {
        gfx = new Graphics3D(gc);
        projectionMatrix = new Mat4x4();
        viewMatrix = new Mat4x4();
        worldMatrix = new Mat4x4();

        /* Esto es un cubo, siempre es útil a la hora de probar los gráficos en 3D */
        cube = new Mesh();
        ArrayList<Triangle> tris = new ArrayList<>();
        // SOUTH
        tris.add(new Triangle(new Vec3d[]{new Vec3d(0.0f, 0.0f, 0.0f), new Vec3d(0.0f, 1.0f, 0.0f), new Vec3d(1.0f, 1.0f, 0.0f)}));
        tris.add(new Triangle(new Vec3d[]{new Vec3d(0.0f, 0.0f, 0.0f), new Vec3d(1.0f, 1.0f, 0.0f), new Vec3d(1.0f, 0.0f, 0.0f)}));
        // EAST
        tris.add(new Triangle(new Vec3d[]{new Vec3d(1.0f, 0.0f, 0.0f), new Vec3d(1.0f, 1.0f, 0.0f), new Vec3d(1.0f, 1.0f, 1.0f)}));
        tris.add(new Triangle(new Vec3d[]{new Vec3d(1.0f, 0.0f, 0.0f), new Vec3d(1.0f, 1.0f, 1.0f), new Vec3d(1.0f, 0.0f, 1.0f)}));
        // NORTH
        tris.add(new Triangle(new Vec3d[]{new Vec3d(1.0f, 0.0f, 1.0f), new Vec3d(1.0f, 1.0f, 1.0f), new Vec3d(0.0f, 1.0f, 1.0f)}));
        tris.add(new Triangle(new Vec3d[]{new Vec3d(1.0f, 0.0f, 1.0f), new Vec3d(0.0f, 1.0f, 1.0f), new Vec3d(0.0f, 0.0f, 1.0f)}));
        // WEST
        tris.add(new Triangle(new Vec3d[]{new Vec3d(0.0f, 0.0f, 1.0f), new Vec3d(0.0f, 1.0f, 1.0f), new Vec3d(0.0f, 1.0f, 0.0f)}));
        tris.add(new Triangle(new Vec3d[]{new Vec3d(0.0f, 0.0f, 1.0f), new Vec3d(0.0f, 1.0f, 0.0f), new Vec3d(0.0f, 0.0f, 0.0f)}));
        // TOP
        tris.add(new Triangle(new Vec3d[]{new Vec3d(0.0f, 1.0f, 0.0f), new Vec3d(0.0f, 1.0f, 1.0f), new Vec3d(1.0f, 1.0f, 1.0f)}));
        tris.add(new Triangle(new Vec3d[]{new Vec3d(0.0f, 1.0f, 0.0f), new Vec3d(1.0f, 1.0f, 1.0f), new Vec3d(1.0f, 1.0f, 0.0f)}));
        // BOTTOM
        tris.add(new Triangle(new Vec3d[]{new Vec3d(1.0f, 0.0f, 1.0f), new Vec3d(0.0f, 0.0f, 1.0f), new Vec3d(0.0f, 0.0f, 0.0f)}));
        tris.add(new Triangle(new Vec3d[]{new Vec3d(1.0f, 0.0f, 1.0f), new Vec3d(0.0f, 0.0f, 0.0f), new Vec3d(1.0f, 0.0f, 0.0f)}));

        cube.setTris(tris);
    }

    public void setProjection(float fovDegrees, float aspectRatio, float near, float far, float left, float top, float width, float height) {
        projectionMatrix = MatrixMath.matrixMakeProjection(fovDegrees, aspectRatio, near, far);
        viewX = left;
        viewY = top;
        viewW = width;
        viewH = height;
    }

    public void setCamera(Vec3d pos, Vec3d lookAt, Vec3d up) {
        viewMatrix = MatrixMath.matrixPointAt(pos, lookAt, up);
        viewMatrix = MatrixMath.matrixQuickInverse(viewMatrix);
    }

    public void setTransform(Mat4x4 transform) {
        worldMatrix = transform;
    }

    public void setTexture(Image texture) {
        this.texture = texture;
    }

    public void setLightSource(Vec3d pos, Vec3d dir, int color) {

    }

    public int render(ArrayList<Triangle> triangles, RenderFlags flag) {

        Mat4x4 matrixWorldView = MatrixMath.matrixMultiplyMatrix(worldMatrix, viewMatrix);

        //ArrayList<Triangle> trianglesToRaster = new ArrayList<>(); // todo ¿Que hace esto en el código de Javidx9?

        int numTrianglesDrawed = 0;

        for ( Triangle triangle : triangles ) {
            Triangle triangleTransformed = new Triangle();

            triangleTransformed.setT(triangle.getT());

            triangleTransformed.getP()[0].set(MatrixMath.matrixMultiplyVector(matrixWorldView, triangle.getP()[0]));
            triangleTransformed.getP()[1].set(MatrixMath.matrixMultiplyVector(matrixWorldView, triangle.getP()[1]));
            triangleTransformed.getP()[2].set(MatrixMath.matrixMultiplyVector(matrixWorldView, triangle.getP()[2]));

            Vec3d normal;
            Vec3d line1;
            Vec3d line2;
            line1 = MatrixMath.vectorSub(triangleTransformed.getP()[1], triangleTransformed.getP()[0]);
            line2 = MatrixMath.vectorSub(triangleTransformed.getP()[2], triangleTransformed.getP()[0]);
            normal = MatrixMath.vectorCrossProduct(line1, line2);
            normal = MatrixMath.vectorNormalise(normal);

            if ( renderFlag == RenderFlags.RENDER_CULL_CW && MatrixMath.vectorDotProduct(normal, triangleTransformed.getP()[0]) > 0.0f ) {
                continue;
            }
            if ( renderFlag == RenderFlags.RENDER_CULL_CCW && MatrixMath.vectorDotProduct(normal, triangleTransformed.getP()[0]) < 0.0f ) {
                continue;
            }

            triangleTransformed.setColor(0xffffffff);
            int numTrianglesClipped = 0;
            Triangle[] clipped = new Triangle[2]; // todo, con esto hay un problema... No se cual es pero hay un problema...
            clipped[0] = new Triangle();
            clipped[1] = new Triangle();
            numTrianglesClipped = MatrixMath.triangleClipAgainstPlane(new Vec3d(0.0f, 0.0f, 0.1f), new Vec3d(0.0f, 0.0f, 1.0f), triangleTransformed, clipped[0], clipped[1]);

            for ( int n = 0; n < numTrianglesClipped; n++ ) {
                Triangle triangleProjected = clipped[n];

                triangleProjected.getP()[0].set(MatrixMath.matrixMultiplyVector(projectionMatrix, clipped[n].getP()[0]));
                triangleProjected.getP()[1].set(MatrixMath.matrixMultiplyVector(projectionMatrix, clipped[n].getP()[1]));
                triangleProjected.getP()[2].set(MatrixMath.matrixMultiplyVector(projectionMatrix, clipped[n].getP()[2]));

                triangleProjected.getP()[0].setX(triangleProjected.getP()[0].getX() / triangleProjected.getP()[0].getW());
                triangleProjected.getP()[1].setX(triangleProjected.getP()[1].getX() / triangleProjected.getP()[1].getW());
                triangleProjected.getP()[2].setX(triangleProjected.getP()[2].getX() / triangleProjected.getP()[2].getW());

                triangleProjected.getP()[0].setY(triangleProjected.getP()[0].getY() / triangleProjected.getP()[0].getW());
                triangleProjected.getP()[1].setY(triangleProjected.getP()[1].getY() / triangleProjected.getP()[1].getW());
                triangleProjected.getP()[2].setY(triangleProjected.getP()[2].getY() / triangleProjected.getP()[2].getW());

                triangleProjected.getP()[0].setZ(triangleProjected.getP()[0].getZ() / triangleProjected.getP()[0].getW());
                triangleProjected.getP()[1].setZ(triangleProjected.getP()[1].getZ() / triangleProjected.getP()[1].getW());
                triangleProjected.getP()[2].setZ(triangleProjected.getP()[2].getZ() / triangleProjected.getP()[2].getW());

                triangleProjected.getT()[0].setX(triangleProjected.getT()[0].getX() / triangleProjected.getP()[0].getW());
                triangleProjected.getT()[1].setX(triangleProjected.getT()[1].getX() / triangleProjected.getP()[1].getW());
                triangleProjected.getT()[2].setX(triangleProjected.getT()[2].getX() / triangleProjected.getP()[2].getW());

                triangleProjected.getT()[0].setY(triangleProjected.getT()[0].getY() / triangleProjected.getP()[0].getW());
                triangleProjected.getT()[1].setY(triangleProjected.getT()[1].getY() / triangleProjected.getP()[1].getW());
                triangleProjected.getT()[2].setY(triangleProjected.getT()[2].getY() / triangleProjected.getP()[2].getW());

                triangleProjected.getT()[0].setZ(1.0f / triangleProjected.getP()[0].getW());
                triangleProjected.getT()[1].setZ(1.0f / triangleProjected.getP()[1].getW());
                triangleProjected.getT()[2].setZ(1.0f / triangleProjected.getP()[2].getW());

                Triangle[] sclipped = new Triangle[2];
                sclipped[0] = new Triangle();
                sclipped[1] = new Triangle();
                ArrayList<Triangle> triangleList = new ArrayList<>();

                triangleList.add(triangleProjected);
                int numNewTriangles = 1;

                for ( int p = 0; p < 4; p++ ) {
                    int numTrianglesToAdd = 0;
                    while ( numNewTriangles > 0 ) {
                        Triangle test = triangleList.get(0); // todo linea 1178 del motor de gráficos 3D de Javidx9
                        triangleList.remove(0);
                        numNewTriangles--;

                        switch (p) {
                            case 0:
                                numTrianglesToAdd = MatrixMath.triangleClipAgainstPlane(new Vec3d(0.0f, -1.0f, 0.0f), new Vec3d(0.0f, 1.0f, 0.0f), test, sclipped[0], sclipped[1]);
                                break;
                            case 1:
                                numTrianglesToAdd = MatrixMath.triangleClipAgainstPlane(new Vec3d(0.0f, +1.0f, 0.0f), new Vec3d(0.0f, -1.0f, 0.0f), test, sclipped[0], sclipped[1]);
                                break;
                            case 2:
                                numTrianglesToAdd = MatrixMath.triangleClipAgainstPlane(new Vec3d(-1.0f, 0.0f, 0.0f), new Vec3d(1.0f, 0.0f, 0.0f), test, sclipped[0], sclipped[1]);
                                break;
                            case 3:
                                numTrianglesToAdd = MatrixMath.triangleClipAgainstPlane(new Vec3d(1.0f, 0.0f, 0.0f), new Vec3d(-1.0f, 0.0f, 0.0f), test, sclipped[0], sclipped[1]);
                                break;
                        }

                        triangleList.addAll(Arrays.asList(sclipped).subList(0, numTrianglesToAdd));
                        /* todo, probar que funciona más rápdio, si la función que provee Java o el bucle for
                        for ( int w = 0; w < numTrianglesToAdd; w++ ) {
                            triangleList.add(sclipped[w]);
                        }*/
                    }
                    numNewTriangles = triangleList.size();
                }
                for ( Triangle triangleRasterized : triangleList ) {
                    Vec3d offSetView = new Vec3d(1.0f, 1.0f, 0.0f);
                    triangleRasterized.getP()[0].set(MatrixMath.vectorAdd(triangleRasterized.getP()[0], offSetView));
                    triangleRasterized.getP()[1].set(MatrixMath.vectorAdd(triangleRasterized.getP()[1], offSetView));
                    triangleRasterized.getP()[2].set(MatrixMath.vectorAdd(triangleRasterized.getP()[2], offSetView));
                    triangleRasterized.getP()[0].setX(triangleRasterized.getP()[0].getX() * 0.5f * viewW);
                    triangleRasterized.getP()[0].setY(triangleRasterized.getP()[0].getY() * 0.5f * viewH);
                    triangleRasterized.getP()[1].setX(triangleRasterized.getP()[0].getX() * 0.5f * viewW);
                    triangleRasterized.getP()[1].setY(triangleRasterized.getP()[0].getY() * 0.5f * viewH);
                    triangleRasterized.getP()[2].setX(triangleRasterized.getP()[0].getX() * 0.5f * viewW);
                    triangleRasterized.getP()[2].setY(triangleRasterized.getP()[0].getY() * 0.5f * viewH);
                    offSetView = new Vec3d(viewX, viewY, 0);
                    triangleRasterized.getP()[0].set(MatrixMath.vectorAdd(triangleRasterized.getP()[0], offSetView));
                    triangleRasterized.getP()[1].set(MatrixMath.vectorAdd(triangleRasterized.getP()[1], offSetView));
                    triangleRasterized.getP()[2].set(MatrixMath.vectorAdd(triangleRasterized.getP()[2], offSetView));

                    if ( flag == RenderFlags.RENDER_TEXTURED ) {
                        // todo aquí es donde iría la función para dibujar un triangulo texturizado...
                        gfx.drawTexturedTriangle(
                                (int)(triangleRasterized.getP()[0].getX()), (int)(triangleRasterized.getP()[0].getY()), triangleRasterized.getT()[0].getX(), triangleRasterized.getT()[0].getY(), triangleRasterized.getT()[0].getZ(),
                                (int)(triangleRasterized.getP()[1].getX()), (int)(triangleRasterized.getP()[1].getY()), triangleRasterized.getT()[1].getX(), triangleRasterized.getT()[1].getY(), triangleRasterized.getT()[1].getZ(),
                                (int)(triangleRasterized.getP()[2].getX()), (int)(triangleRasterized.getP()[2].getY()), triangleRasterized.getT()[2].getX(), triangleRasterized.getT()[2].getY(), triangleRasterized.getT()[2].getZ(),
                                texture
                        );

                    }
                    if ( flag == RenderFlags.RENDER_WIRE ) {
                        gfx.drawTriangleWire(triangleRasterized, 0xffffffff);
                    }
                    if ( flag == RenderFlags.RENDER_FLAT ) {
                        gfx.drawTriangleFlat(triangleRasterized);
                    }

                    numTrianglesDrawed++;
                }
            }
        }

        return numTrianglesDrawed;
    }

    public int render(ArrayList<Triangle> triangles) {
        return render(triangles, renderFlag);
    }

    public Graphics3D getGfx() {
        return gfx;
    }

    public void setGfx(Graphics3D gfx) {
        this.gfx = gfx;
    }

    public Mesh getCube() {
        return cube;
    }

}
