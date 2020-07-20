package programs.atoms;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.gfx.engine3d.normal.*;
import engine.gfx.images.Image;
import engine.gfx.images.maths.Matrix3x3Float;
import engine.gfx.images.maths.MatrixOperations;

import java.util.ArrayList;

public class TestAtom3D extends AbstractGame {

    private Mat4x4 projectionMatrix = new Mat4x4();

    private Mat4x4 rotationXMatrix = new Mat4x4();

    private Mat4x4 rotationZMatrix = new Mat4x4();

    private Mat4x4 scaleMatrix = new Mat4x4();

    private Mesh cube;

    private Atom3D atom3D;

    private Image imageCarbon;

    private Image imageHydrogen;

    private float rotation = 0;

    private TestAtom3D(String title) {
        super(title);
    }

    @Override
    public void initialize(GameContainer gc) {
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

        atom3D = new Atom3D(new Vec3d(0, 0, 0));

        imageCarbon = new Image("/atoms/Carbon.png");
        imageHydrogen = new Image("/atoms/Hydrogen.png");

        float near = 0.1f;
        float far = 1000.0f;
        float fovDegrees = 90.0f;
        float aspectRatio = (float)gc.getHeight() / (float)gc.getWidth();
        projectionMatrix = MatrixMath.matrixMakeProjection(fovDegrees, aspectRatio, near, far);
    }

    @Override
    public void update(GameContainer gc, float dt) {
        scaleMatrix = MatrixMath.matrixMakeScale(0.1f, 0.1f, 0.1f);
        rotationXMatrix = MatrixMath.matrixMakeRotationX(rotation);
        rotationZMatrix = MatrixMath.matrixMakeRotationZ(rotation);
        rotation += 1.0f * dt;
    }

    @Override
    public void render(GameContainer gc, Renderer r) {

        r.drawImage(imageHydrogen,
                gc.getInput().getMouseX() - (imageHydrogen.getW() / 2),
                gc.getInput().getMouseY() - (imageHydrogen.getH() / 2));

        ArrayList<Vec3d> pointsProjected = new ArrayList<>();
        for ( Vec3d point : atom3D.getPoints3d() ) {
            Vec3d pTranslated;
            Vec3d pRotatedZ;
            Vec3d pRotatedZX;
            Vec3d pScaled;

            pScaled = MatrixMath.matrixMultiplyVector(scaleMatrix, point);

            pRotatedZ = MatrixMath.matrixMultiplyVector(rotationZMatrix, pScaled);

            pRotatedZX = MatrixMath.matrixMultiplyVector(rotationXMatrix, pRotatedZ);

            pTranslated = new Vec3d(pRotatedZX.getX(), pRotatedZX.getY(), pRotatedZX.getZ());

            pTranslated.addToZ(3);

            Vec3d pProjected;

            pProjected = MatrixMath.matrixMultiplyVector(projectionMatrix, pTranslated);

            pProjected.addToX(1);
            pProjected.addToY(1);
            pProjected.multiplyXBy(0.5 * gc.getWidth());
            pProjected.multiplyYBy(0.5 * gc.getHeight());

            pointsProjected.add(pProjected);
        }

        for ( int i = 1; i < pointsProjected.size(); i++ ) {
            r.drawLine(
                    (int)pointsProjected.get(0).getX(), (int)pointsProjected.get(0).getY(),
                    (int)pointsProjected.get(i).getX(), (int)pointsProjected.get(i).getY(),
                    0xffffffff);
        }

        pointsProjected.sort((o1, o2) -> Float.compare(o2.getZ(), o1.getZ()));

        for ( Vec3d pProjected : pointsProjected ) {

            Matrix3x3Float scale = new Matrix3x3Float();
            Matrix3x3Float offsetImage = new Matrix3x3Float();
            Matrix3x3Float offsetScreen = new Matrix3x3Float();
            Matrix3x3Float scaledOffsetImage;
            Matrix3x3Float transform;

            scale.setAsScale( 1 / (pProjected.getZ() / 3), 1 / (pProjected.getZ() / 3));
            offsetImage.setAsTranslate(- imageCarbon.getW() / 2.0f, - imageCarbon.getH() / 2.0f);
            scaledOffsetImage = MatrixOperations.multiply(offsetImage, scale);
            offsetScreen.setAsTranslate(pProjected.getX(), pProjected.getY());
            transform = MatrixOperations.multiply(scaledOffsetImage, offsetScreen);
            r.drawImage(imageCarbon, transform);
        }

        r.drawText(String.format("Rotation X axis: %.3f degrees.", (rotation % 360)), 10, 10, 0xffffffff);
        r.drawText(String.format("Rotation Z axis: %.3f degrees.", (rotation % 360)), 10, 40, 0xffffffff);

    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new TestAtom3D("Test atoms 3D"));
        gc.start();
    }

}
