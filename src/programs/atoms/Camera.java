package programs.atoms;

import engine.gfx.engine3d.normal.Mat4x4;
import engine.gfx.engine3d.normal.MatrixMath;
import engine.gfx.engine3d.normal.Vec3d;

public class Camera {

    private Mat4x4 matView;

    private Mat4x4 matCamera;

    private Vec3d camera;

    private Vec3d lookDirection;

    private Vec3d up = new Vec3d(0.0f, 1.0f, 0.0f);

    private Vec3d target = new Vec3d(0.0f, 0.0f, 1.0f);

    private Vec3d cameraRot = new Vec3d(0.0f, 0.0f, 0.0f);

    public Camera() {
        camera = new Vec3d();
        lookDirection = new Vec3d();
        matCamera = calculateMatCamera(MatrixMath.matrixMakeIdentity()); // todo punto susceptible de que este mal.
        matView = MatrixMath.matrixQuickInverse(matCamera);
    }

    public Mat4x4 calculateMatCamera(Mat4x4 transform) {
        lookDirection = MatrixMath.matrixMultiplyVector(transform, target);
        target = MatrixMath.vectorAdd(camera, lookDirection);
        return MatrixMath.matrixPointAt(camera, target, up);
    }

    public Mat4x4 getMatView() {
        matView = MatrixMath.matrixQuickInverse(matCamera);
        return matView;
    }

    public void rotX(float angleRad) {
        float rotX = cameraRot.getX();
        rotX += angleRad;
        cameraRot.setX(rotX);
        matCamera = calculateMatCamera(MatrixMath.matrixMakeRotationX(cameraRot.getX()));
    }

    public void rotY(float angleRad) {
        float rotY = cameraRot.getY();
        rotY += angleRad;
        cameraRot.setY(rotY);
        matCamera = calculateMatCamera(MatrixMath.matrixMakeRotationY(cameraRot.getY()));
    }

    public void rotZ(float angleRad) {
        float rotZ = cameraRot.getZ();
        rotZ += angleRad;
        cameraRot.setZ(rotZ);
        matCamera = calculateMatCamera(MatrixMath.matrixMakeRotationZ(cameraRot.getZ()));
    }

}
