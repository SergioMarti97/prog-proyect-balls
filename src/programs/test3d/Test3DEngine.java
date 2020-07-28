package programs.test3d;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.gfx.engine3d.normal.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Esta clase es un programa para testear y depurar el funcionamiento de las clases
 * del motor 3D.
 * Objetivo: Conseguir renderizar texturas.
 *
 * @class: Test3DEngine.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-28
 */
public class Test3DEngine extends AbstractGame {

    private Mat4x4 projectionMatrix = new Mat4x4();

    private Mat4x4 matView;

    private Camera cameraObj;

    private Mesh cube;

    private Vec3d lightDirection;

    private Vec3d cubeTranslation = new Vec3d();

    private Vec3d cubeRotation = new Vec3d();

    private RenderFlags renderFlag = RenderFlags.RENDER_FLAT;

    private boolean isShowingInformation = true;

    /**
     * Constructor de la clase.
     * @param title el título de la aplicación.
     */
    private Test3DEngine(String title) {
        super(title);
    }

    /**
     * Este método crea un cubo unitario. Siempre útil para testear motores 3D.
     * @return un cubo 3D unitario.
     */
    private Mesh initializationCube() {
        Mesh cube = new Mesh();

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

        return cube;
    }

    /**
     * Este método construye la matriz de proyección.
     * @param gc el objeto GameContainer que contiene información necesaria como el tamaño de la
     *           pantalla, necesario para calcular el aspectRatio.
     */
    private void buildProjectionMatrix(GameContainer gc) {
        float near = 0.1f;
        float far = 1000.0f;
        float fovDegrees = 90.0f;
        float aspectRatio = (float)gc.getHeight() / (float)gc.getWidth();
        projectionMatrix = MatrixMath.matrixMakeProjection(fovDegrees, aspectRatio, near, far);
    }

    /**
     * Este método construye y asigna las matrices correspondientes a la cámara y
     * a la matriz de visión.
     */
    private void buildCameraMatrices() {
        cameraObj = new Camera();
        cameraObj.setOrigin(new Vec3d(0.0f, 0.0f, -10.0f));
        matView = cameraObj.getMatView();
    }

    @Override
    public void initialize(GameContainer gc) {
        cube = initializationCube();
        buildProjectionMatrix(gc);
        buildCameraMatrices();

        cubeRotation.setX(0.01f);
        cubeRotation.setY(0.01f);
        cubeRotation.setZ(0.025f);

        lightDirection = new Vec3d(0.0f, 0.0f, -1.0f);
        float l = (float) Math.sqrt(
                lightDirection.getX() * lightDirection.getX() +
                lightDirection.getY() * lightDirection.getY() +
                lightDirection.getZ() * lightDirection.getZ()
        );
        lightDirection.setX(lightDirection.getX() / l);
        lightDirection.setY(lightDirection.getY() / l);
        lightDirection.setZ(lightDirection.getZ() / l);
    }

    @Override
    public void update(GameContainer gc, float dt) {
        // Showing information
        if ( gc.getInput().isKeyDown(KeyEvent.VK_SPACE) ) {
            isShowingInformation = !isShowingInformation;
        }

        // Change the render flag.
        if ( gc.getInput().isKeyDown(KeyEvent.VK_H) ) {
            renderFlag = RenderFlags.RENDER_WIRE;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_J) ) {
            renderFlag = RenderFlags.RENDER_FLAT;
        }

        // Camera panning
        if ( gc.getInput().isKeyDown(KeyEvent.VK_UP) ) {
            cameraObj.getOrigin().addToY(8.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_DOWN) ) {
            cameraObj.getOrigin().addToY(-8.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_RIGHT) ) {
            cameraObj.getOrigin().addToX(8.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_LEFT) ) {
            cameraObj.getOrigin().addToX(-8.0f * dt);
        }

        // Camera Rotation
        if ( gc.getInput().isKeyDown(KeyEvent.VK_W) ) {
            cameraObj.rotX(-2.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_S) ) {
            cameraObj.rotX(2.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_A) ) {
            cameraObj.rotY(-2.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_D) ) {
            cameraObj.rotY(2.0f * dt);
        }

        // Camera Zooming
        Vec3d forward = MatrixMath.vectorMul(cameraObj.getLookDirection(), - gc.getInput().getScroll() * dt);
        cameraObj.setOrigin(MatrixMath.vectorAdd(cameraObj.getOrigin(), forward));

        matView = cameraObj.getMatView();

        // Cube transformations
        transformCube();

    }

    /**
     * Esta función sirve para transformar los triangulos que forman el la malla (el cubo).
     */
    private void transformCube() {
        Mat4x4 matIdentity = MatrixMath.matrixMakeIdentity();
        Mat4x4 matRotX = MatrixMath.matrixMakeRotationX(cubeRotation.getX());
        matRotX = MatrixMath.matrixMultiplyMatrix(matIdentity, matRotX);
        Mat4x4 matRotY = MatrixMath.matrixMakeRotationY(cubeRotation.getY());
        Mat4x4 matRotXY = MatrixMath.matrixMultiplyMatrix(matRotY, matRotX);
        Mat4x4 matRotZ = MatrixMath.matrixMakeRotationZ(cubeRotation.getZ());
        Mat4x4 matRot = MatrixMath.matrixMultiplyMatrix(matRotXY, matRotZ);
        Mat4x4 matTranslation = MatrixMath.matrixMakeTranslation(cubeTranslation.getX(), cubeTranslation.getY(), cubeTranslation.getZ());
        Mat4x4 matRotTrans = MatrixMath.matrixMultiplyMatrix(matRot, matTranslation);
        for ( Triangle triangle : cube.getTris() ) {
            triangle.setP(transformPoints(matRotTrans, triangle.getP()));
        }
    }

    /**
     * Este método es el responsable de calcular un color en base al parámetro "light".
     * @param light es la iluminación. Entre 0 para nada y 1 para máxima iluminación.
     * @return devuleve un color en base a la iluminación.
     */
    private int calculateColor(float light) {
        light *= 256;
        return (255 << 24 | (int)light << 16 | (int)light << 8 | (int)light);
    }

    /**
     * Este método es el método general para transformar una array de puntos en 3D con
     * cualquier matriz. Por ejemplo, la matriz de visión o la matriz de proyección.
     * También se podría utilizar para transformar los triangulos con una matriz de
     * rotación o translación.
     * @param transform es la matriz con la que se van a transformar los puntos.
     * @param vec3ds son los puntos 3D que se van a transformar
     * @return devuelve un nuevo array con los puntos pasados por parámetro transformados.
     */
    private Vec3d[] transformPoints(Mat4x4 transform, Vec3d[] vec3ds) {
        Vec3d[] pointsTransformed = new Vec3d[3];
        for ( int i = 0; i < vec3ds.length; i++ ) {
            pointsTransformed[i] = MatrixMath.matrixMultiplyVector(transform, vec3ds[i]);
        }
        return pointsTransformed;
    }

    /**
     * La normal de un plano es un vector que es perpendicular a las dos direcciones
     * del plano. Es decir, hace 90 grados con las dos rectas (direcciones) que pertenecen
     * al plano.
     * @param points son los puntos que pertenecen a un plano.
     *               En nuestro caso, el plano es el triangulo.
     * @return devuleve la normal al plano de los formado por los puntos.
     */
    private Vec3d calculateNormalToPlane(Vec3d[] points) {
        Vec3d normal = new Vec3d();
        Vec3d line1 = new Vec3d();
        Vec3d line2 = new Vec3d();

        line1.setX(points[1].getX() - points[0].getX());
        line1.setY(points[1].getY() - points[0].getY());
        line1.setZ(points[1].getZ() - points[0].getZ());

        line2.setX(points[2].getX() - points[0].getX());
        line2.setY(points[2].getY() - points[0].getY());
        line2.setZ(points[2].getZ() - points[0].getZ());

        normal.setX( line1.getY() * line2.getZ() - line1.getZ() * line2.getY() );
        normal.setY( line1.getZ() * line2.getX() - line1.getX() * line2.getZ() );
        normal.setZ( line1.getX() * line2.getY() - line1.getY() * line2.getX() );

        return MatrixMath.vectorNormalise(normal);
    }

    /**
     * Esta función sirve para escalar y transladr los puntos proyectados a algo visible por pantalla.
     * @param pointsProjected los puntos proyectados en 2D.
     * @param width el ancho de la pantalla.
     * @param height el alto de la pantalla.
     */
    private void offSetProjectedPoints(Vec3d[] pointsProjected, int width, int height) {
        for ( Vec3d vec3d : pointsProjected ) {
            vec3d.addToX(1);
            vec3d.addToY(1);
            vec3d.multiplyXBy(0.5 * width);
            vec3d.multiplyYBy(0.5 * height);
        }
    }

    /**
     * Este método sirve para calcular los triangulos proyectados de un espacio 3D a 2D.
     * Se requiere del ancho y el alto de la pantalla para escalar y transladar el resultado en algo visible
     * por pantalla.
     * @param triangles los triangulos 3D a proyectar a 2D.
     * @param width el ancho de la pantalla.
     * @param height el alto de la pantalla.
     * @return un @ArrayList de triangulos proyectados en 2D.
     */
    private ArrayList<Triangle> projectTriangles(ArrayList<Triangle> triangles, int width, int height) {
        ArrayList<Triangle> trianglesProjected = new ArrayList<>();
        int color;
        for ( Triangle triangle : triangles ) {
            Vec3d[] pointsViewed = transformPoints(matView, triangle.getP());

            Vec3d normal = calculateNormalToPlane(pointsViewed);

            Vec3d diffViewedPointCameraOrigin = new Vec3d(
                    pointsViewed[0].getX() - cameraObj.getOrigin().getX(),
                    pointsViewed[0].getY() - cameraObj.getOrigin().getY(),
                    pointsViewed[0].getZ() - cameraObj.getOrigin().getZ()
            );

            if ( MatrixMath.vectorDotProduct(normal, diffViewedPointCameraOrigin) < 0.0f ) {
                color = calculateColor(MatrixMath.vectorDotProduct(normal, lightDirection));

                ArrayList<Triangle> clippedTriangles = MatrixMath.triangleClipAgainstPlane(
                        new Vec3d(0.0f, 0.0f, 0.1f),
                        new Vec3d(0.0f, 0.0f, 1.0f),
                        new Triangle(pointsViewed)
                );

                for ( Triangle triangleClipped : clippedTriangles ) {
                    Vec3d[] pointsProjected = transformPoints(projectionMatrix, triangleClipped.getP());

                    offSetProjectedPoints(pointsProjected, width, height);

                    trianglesProjected.add(new Triangle(pointsProjected, triangle.getT(), color));
                }
            }
        }

        return trianglesProjected;
    }

    /**
     * Muestra información por pantalla, como la rueda del ratón, y la información de la cámara.
     * @param gc el objeto GameContainer que contiene información como el input.
     *           Si se creara en una variable global de para el la rueda del ratón
     *           y se actualizase en el método de update, no sería necesario este
     *           parámetro.
     * @param r el objeto Render con todas las funciones necesarias para dibujar.
     */
    private void showingInformation(GameContainer gc, Renderer r) {
        r.drawText("Mouse wheel: " + gc.getInput().getScroll(), 10, 10, 0xffffffff);
        cameraObj.showInformation(r, 10, 40, 0xffffffff);
    }

    /**
     * Este método recorre un ArrayList de triangulos y los dibuja, en función del tipo de renderizado
     * que este activado.
     * @param r el objeto render con todas las funciones de dibujado.
     * @param triangles los triangulos a dibujar. En este caso, lo triangulos proyectados.
     */
    private void renderTriangle(Renderer r, ArrayList<Triangle> triangles) {
        for ( Triangle tri : triangles ) {
            switch ( renderFlag ) {
                case RENDER_FLAT:
                    r.drawFillTriangle(
                            (int)tri.getP()[0].getX(), (int)tri.getP()[0].getY(),
                            (int)tri.getP()[1].getX(), (int)tri.getP()[1].getY(),
                            (int)tri.getP()[2].getX(), (int)tri.getP()[2].getY(),
                            tri.getColor()
                    );
                    break;
                case RENDER_WIRE:
                    r.drawTriangle(
                            (int)tri.getP()[0].getX(), (int)tri.getP()[0].getY(),
                            (int)tri.getP()[1].getX(), (int)tri.getP()[1].getY(),
                            (int)tri.getP()[2].getX(), (int)tri.getP()[2].getY(),
                            tri.getColor()
                    );
                    break;
            }
        }
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        ArrayList<Triangle> projectedTriangles = projectTriangles(cube.getTris(), gc.getWidth(), gc.getHeight());

        projectedTriangles.sort(
                (o1, o2) -> {
                    float medZ1 = 0;
                    for ( Vec3d vec3d : o1.getP() ) {
                        medZ1 += vec3d.getZ();
                    }
                    medZ1 /= o1.getP().length;
                    float medZ2 = 0;
                    for ( Vec3d vec3d : o2.getP() ) {
                        medZ2 += vec3d.getZ();
                    }
                    medZ2 /= o2.getP().length;
                    return Float.compare(medZ1, medZ2);
                }
        );

        renderTriangle(r, projectedTriangles);

        if ( isShowingInformation ) {
            showingInformation(gc, r);
        }
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new Test3DEngine("Test 3D Engine"));
        gc.start();
    }

}
