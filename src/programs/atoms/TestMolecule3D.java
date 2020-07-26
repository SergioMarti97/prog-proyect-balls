package programs.atoms;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.gfx.engine3d.normal.Mat4x4;
import engine.gfx.engine3d.normal.MatrixMath;
import engine.gfx.engine3d.normal.Vec3d;
import engine.gfx.images.Image;
import engine.gfx.images.maths.Matrix3x3Float;
import engine.gfx.images.maths.MatrixOperations;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TestMolecule3D extends AbstractGame {

    private Mat4x4 projectionMatrix = new Mat4x4();

    private Mat4x4 matView;

    private Molecule3D molecule;

    private Camera cameraObj;

    private TestMolecule3D(String title) {
        super(title);
    }

    @Override
    public void initialize(GameContainer gc) {
        molecule = new Molecule3D();
        molecule.getAtoms().add(new Atom3D(new Vec3d(0, 0, 0), CarbonHybridization.SP3));
        Atom3D atom3D = molecule.getAtoms().get(0);
        Vec3d linkPosition = atom3D.getPoints3dFinal()[1];
        Mat4x4 matRotation = MatrixMath.matrixMakeRotationZ((float) Math.PI);
        Mat4x4 matTranslation = MatrixMath.matrixMakeTranslation(linkPosition.getX(), linkPosition.getY(), linkPosition.getZ());
        Atom3D atom3D2 = new Atom3D(new Vec3d(0, 0, 0), CarbonHybridization.SP3);
        atom3D2.transform(MatrixMath.matrixMultiplyMatrix(matRotation, matTranslation));
        molecule.getAtoms().add(atom3D2);

        float near = 0.1f;
        float far = 1000.0f;
        float fovDegrees = 90.0f;
        float aspectRatio = (float)gc.getHeight() / (float)gc.getWidth();
        projectionMatrix = MatrixMath.matrixMakeProjection(fovDegrees, aspectRatio, near, far);

        cameraObj = new Camera();
        matView = cameraObj.getMatView();
    }

    @Override
    public void update(GameContainer gc, float dt) {
        // Panning
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

        // Camera Zooming
        Vec3d forward = MatrixMath.vectorMul(cameraObj.getLookDirection(), - gc.getInput().getScroll() * dt);
        cameraObj.setOrigin(MatrixMath.vectorAdd(cameraObj.getOrigin(), forward));

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

        matView = cameraObj.getMatView();

        if ( gc.getInput().isKeyDown(KeyEvent.VK_Z) ) {
            for ( Atom3D atom3D : molecule.getAtoms() ) {
                float actualRadius = atom3D.getRadius();
                atom3D.setRadius((float)(actualRadius + 1.5 * dt));
            }
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_X) ) {
            for ( Atom3D atom3D : molecule.getAtoms() ) {
                float actualRadius = atom3D.getRadius();
                atom3D.setRadius((float) (actualRadius - 1.5 * dt));
            }
        }
    }

    private ArrayList<Vec3d> projectPoints(Vec3d[] points, int width, int height) {
        ArrayList<Vec3d> pointsProjected = new ArrayList<>();

        for ( Vec3d point : points ) {
            Vec3d pTranslated = new Vec3d(point.getX(), point.getY(), point.getZ());

            pTranslated.addToZ(3);

            Vec3d pViewed = MatrixMath.matrixMultiplyVector(matView, pTranslated);

            Vec3d pProjected = MatrixMath.matrixMultiplyVector(projectionMatrix, pViewed);

            pProjected.addToX(1);
            pProjected.addToY(1);
            pProjected.multiplyXBy(0.5 * width);
            pProjected.multiplyYBy(0.5 * height);

            pointsProjected.add(pProjected);
        }

        return pointsProjected;
    }

    private void drawAtomSprite(Renderer r, Image image, Vec3d point) {
        Matrix3x3Float scale = new Matrix3x3Float();
        Matrix3x3Float offsetImage = new Matrix3x3Float();
        Matrix3x3Float offsetScreen = new Matrix3x3Float();
        Matrix3x3Float scaledOffsetImage;
        Matrix3x3Float transform;

        scale.setAsScale(1 / (point.getZ() / 3), 1 / (point.getZ() / 3));
        offsetImage.setAsTranslate(- image.getW() / 2.0f, - image.getH() / 2.0f);
        scaledOffsetImage = MatrixOperations.multiply(offsetImage, scale);
        offsetScreen.setAsTranslate(point.getX(), point.getY());
        transform = MatrixOperations.multiply(scaledOffsetImage, offsetScreen);
        r.drawImage(image, transform);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.clear(0xff002366);
        for ( Atom3D atom3D : molecule.getAtoms() ) {
            ArrayList<Vec3d> pointsProjected = projectPoints(atom3D.getPoints3dFinal(), gc.getWidth(), gc.getHeight());

            Vec3d center = pointsProjected.get(0);

            pointsProjected.sort((o1, o2) -> Float.compare(o2.getZ(), o1.getZ()));

            for ( Vec3d pProjected : pointsProjected ) {
                if ( pProjected.equals(center) ) {
                    drawAtomSprite(r, atom3D.getImage(), pProjected);
                } else {
                    r.drawCircle((int)(pProjected.getX()), (int)(pProjected.getY()), 10, 0xffff0000);
                }
            }
        }
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new TestMolecule3D("Test molecule 3D"));
        gc.start();
    }

}
