package programs.atoms;

import engine.gfx.engine3d.normal.Mat4x4;
import engine.gfx.engine3d.normal.MatrixMath;
import engine.gfx.engine3d.normal.Vec3d;

public class Camera {

    private Mat4x4 matView;

    private Mat4x4 matCamera;

    private Vec3d origin;

    private Vec3d lookDirection;

    private Vec3d up = new Vec3d(0.0f, 1.0f, 0.0f);

    private Vec3d target = new Vec3d(0.0f, 0.0f, 1.0f);

    private Vec3d cameraRot = new Vec3d(0.0f, 0.0f, 0.0f);

    public Camera() {
        origin = new Vec3d();
        lookDirection = new Vec3d();
        matCamera = calculateMatCamera(up, target, MatrixMath.matrixMakeIdentity());
        matView = MatrixMath.matrixQuickInverse(matCamera);
    }

    public Mat4x4 calculateMatCamera(Vec3d up, Vec3d target, Mat4x4 transform) {
        lookDirection = MatrixMath.matrixMultiplyVector(transform, target);
        target = MatrixMath.vectorAdd(origin, lookDirection);
        return MatrixMath.matrixPointAt(origin, target, up);
    }

    public Mat4x4 getMatView() {
        Mat4x4 matCameraRotX = MatrixMath.matrixMakeRotationX(cameraRot.getX());
        Mat4x4 matCameraRotY = MatrixMath.matrixMakeRotationY(cameraRot.getY());
        Mat4x4 matCameraRotZ = MatrixMath.matrixMakeRotationZ(cameraRot.getZ());
        Mat4x4 matCameraRotXY = MatrixMath.matrixMultiplyMatrix(matCameraRotX, matCameraRotY);
        Mat4x4 matCameraRot = MatrixMath.matrixMultiplyMatrix(matCameraRotXY, matCameraRotZ);
        matCamera = calculateMatCamera(up, target, matCameraRot);
        matView = MatrixMath.matrixQuickInverse(matCamera);
        return matView;
    }

    public void rotX(float angleRad) {
        float rotX = cameraRot.getX();
        rotX += angleRad;
        cameraRot.setX(rotX);
    }

    public void rotY(float angleRad) {
        float rotY = cameraRot.getY();
        rotY += angleRad;
        cameraRot.setY(rotY);
    }

    public void rotZ(float angleRad) {
        float rotZ = cameraRot.getZ();
        rotZ += angleRad;
        cameraRot.setZ(rotZ);
    }

    public void setOrigin(Vec3d origin) {
        this.origin = origin;
    }

    public Vec3d getOrigin() {
        return origin;
    }

    public Vec3d getCameraRot() {
        return cameraRot;
    }

    public Vec3d getUp() {
        return up;
    }

    public Vec3d getTarget() {
        return target;
    }

    public Vec3d getLookDirection() {
        return lookDirection;
    }

}
