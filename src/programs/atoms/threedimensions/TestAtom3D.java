package programs.atoms.threedimensions;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.gfx.engine3d.normal.*;
import engine.gfx.images.Image;
import engine.maths.Mat3x3;
import engine.maths.MatrixOperations;
import engine.maths.Mat4x4;
import engine.maths.Vec3d;
import programs.atoms.CarbonHybridization;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 */

public class TestAtom3D extends AbstractGame {

    private Mat4x4 projectionMatrix = new Mat4x4();

    private Mat4x4 matView;

    private Camera cameraObj;

    private Atom3D atom3D;

    private Image hydrogen = new Image("/atoms/Oxygen.png");

    private Vec3d mousePosIni = new Vec3d();

    private Vec3d mousePosFin = new Vec3d();

    private Vec3d atomTranslation = new Vec3d();

    private Vec3d atomRotation = new Vec3d();

    private float angleRotX = 0;

    private float angleRotY = 0;

    private boolean isDrawingSprites = true;

    private boolean isMovingCamera = true;

    private boolean isShowingTexts = true;

    private TestAtom3D(String title) {
        super(title);
    }

    @Override
    public void initialize(GameContainer gc) {
        atom3D = new Atom3D(new Vec3d(0, 0, 0), CarbonHybridization.SP);

        float near = 0.1f;
        float far = 1000.0f;
        float fovDegrees = 90.0f;
        float aspectRatio = (float)gc.getHeight() / (float)gc.getWidth();
        projectionMatrix = MatrixMath.matrixMakeProjection(fovDegrees, aspectRatio, near, far);

        cameraObj = new Camera();
        cameraObj.setOrigin(new Vec3d(0.0f, 0.0f, -10.0f));
        matView = cameraObj.getMatView();
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if ( gc.getInput().isKeyDown(KeyEvent.VK_CONTROL) ) {
            isMovingCamera = !isMovingCamera;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_T) ) {
            isShowingTexts = !isShowingTexts;
        }

        if ( isMovingCamera ) {
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
        } else {
            // Translate atom
            if ( gc.getInput().isKeyDown(KeyEvent.VK_UP) ) {
                atomTranslation.addToY(-1.0f * dt);
            }
            if ( gc.getInput().isKeyDown(KeyEvent.VK_DOWN) ) {
                atomTranslation.addToY(1.0f * dt);
            }
            if ( gc.getInput().isKeyDown(KeyEvent.VK_RIGHT) ) {
                atomTranslation.addToX(1.0f * dt);
            }
            if ( gc.getInput().isKeyDown(KeyEvent.VK_LEFT) ) {
                atomTranslation.addToX(-1.0f * dt);
            }

            atomTranslation.addToZ(gc.getInput().getScroll() * dt);
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

        matView = cameraObj.getMatView();

        // Draw Sprites and molecule size
        if ( gc.getInput().isKeyDown(KeyEvent.VK_SPACE) ) {
            isDrawingSprites = !isDrawingSprites;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_Z) ) {
            float actualRadius = atom3D.getRadius();
            atom3D.setRadius((float)(actualRadius + 1.5 * dt));
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_X) ) {
            float actualRadius = atom3D.getRadius();
            atom3D.setRadius((float)(actualRadius - 1.5 * dt));
        }

        // Molecule rotation
        if ( gc.getInput().isButtonDown(1) ) {
            mousePosIni.set(gc.getInput().getMouseX(), gc.getInput().getMouseY(), 0.0f);
        }
        if ( gc.getInput().isButtonUp(1) ) {
            mousePosFin.set(gc.getInput().getMouseX(), gc.getInput().getMouseY(), 0.0f);
            if ( !(mousePosFin.getX() == mousePosIni.getX() && mousePosFin.getY() == mousePosIni.getY()) ) {
                float m1 = 3.0f / ((gc.getWidth() / 2.0f) - mousePosIni.getX());
                float m2 = 3.0f / ((gc.getWidth() / 2.0f) - mousePosFin.getX());

                angleRotY = 100.0f * (float) Math.atan((m1 - m2) / (1 + m2 * m1));

                m1 = 3.0f / ((gc.getWidth() / 2.0f) - mousePosIni.getY());
                m2 = 3.0f / ((gc.getWidth() / 2.0f) - mousePosFin.getY());

                angleRotX = 100.0f * (float) Math.atan((m1 - m2) / (1 + m2 * m1));
            }
        }

        if ( angleRotX != 0 ) {
            angleRotX = (angleRotX > 0) ? angleRotX - 1.5f * dt : angleRotX + 1.5f * dt;
        }
        if ( angleRotY != 0 ) {
            angleRotY = (angleRotY > 0) ? angleRotY - 1.5f * dt : angleRotY + 1.5f * dt;
        }

        // Atom transformations
        atomRotation.addToX(angleRotX * dt);
        atomRotation.addToY(angleRotY * dt);
        Mat4x4 matScale = MatrixMath.matrixMakeScale(atom3D.getRadius(), atom3D.getRadius(), atom3D.getRadius());
        Mat4x4 matRotX = MatrixMath.matrixMakeRotationX(atomRotation.getX());
        Mat4x4 matRotY = MatrixMath.matrixMakeRotationY(atomRotation.getY());
        Mat4x4 matRot = MatrixMath.matrixMultiplyMatrix(matRotX, matRotY);
        Mat4x4 matTranslation = MatrixMath.matrixMakeTranslation(atomTranslation.getX(), atomTranslation.getY(), atomTranslation.getZ());
        Mat4x4 matRotTrans = MatrixMath.matrixMultiplyMatrix(matRot, matTranslation);
        Mat4x4 matFinal = MatrixMath.matrixMultiplyMatrix(matScale, matRotTrans);
        atom3D.transform(matFinal);

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
        Mat3x3 scale = new Mat3x3();
        Mat3x3 offsetImage = new Mat3x3();
        Mat3x3 offsetScreen = new Mat3x3();
        Mat3x3 scaledOffsetImage;
        Mat3x3 transform;

        scale.setAsScale(1 / (point.getZ() / 3), 1 / (point.getZ() / 3));
        offsetImage.setAsTranslate(- image.getW() / 2.0f, - image.getH() / 2.0f);
        scaledOffsetImage = MatrixOperations.multiply(offsetImage, scale);
        offsetScreen.setAsTranslate(point.getX(), point.getY());
        transform = MatrixOperations.multiply(scaledOffsetImage, offsetScreen);
        r.drawImage(image, transform);
    }

    private void renderAtom(GameContainer gc, Renderer r) {
        ArrayList<Vec3d> pointsProjected = projectPoints(atom3D.getPoints3dFinal(), gc.getWidth(), gc.getHeight());

        if ( isDrawingSprites ) {
            Vec3d center = pointsProjected.get(0);
            pointsProjected.sort((o1, o2) -> Float.compare(o2.getZ(), o1.getZ()));

            for ( Vec3d pProjected : pointsProjected ) {
                if ( pProjected.equals(center) ) {
                    drawAtomSprite(r, atom3D.getImage(), pProjected);
                } else {
                    Mat3x3 scaleImage = new Mat3x3();
                    Mat3x3 scaleDistance = new Mat3x3();
                    Mat3x3 scale;
                    Mat3x3 offsetImage = new Mat3x3();
                    Mat3x3 offsetScreen = new Mat3x3();
                    Mat3x3 scaledOffsetImage;
                    Mat3x3 transform;

                    scaleDistance.setAsScale(1 / (pProjected.getZ() / 3), 1 / (pProjected.getZ() / 3));
                    scaleImage.setAsScale(0.9f, 0.9f);
                    scale = MatrixOperations.multiply(scaleImage, scaleDistance);
                    offsetImage.setAsTranslate(- hydrogen.getW() / 2.0f, - hydrogen.getH() / 2.0f);
                    scaledOffsetImage = MatrixOperations.multiply(offsetImage, scale);
                    offsetScreen.setAsTranslate(pProjected.getX(), pProjected.getY());
                    transform = MatrixOperations.multiply(scaledOffsetImage, offsetScreen);
                    r.drawImage(hydrogen, transform);
                }
            }

        } else {

            for ( int i = 1; i < pointsProjected.size(); i++ ) {
                r.drawLine(
                        (int)pointsProjected.get(0).getX(), (int)pointsProjected.get(0).getY(),
                        (int)pointsProjected.get(i).getX(), (int)pointsProjected.get(i).getY(),
                        0xffffffff);
            }

        }
    }

    private void renderTexts(GameContainer gc, Renderer r) {
        r.drawText(String.format("Atom position X: %.3f Y: %.3f Z: %.3f.",
                atom3D.getPoints3dFinal()[0].getX(),
                atom3D.getPoints3dFinal()[0].getY(),
                atom3D.getPoints3dFinal()[0].getZ()
        ), 10, 10, 0xffffffff);
        r.drawText(String.format("Atom radius: %.3f.", atom3D.getRadius()), 10, 40, 0xffffffff);

        r.drawText(String.format("Atom rotation X axis: %.3f degrees.", ((atomRotation.getX() * Math.PI / 180) % 360)),
                10, 70, 0xffffffff);
        r.drawText(String.format("Atom rotation Y axis: %.3f degrees.", ((atomRotation.getY() * Math.PI / 180) % 360)),
                10, 100, 0xffffffff);
        r.drawText(String.format("Atom rotation Z axis: %.3f degrees.", ((atomRotation.getZ() * Math.PI / 180) % 360)),
                10, 130, 0xffffffff);

        r.drawText("Difference between Mouse Star and Mouse End", 10, 160, 0xffffffff);
        float diffX = mousePosIni.getX() - mousePosFin.getX();
        float diffY = mousePosIni.getY() - mousePosFin.getY();
        r.drawText(String.format("X: %.3f Y: %.3f", diffX, diffY), 10, 190, 0xffffffff);

        r.drawText(String.format("Scroll: %d ", gc.getInput().getScroll()),
                10, 220, 0xffffffff);

        r.drawText(String.format("Arrows: %s.", isMovingCamera ? "moving camera" : "moving molecule"),
                10, 250, 0xffffffff);

        cameraObj.showInformation(r, 10, 280, 0xffffffff);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.clear(0xff002366);
        renderAtom(gc, r);
        r.drawCircle((int)(mousePosIni.getX()), (int)(mousePosIni.getY()), 10, 0xffff0000);
        r.drawCircle((int)(mousePosFin.getX()), (int)(mousePosFin.getY()), 10, 0xffffff00);
        if ( isShowingTexts ) {
            renderTexts(gc, r);
        }
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new TestAtom3D("Test atoms 3D"));
        gc.start();
    }

}
