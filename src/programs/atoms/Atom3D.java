package programs.atoms;

import engine.gfx.engine3d.normal.Mat4x4;
import engine.gfx.engine3d.normal.MatrixMath;
import engine.gfx.engine3d.normal.Vec3d;

public class Atom3D {

    private final int MAX_POINTS = 5;

    private int numPoints;

    private Vec3d center;

    private Vec3d[] points3d;

    public Atom3D(Vec3d center) {
        this.center = center;
        points3d = new Vec3d[MAX_POINTS];
        for ( int i = 0; i < points3d.length; i++ ) {
            points3d[i] = new Vec3d();
        }
        points3d[0] = center;
        Vec3d point = new Vec3d(center.getX(), center.getY(), center.getZ());
        point.addToY(1);
        points3d[1] = point;
        Mat4x4 matrixRotateZ = MatrixMath.matrixMakeRotationZ((float)(120.0f * Math.PI / 180.0f));
        points3d[2] = MatrixMath.matrixMultiplyVector(matrixRotateZ, point);
        Mat4x4 matrixRotateY = MatrixMath.matrixMakeRotationY((float)(120.0f * Math.PI / 180.0f));
        Mat4x4 matrixRotateZY = MatrixMath.matrixMultiplyMatrix(matrixRotateZ, matrixRotateY);
        points3d[3] = MatrixMath.matrixMultiplyVector(matrixRotateZY, point);
        matrixRotateY = MatrixMath.matrixMakeRotationY((float)(240.0f * Math.PI / 180.0f));
        matrixRotateZY = MatrixMath.matrixMultiplyMatrix(matrixRotateZ, matrixRotateY);
        points3d[4] = MatrixMath.matrixMultiplyVector(matrixRotateZY, point);
    }

    public Vec3d[] getPoints3d() {
        return points3d;
    }

}
