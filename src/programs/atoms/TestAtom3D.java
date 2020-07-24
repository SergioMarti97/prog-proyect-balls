package programs.atoms;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.gfx.engine3d.normal.*;
import engine.gfx.images.maths.Matrix3x3Float;
import engine.gfx.images.maths.MatrixOperations;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TestAtom3D extends AbstractGame {

    private Mat4x4 projectionMatrix = new Mat4x4();

    private Atom3D atom3D;

    private Vec3d[] axis = new Vec3d[4];

    private Camera cameraObj;

    private Mat4x4 matView;

    private Vec3d camera;

    private Vec3d lookDirection;

    private Vec3d up = new Vec3d(0.0f, 1.0f, 0.0f);

    private Vec3d target = new Vec3d(0.0f, 0.0f, 1.0f);

    private Vec3d cameraRot = new Vec3d(0.0f, 0.0f, 0.0f);

    private Vec3d mousePosIni = new Vec3d();

    private Vec3d mousePosFin = new Vec3d();

    private float rotationX = 0;

    private float rotationY = 0;

    private float rotationZ = 0;

    float angleRotY = 0;

    float angleRotX = 0;

    private boolean isDrawingSprites = true;

    private TestAtom3D(String title) {
        super(title);
    }

    private Mat4x4 setMatCamera(Vec3d up, Vec3d target, Mat4x4 transform) {
        lookDirection = MatrixMath.matrixMultiplyVector(transform, target);
        target = MatrixMath.vectorAdd(camera, lookDirection);
        return MatrixMath.matrixPointAt(camera, target, up);
    }

    @Override
    public void initialize(GameContainer gc) {
        camera = new Vec3d();
        lookDirection = new Vec3d();

        atom3D = new Atom3D(new Vec3d(0, 0, 0), CarbonHybridization.SP3);

        // Matríz de proyección
        float near = 0.1f;
        float far = 1000.0f;
        float fovDegrees = 90.0f;
        float aspectRatio = (float)gc.getHeight() / (float)gc.getWidth();
        projectionMatrix = MatrixMath.matrixMakeProjection(fovDegrees, aspectRatio, near, far);

        // Cámara
        Mat4x4 matCamera = setMatCamera(up, target, MatrixMath.matrixMakeRotationY(cameraRot.getY()));
        matView = MatrixMath.matrixQuickInverse(matCamera);
        cameraObj = new Camera();

        // Inicialización a lo bruto de un eje de coordenadas cartesiano.
        axis[0] = new Vec3d(0, 0, 0);
        axis[1] = new Vec3d(1, 0, 0);
        axis[2] = new Vec3d(0, 1, 0);
        axis[3] = new Vec3d(0, 0, 1);
    }

    @Override
    public void update(GameContainer gc, float dt) {

        // Panning
        if ( gc.getInput().isKeyDown(KeyEvent.VK_UP) ) {
            camera.addToY(8.0 * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_DOWN) ) {
            camera.addToY(-8.0 * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_RIGHT) ) {
            camera.addToX(8.0 * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_LEFT) ) {
            camera.addToX(-8.0 * dt);
        }

        // Camera Zooming
        Vec3d forward = MatrixMath.vectorMul(lookDirection, 1.0f * dt);
        if ( gc.getInput().getScroll() > 0 ) {
            camera = MatrixMath.vectorSub(camera, forward);
        }
        if ( gc.getInput().getScroll() < 0 ) {
            camera = MatrixMath.vectorAdd(camera, forward);
        }

        // Camera Rotation
        if ( gc.getInput().isKeyDown(KeyEvent.VK_W) ) {
            float rotX = cameraRot.getX();
            rotX -= 2.0f * dt;
            cameraRot.setX(rotX);
            cameraObj.rotX(-2.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_S) ) {
            float rotX = cameraRot.getX();
            rotX += 2.0f * dt;
            cameraRot.setX(rotX);
            cameraObj.rotX(2.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_A) ) {
            float rotY = cameraRot.getY();
            rotY -= 2.0f * dt;
            cameraRot.setY(rotY);
            cameraObj.rotY(-2.0f * dt);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_D) ) {
            float rotY = cameraRot.getY();
            rotY += 2.0f * dt;
            cameraRot.setY(rotY);
            cameraObj.rotY(2.0f * dt);
        }

        Mat4x4 matCameraRotX = MatrixMath.matrixMakeRotationX(cameraRot.getX());
        Mat4x4 matCameraRotY = MatrixMath.matrixMakeRotationY(cameraRot.getY());
        Mat4x4 matCameraRot = MatrixMath.matrixMultiplyMatrix(matCameraRotX, matCameraRotY);
        Mat4x4 matCamera = setMatCamera(up, target, matCameraRot);
        matView = MatrixMath.matrixQuickInverse(matCamera);

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
                // Rotación en Y
                float m1 = 3.0f / ((gc.getWidth() / 2.0f) - mousePosIni.getX());
                float m2 = 3.0f / ((gc.getWidth() / 2.0f) - mousePosFin.getX());

                angleRotY = 100.0f * (float) Math.atan((m1 - m2) / (1 + m2 * m1));

                // Rotación en X
                m1 = 3.0f / ((gc.getWidth() / 2.0f) - mousePosIni.getY());
                m2 = 3.0f / ((gc.getWidth() / 2.0f) - mousePosFin.getY());

                angleRotX = 100.0f * (float) Math.atan((m1 - m2) / (1 + m2 * m1));

                //rotationX += angleRotX;
                //rotationY += angleRotY;
            }
        }

        if ( angleRotX != 0 ) {
            angleRotX = (angleRotX > 0) ? angleRotX - 1.5f * dt : angleRotX + 1.5f * dt;
        }

        if ( angleRotY != 0 ) {
            angleRotY = (angleRotY > 0) ? angleRotY - 1.5f * dt : angleRotY + 1.5f * dt;
        }

        // Transformaciones del átomo
        rotationX += angleRotX * dt;
        rotationY += angleRotY * dt; // += 1.5f * dt;
        Mat4x4 matScale = MatrixMath.matrixMakeScale(atom3D.getRadius(), atom3D.getRadius(), atom3D.getRadius());
        Mat4x4 matRotX = MatrixMath.matrixMakeRotationX(rotationX);
        Mat4x4 matRotY = MatrixMath.matrixMakeRotationY(rotationY);
        Mat4x4 matRot = MatrixMath.matrixMultiplyMatrix(matRotX, matRotY);
        Mat4x4 matFinal = MatrixMath.matrixMultiplyMatrix(matScale, matRot);
        atom3D.transform(matFinal);
    }

    private void renderAxis(GameContainer gc, Renderer r) {
        ArrayList<Vec3d> pointsProjected = new ArrayList<>();

        for ( Vec3d point : axis ) {
            Vec3d pTranslated = new Vec3d(point.getX(), point.getY(), point.getZ());

            pTranslated.addToZ(3);

            Vec3d pViewed = MatrixMath.matrixMultiplyVector(matView, pTranslated);

            Vec3d pProjected = MatrixMath.matrixMultiplyVector(projectionMatrix, pViewed);

            pProjected.addToX(1);
            pProjected.addToY(8);
            pProjected.multiplyXBy(0.1 * gc.getWidth());
            pProjected.multiplyYBy(0.1 * gc.getHeight());

            pointsProjected.add(pProjected);
        }

        //pointsProjected.sort((o1, o2) -> Float.compare(o2.getZ(), o1.getZ()));

        r.drawLine(
                (int)pointsProjected.get(0).getX(), (int)pointsProjected.get(0).getY(),
                (int)pointsProjected.get(1).getX(), (int)pointsProjected.get(1).getY(),
                0xffff0000);

        r.drawLine(
                (int)pointsProjected.get(0).getX(), (int)pointsProjected.get(0).getY(),
                (int)pointsProjected.get(2).getX(), (int)pointsProjected.get(2).getY(),
                0xff00ff00);

        r.drawLine(
                (int)pointsProjected.get(0).getX(), (int)pointsProjected.get(0).getY(),
                (int)pointsProjected.get(3).getX(), (int)pointsProjected.get(3).getY(),
                0xff0000ff);
    }

    private void renderAtom(GameContainer gc, Renderer r) {
        ArrayList<Vec3d> pointsProjected = new ArrayList<>();

        for ( Vec3d point : atom3D.getPoints3dFinal() ) {
            Vec3d pTranslated = new Vec3d(point.getX(), point.getY(), point.getZ());

            pTranslated.addToZ(3);

            Vec3d pViewed = MatrixMath.matrixMultiplyVector(matView, pTranslated);

            Vec3d pProjected = MatrixMath.matrixMultiplyVector(projectionMatrix, pViewed);

            pProjected.addToX(1);
            pProjected.addToY(1);
            pProjected.multiplyXBy(0.5 * gc.getWidth());
            pProjected.multiplyYBy(0.5 * gc.getHeight());

            pointsProjected.add(pProjected);
        }

        if ( isDrawingSprites ) {
            pointsProjected.sort((o1, o2) -> Float.compare(o2.getZ(), o1.getZ()));

            for ( Vec3d pProjected : pointsProjected ) {
                Matrix3x3Float scale = new Matrix3x3Float();
                Matrix3x3Float offsetImage = new Matrix3x3Float();
                Matrix3x3Float offsetScreen = new Matrix3x3Float();
                Matrix3x3Float scaledOffsetImage;
                Matrix3x3Float transform;

                scale.setAsScale(1 / (pProjected.getZ() / 3), 1 / (pProjected.getZ() / 3));
                offsetImage.setAsTranslate(- atom3D.getImage().getW() / 2.0f, - atom3D.getImage().getH() / 2.0f);
                scaledOffsetImage = MatrixOperations.multiply(offsetImage, scale);
                offsetScreen.setAsTranslate(pProjected.getX(), pProjected.getY());
                transform = MatrixOperations.multiply(scaledOffsetImage, offsetScreen);
                r.drawImage(atom3D.getImage(), transform);
            }
        } else {
            for ( int i = 1; i < pointsProjected.size(); i++ ) {
                r.drawLine(
                        (int)pointsProjected.get(0).getX(), (int)pointsProjected.get(0).getY(),
                        (int)pointsProjected.get(i).getX(), (int)pointsProjected.get(i).getY(),
                        0xffffffff);
            }
        }

        r.drawText(String.format("Atom position X: %.3f Y: %.3f Z: %.3f.",
                atom3D.getPoints3dFinal()[0].getX(),
                atom3D.getPoints3dFinal()[0].getY(),
                atom3D.getPoints3dFinal()[0].getZ()
        ), 10, 10, 0xffffffff);
        r.drawText(String.format("Atom radius: %.3f.", atom3D.getRadius()), 10, 40, 0xffffffff);
        r.drawText(String.format("Rotation X axis: %.3f degrees.", ((rotationX * Math.PI / 180) % 360)), 10, 70, 0xffffffff);
        r.drawText(String.format("Rotation Y axis: %.3f degrees.", ((rotationY * Math.PI / 180) % 360)), 10, 100, 0xffffffff);
        r.drawText(String.format("Rotation Z axis: %.3f degrees.", ((rotationZ * Math.PI / 180) % 360)), 10, 130, 0xffffffff);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        renderAtom(gc, r);
        renderAxis(gc, r);
        r.drawCircle((int)(mousePosIni.getX()), (int)(mousePosIni.getY()), 10, 0xffff0000);
        r.drawCircle((int)(mousePosFin.getX()), (int)(mousePosFin.getY()), 10, 0xffffff00);
        r.drawText("Difference between Mouse Star and Mouse Fin", 10, 160, 0xffffffff);
        float diffX = mousePosIni.getX() - mousePosFin.getX();
        float diffY = mousePosIni.getY() - mousePosFin.getY();
        r.drawText(String.format("X: %.3f Y: %.3f", diffX, diffY), 10, 190, 0xffffffff);
        //r.drawText(String.format("RotX angle: %.3f. RotY angle: %.3f", angleRotX, angleRotY), 10, 220, 0xffffffff);
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new TestAtom3D("Test atoms 3D"));
        gc.start();
    }

}
