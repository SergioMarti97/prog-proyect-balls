package programs.atoms.threedimensions;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.gfx.engine3d.normal.Camera;
import engine.gfx.engine3d.normal.Mat4x4;
import engine.gfx.engine3d.normal.MatrixMath;
import engine.gfx.engine3d.normal.Vec3d;
import engine.gfx.images.Image;
import engine.gfx.images.maths.Matrix3x3Float;
import engine.gfx.images.maths.MatrixOperations;
import programs.atoms.AtomKind;
import programs.atoms.CarbonHybridization;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TestMolecule3D extends AbstractGame {

    private Mat4x4 projectionMatrix = new Mat4x4();

    private Mat4x4 matView;

    private Molecule3D molecule;

    private Camera cameraObj;

    private Vec3d moleculeRotation = new Vec3d();

    private boolean isRenderingSprites = true;

    private TestMolecule3D(String title) {
        super(title);
    }

    @Override
    public void initialize(GameContainer gc) {
        molecule = new Molecule3D();
        molecule.addAtom(new Atom3D(new Vec3d(0, 0, 0), CarbonHybridization.SP3), 0, 1);
        molecule.addAtom(new Atom3D(new Vec3d(0, 0, 0), CarbonHybridization.SP3, AtomKind.OXYGEN), 0, 4);

        float near = 0.1f;
        float far = 1000.0f;
        float fovDegrees = 90.0f;
        float aspectRatio = (float)gc.getHeight() / (float)gc.getWidth();
        projectionMatrix = MatrixMath.matrixMakeProjection(fovDegrees, aspectRatio, near, far);

        cameraObj = new Camera();
        cameraObj.setOrigin(new Vec3d(0.0f, 0.0f, -5.0f));
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

        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD4) ) {
            moleculeRotation.addToX(2.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD6) ) {
            moleculeRotation.addToX(-2.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD8) ) {
            moleculeRotation.addToY(2.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD2) ) {
            moleculeRotation.addToY(-2.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD7) ) {
            moleculeRotation.addToZ(2.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD3) ) {
            moleculeRotation.addToZ(-2.0f * dt);
        }

        moleculeRotation.addToX(0);
        moleculeRotation.addToY(1.0f * dt);
        moleculeRotation.addToZ(0);

        Mat4x4 matRotX = MatrixMath.matrixMakeRotationX(moleculeRotation.getX());
        Mat4x4 matRotY = MatrixMath.matrixMakeRotationY(moleculeRotation.getY());
        Mat4x4 matRotZ = MatrixMath.matrixMakeRotationZ(moleculeRotation.getZ());
        Mat4x4 matRotXY = MatrixMath.matrixMultiplyMatrix(matRotX, matRotY);
        Mat4x4 matRot = MatrixMath.matrixMultiplyMatrix(matRotZ, matRotXY);
        //molecule.rotate(matRot);

        if ( gc.getInput().isKeyDown(KeyEvent.VK_SPACE) ) {
            isRenderingSprites = !isRenderingSprites;
        }
    }

    private ArrayList<Vec3d> projectPoints(Vec3d[] points, int width, int height) {
        ArrayList<Vec3d> pointsProjected = new ArrayList<>();

        for ( Vec3d point : points ) {
            Vec3d pViewed = MatrixMath.matrixMultiplyVector(matView, point);
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
        Vec3d center;
        int i;
        for ( i = 0; i < molecule.getAtoms().size(); i++ ) {
            ArrayList<Vec3d> pointsProjected = projectPoints(molecule.getAtoms().get(i).getPoints3dFinal(), gc.getWidth(), gc.getHeight());

            center = pointsProjected.get(0);

            pointsProjected.sort((o1, o2) -> Float.compare(o2.getZ(), o1.getZ()));


            for ( Vec3d point : pointsProjected ) {
                if ( point.equals(center) ) {
                    drawAtomSprite(r, molecule.getAtoms().get(i).getImage(), point);
                    r.drawText(String.format("%d", molecule.getAtoms().get(i).getId()), (int)point.getX(), (int)point.getY(), 0xffffffff);
                } else {
                    switch ( i % 3 ) {
                        case 0: default:
                            r.drawCircle((int)(point.getX()), (int)(point.getY()),
                                    10, 0xffff0000);
                            break;
                        case 1:
                            r.drawCircle((int)(point.getX()), (int)(point.getY()),
                                    10, 0xff00ff00);
                            break;
                        case 2:
                            r.drawCircle((int)(point.getX()), (int)(point.getY()),
                                    10, 0xff0000ff);
                            break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new TestMolecule3D("Test molecule 3D"));
        gc.start();
    }

}
