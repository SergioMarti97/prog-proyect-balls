package engine.gfx.engine3d.normal;

public class MatrixMath {

    public static Vec3d  matrixMultiplyVector(Mat4x4 m, Vec3d i) {
        Vec3d v = new Vec3d();
        v.setX(i.getX() * m.getM()[0][0] + i.getY() * m.getM()[1][0] + i.getZ() * m.getM()[2][0] + i.getW() * m.getM()[3][0]);
        v.setY(i.getX() * m.getM()[0][1] + i.getY() * m.getM()[1][1] + i.getZ() * m.getM()[2][1] + i.getW() * m.getM()[3][1]);
        v.setZ(i.getX() * m.getM()[0][2] + i.getY() * m.getM()[1][2] + i.getZ() * m.getM()[2][2] + i.getW() * m.getM()[3][2]);
        v.setW(i.getX() * m.getM()[0][3] + i.getY() * m.getM()[1][3] + i.getZ() * m.getM()[2][3] + i.getW() * m.getM()[3][3]);
        return v;
    }

    public static Mat4x4 matrixMultiplyMatrix(Mat4x4 m1, Mat4x4 m2) {
        Mat4x4 matrix = new Mat4x4();
        for ( int c = 0; c < 4; c++ ) {
            for ( int r = 0; r < 4; r++ ) {
                matrix.getM()[r][c] = m1.getM()[r][0] * m2.getM()[0][c] +
                                      m1.getM()[r][1] * m2.getM()[1][c] +
                                      m1.getM()[r][2] * m2.getM()[2][c] +
                                      m1.getM()[r][3] * m2.getM()[3][c];
            }
        }
        return matrix;
    }

    public static Mat4x4 matrixMakeIdentity() {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = 1.0f;
        matrix.getM()[1][1] = 1.0f;
        matrix.getM()[2][2] = 1.0f;
        matrix.getM()[3][3] = 1.0f;
        return matrix;
    }

    public static Mat4x4 matrixMakeRotationX(float angleRad) {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = 1.0f;
        matrix.getM()[1][1] = (float)(Math.cos(angleRad));
        matrix.getM()[1][2] = (float)(Math.sin(angleRad));
        matrix.getM()[2][1] = - (float)(Math.sin(angleRad));
        matrix.getM()[2][2] = (float)(Math.cos(angleRad));
        matrix.getM()[3][3] = 1.0f;
        return matrix;
    }

    public static Mat4x4 matrixMakeRotationY(float angleRad) {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = (float)(Math.cos(angleRad));
        matrix.getM()[0][2] = (float)(Math.sin(angleRad));
        matrix.getM()[2][0] = - (float)(Math.sin(angleRad));
        matrix.getM()[1][1] = 1.0f;
        matrix.getM()[2][2] = (float)(Math.cos(angleRad));
        matrix.getM()[3][3] = 1.0f;
        return matrix;
    }

    public static Mat4x4 matrixMakeRotationZ(float angleRad) {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = (float)(Math.cos(angleRad));
        matrix.getM()[0][1] = (float)(Math.sin(angleRad));
        matrix.getM()[1][0] = - (float)(Math.sin(angleRad));
        matrix.getM()[1][1] = (float)(Math.cos(angleRad));
        matrix.getM()[2][2] = 1.0f;
        matrix.getM()[3][3] = 1.0f;
        return matrix;
    }

    public static Mat4x4 matrixMakeRotation(Vec3d rotation) {
        Mat4x4 matRotX = MatrixMath.matrixMakeRotationX(rotation.getX());
        Mat4x4 matRotY = MatrixMath.matrixMakeRotationY(rotation.getY());
        Mat4x4 matRotZ = MatrixMath.matrixMakeRotationZ(rotation.getZ());
        Mat4x4 matRotXY = MatrixMath.matrixMultiplyMatrix(matRotX, matRotY);
        return MatrixMath.matrixMultiplyMatrix(matRotZ, matRotXY);
    }

    public static Mat4x4 matrixMakeScale(float x, float y, float z) {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = x;
        matrix.getM()[1][1] = y;
        matrix.getM()[2][2] = z;
        matrix.getM()[3][3] = 1.0f;
        return matrix;
    }

    public static Mat4x4 matrixMakeTranslation(float x, float y, float z) {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = 1.0f;
        matrix.getM()[1][1] = 1.0f;
        matrix.getM()[2][2] = 1.0f;
        matrix.getM()[3][3] = 1.0f;
        matrix.getM()[3][0] = x;
        matrix.getM()[3][1] = y;
        matrix.getM()[3][2] = z;
        return matrix;
    }

    public static Mat4x4 matrixMakeTranslation(Vec3d vec3d) {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = 1.0f;
        matrix.getM()[1][1] = 1.0f;
        matrix.getM()[2][2] = 1.0f;
        matrix.getM()[3][3] = 1.0f;
        matrix.getM()[3][0] = vec3d.getX();
        matrix.getM()[3][1] = vec3d.getY();
        matrix.getM()[3][2] = vec3d.getZ();
        return matrix;
    }

    public static Mat4x4 matrixMakeProjection(float fovDegrees, float aspectRatio, float near, float far) {
        float fovRad = (float) (1.0f / Math.tan(fovDegrees * 0.5f / 180.0f * 3.14159f));
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = aspectRatio * fovRad;
        matrix.getM()[1][1] = fovRad;
        matrix.getM()[2][2] = far / (far - near);
        matrix.getM()[3][2] = (-far * near) / (far - near);
        matrix.getM()[2][3] = 1.0f;
        matrix.getM()[3][3] = 0.0f;
        return matrix;
    }

    public static Mat4x4 matrixPointAt(Vec3d pos, Vec3d target, Vec3d up) {
        Vec3d newForward = vectorSub(target, pos);
        newForward = vectorNormalise(newForward);

        // Calculate new Up direction
        Vec3d a = vectorMul(newForward, vectorDotProduct(up, newForward));
        Vec3d newUp = vectorSub(up, a);
        newUp = vectorNormalise(newUp);

        // New Right direction is easy, its just cross product
        Vec3d newRight = vectorCrossProduct(newUp, newForward);

        // Construct Dimensioning and Translation Matrix
        Mat4x4 matrix = new Mat4x4();

        matrix.getM()[0][0] = newRight.getX();
        matrix.getM()[0][1] = newRight.getY();
        matrix.getM()[0][2] = newRight.getZ();
        matrix.getM()[0][3] = 0.0f;
        matrix.getM()[1][0] = newUp.getX();
        matrix.getM()[1][1] = newUp.getY();
        matrix.getM()[1][2] = newUp.getZ();
        matrix.getM()[1][3] = 0.0f;
        matrix.getM()[2][0] = newForward.getX();
        matrix.getM()[2][1] = newForward.getY();
        matrix.getM()[2][2] = newForward.getZ();
        matrix.getM()[2][3] = 0.0f;
        matrix.getM()[3][0] = pos.getX();
        matrix.getM()[3][1] = pos.getY();
        matrix.getM()[3][2] = pos.getZ();
        matrix.getM()[3][3] = 1.0f;

        return matrix;
    }

    public static Mat4x4 matrixQuickInverse(Mat4x4 m) {
        Mat4x4 matrix = new Mat4x4();

        matrix.getM()[0][0] = m.getM()[0][0];
        matrix.getM()[0][1] = m.getM()[1][0];
        matrix.getM()[0][2] = m.getM()[2][0];
        matrix.getM()[0][3] = 0.0f;
        matrix.getM()[1][0] = m.getM()[0][1];
        matrix.getM()[1][1] = m.getM()[1][1];
        matrix.getM()[1][2] = m.getM()[2][1];
        matrix.getM()[1][3] = 0.0f;
        matrix.getM()[2][0] = m.getM()[0][2];
        matrix.getM()[2][1] = m.getM()[1][2];
        matrix.getM()[2][2] = m.getM()[2][2];
        matrix.getM()[2][3] = 0.0f;
        matrix.getM()[3][0] = -(m.getM()[3][0] * matrix.getM()[0][0] + m.getM()[3][1] * matrix.getM()[1][0] + m.getM()[3][2] * matrix.getM()[2][0]);
        matrix.getM()[3][1] = -(m.getM()[3][0] * matrix.getM()[0][1] + m.getM()[3][1] * matrix.getM()[1][1] + m.getM()[3][2] * matrix.getM()[2][1]);
        matrix.getM()[3][2] = -(m.getM()[3][0] * matrix.getM()[0][2] + m.getM()[3][1] * matrix.getM()[1][2] + m.getM()[3][2] * matrix.getM()[2][2]);
        matrix.getM()[3][3] = 1.0f;

        return matrix;
    } // Only for Rotation/Translation Matrices

    public static Mat4x4 matrixInverse(Mat4x4 m) {
        double  det;

        Mat4x4 matInv = new Mat4x4();

        matInv.getM()[0][0] =  m.getM()[1][1] * m.getM()[2][2] * m.getM()[3][3] - m.getM()[1][1] * m.getM()[2][3] * m.getM()[3][2] - m.getM()[2][1] * m.getM()[1][2] * m.getM()[3][3] + m.getM()[2][1] * m.getM()[1][3] * m.getM()[3][2] + m.getM()[3][1] * m.getM()[1][2] * m.getM()[2][3] - m.getM()[3][1] * m.getM()[1][3] * m.getM()[2][2];
        matInv.getM()[1][0] = -m.getM()[1][0] * m.getM()[2][2] * m.getM()[3][3] + m.getM()[1][0] * m.getM()[2][3] * m.getM()[3][2] + m.getM()[2][0] * m.getM()[1][2] * m.getM()[3][3] - m.getM()[2][0] * m.getM()[1][3] * m.getM()[3][2] - m.getM()[3][0] * m.getM()[1][2] * m.getM()[2][3] + m.getM()[3][0] * m.getM()[1][3] * m.getM()[2][2];
        matInv.getM()[2][0] =  m.getM()[1][0] * m.getM()[2][1] * m.getM()[3][3] - m.getM()[1][0] * m.getM()[2][3] * m.getM()[3][1] - m.getM()[2][0] * m.getM()[1][1] * m.getM()[3][3] + m.getM()[2][0] * m.getM()[1][3] * m.getM()[3][1] + m.getM()[3][0] * m.getM()[1][1] * m.getM()[2][3] - m.getM()[3][0] * m.getM()[1][3] * m.getM()[2][1];
        matInv.getM()[3][0] = -m.getM()[1][0] * m.getM()[2][1] * m.getM()[3][2] + m.getM()[1][0] * m.getM()[2][2] * m.getM()[3][1] + m.getM()[2][0] * m.getM()[1][1] * m.getM()[3][2] - m.getM()[2][0] * m.getM()[1][2] * m.getM()[3][1] - m.getM()[3][0] * m.getM()[1][1] * m.getM()[2][2] + m.getM()[3][0] * m.getM()[1][2] * m.getM()[2][1];
        matInv.getM()[0][1] = -m.getM()[0][1] * m.getM()[2][2] * m.getM()[3][3] + m.getM()[0][1] * m.getM()[2][3] * m.getM()[3][2] + m.getM()[2][1] * m.getM()[0][2] * m.getM()[3][3] - m.getM()[2][1] * m.getM()[0][3] * m.getM()[3][2] - m.getM()[3][1] * m.getM()[0][2] * m.getM()[2][3] + m.getM()[3][1] * m.getM()[0][3] * m.getM()[2][2];
        matInv.getM()[1][1] =  m.getM()[0][0] * m.getM()[2][2] * m.getM()[3][3] - m.getM()[0][0] * m.getM()[2][3] * m.getM()[3][2] - m.getM()[2][0] * m.getM()[0][2] * m.getM()[3][3] + m.getM()[2][0] * m.getM()[0][3] * m.getM()[3][2] + m.getM()[3][0] * m.getM()[0][2] * m.getM()[2][3] - m.getM()[3][0] * m.getM()[0][3] * m.getM()[2][2];
        matInv.getM()[2][1] = -m.getM()[0][0] * m.getM()[2][1] * m.getM()[3][3] + m.getM()[0][0] * m.getM()[2][3] * m.getM()[3][1] + m.getM()[2][0] * m.getM()[0][1] * m.getM()[3][3] - m.getM()[2][0] * m.getM()[0][3] * m.getM()[3][1] - m.getM()[3][0] * m.getM()[0][1] * m.getM()[2][3] + m.getM()[3][0] * m.getM()[0][3] * m.getM()[2][1];
        matInv.getM()[3][1] =  m.getM()[0][0] * m.getM()[2][1] * m.getM()[3][2] - m.getM()[0][0] * m.getM()[2][2] * m.getM()[3][1] - m.getM()[2][0] * m.getM()[0][1] * m.getM()[3][2] + m.getM()[2][0] * m.getM()[0][2] * m.getM()[3][1] + m.getM()[3][0] * m.getM()[0][1] * m.getM()[2][2] - m.getM()[3][0] * m.getM()[0][2] * m.getM()[2][1];
        matInv.getM()[0][2] =  m.getM()[0][1] * m.getM()[1][2] * m.getM()[3][3] - m.getM()[0][1] * m.getM()[1][3] * m.getM()[3][2] - m.getM()[1][1] * m.getM()[0][2] * m.getM()[3][3] + m.getM()[1][1] * m.getM()[0][3] * m.getM()[3][2] + m.getM()[3][1] * m.getM()[0][2] * m.getM()[1][3] - m.getM()[3][1] * m.getM()[0][3] * m.getM()[1][2];
        matInv.getM()[1][2] = -m.getM()[0][0] * m.getM()[1][2] * m.getM()[3][3] + m.getM()[0][0] * m.getM()[1][3] * m.getM()[3][2] + m.getM()[1][0] * m.getM()[0][2] * m.getM()[3][3] - m.getM()[1][0] * m.getM()[0][3] * m.getM()[3][2] - m.getM()[3][0] * m.getM()[0][2] * m.getM()[1][3] + m.getM()[3][0] * m.getM()[0][3] * m.getM()[1][2];
        matInv.getM()[2][2] =  m.getM()[0][0] * m.getM()[1][1] * m.getM()[3][3] - m.getM()[0][0] * m.getM()[1][3] * m.getM()[3][1] - m.getM()[1][0] * m.getM()[0][1] * m.getM()[3][3] + m.getM()[1][0] * m.getM()[0][3] * m.getM()[3][1] + m.getM()[3][0] * m.getM()[0][1] * m.getM()[1][3] - m.getM()[3][0] * m.getM()[0][3] * m.getM()[1][1];
        matInv.getM()[3][2] = -m.getM()[0][0] * m.getM()[1][1] * m.getM()[3][2] + m.getM()[0][0] * m.getM()[1][2] * m.getM()[3][1] + m.getM()[1][0] * m.getM()[0][1] * m.getM()[3][2] - m.getM()[1][0] * m.getM()[0][2] * m.getM()[3][1] - m.getM()[3][0] * m.getM()[0][1] * m.getM()[1][2] + m.getM()[3][0] * m.getM()[0][2] * m.getM()[1][1];
        matInv.getM()[0][3] = -m.getM()[0][1] * m.getM()[1][2] * m.getM()[2][3] + m.getM()[0][1] * m.getM()[1][3] * m.getM()[2][2] + m.getM()[1][1] * m.getM()[0][2] * m.getM()[2][3] - m.getM()[1][1] * m.getM()[0][3] * m.getM()[2][2] - m.getM()[2][1] * m.getM()[0][2] * m.getM()[1][3] + m.getM()[2][1] * m.getM()[0][3] * m.getM()[1][2];
        matInv.getM()[1][3] =  m.getM()[0][0] * m.getM()[1][2] * m.getM()[2][3] - m.getM()[0][0] * m.getM()[1][3] * m.getM()[2][2] - m.getM()[1][0] * m.getM()[0][2] * m.getM()[2][3] + m.getM()[1][0] * m.getM()[0][3] * m.getM()[2][2] + m.getM()[2][0] * m.getM()[0][2] * m.getM()[1][3] - m.getM()[2][0] * m.getM()[0][3] * m.getM()[1][2];
        matInv.getM()[2][3] = -m.getM()[0][0] * m.getM()[1][1] * m.getM()[2][3] + m.getM()[0][0] * m.getM()[1][3] * m.getM()[2][1] + m.getM()[1][0] * m.getM()[0][1] * m.getM()[2][3] - m.getM()[1][0] * m.getM()[0][3] * m.getM()[2][1] - m.getM()[2][0] * m.getM()[0][1] * m.getM()[1][3] + m.getM()[2][0] * m.getM()[0][3] * m.getM()[1][1];
        matInv.getM()[3][3] =  m.getM()[0][0] * m.getM()[1][1] * m.getM()[2][2] - m.getM()[0][0] * m.getM()[1][2] * m.getM()[2][1] - m.getM()[1][0] * m.getM()[0][1] * m.getM()[2][2] + m.getM()[1][0] * m.getM()[0][2] * m.getM()[2][1] + m.getM()[2][0] * m.getM()[0][1] * m.getM()[1][2] - m.getM()[2][0] * m.getM()[0][2] * m.getM()[1][1];

        det = m.getM()[0][0] * matInv.getM()[0][0] + m.getM()[0][1] * matInv.getM()[1][0] + m.getM()[0][2] * matInv.getM()[2][0] + m.getM()[0][3] * matInv.getM()[3][0];
        //	if (det == 0) return false;

        det = 1.0 / det;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matInv.getM()[i][j] *= (float) det;
            }
        }

        return matInv;
    }

    public static Vec3d vectorAdd(Vec3d v1, Vec3d v2) {
        return new Vec3d(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    public static Vec3d vectorSub(Vec3d v1, Vec3d v2) {
        return new Vec3d(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
    }

    public static Vec3d vectorMul(Vec3d v1, float k) {
        return new Vec3d(v1.getX() * k, v1.getY() * k, v1.getZ() * k);
    }

    public static Vec3d vectorDiv(Vec3d v1, float k) {
        return new Vec3d(v1.getX() / k, v1.getY() / k, v1.getZ() / k);
    }

    public static float vectorDotProduct(Vec3d v1, Vec3d v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

    public static float vectorLength(Vec3d v) {
        return (float) Math.sqrt(vectorDotProduct(v, v));
    }

    public static Vec3d vectorNormalise(Vec3d v) {
        float l = vectorLength(v);
        return new Vec3d(v.getX() / l, v.getY() / l, v.getZ() / l );
    }

    public static Vec3d vectorCrossProduct(Vec3d v1, Vec3d v2) {
        Vec3d v = new Vec3d();
        v.setX(v1.getY() * v2.getZ() - v1.getZ() * v2.getY());
        v.setY(v1.getZ() * v2.getX() - v1.getX() * v2.getZ());
        v.setZ(v1.getX() * v2.getY() - v1.getY() * v2.getX());
        return v;
    }

    public static Vec3d vectorIntersectPlane(Vec3d plane_p, Vec3d plane_n, Vec3d lineStart, Vec3d lineEnd, float t) {
        plane_n = vectorNormalise(plane_n);
        float plane_d = -vectorDotProduct(plane_n, plane_p);
        float ad = vectorDotProduct(lineStart, plane_n);
        float bd = vectorDotProduct(lineEnd, plane_n);
        t = (-plane_d - ad) / (bd - ad);
        Vec3d lineStartToEnd = vectorSub(lineEnd, lineStart);
        Vec3d lineToIntersect = vectorMul(lineStartToEnd, t);
        return vectorAdd(lineStart, lineToIntersect);
    }

    private static float dist(Vec3d p, Vec3d plane_p, Vec3d plane_n) {
        // Return signed shortest distance from point to plane, plane normal must be normalised
        Vec3d n = vectorNormalise(p);
        return (plane_n.getX() * p.getX() + plane_n.getY() * p.getY() + plane_n.getZ() * p.getZ() - vectorDotProduct(plane_n, plane_p));
    };

    public static int triangleClipAgainstPlane(Vec3d plane_p, Vec3d plane_n, Triangle in_tri, Triangle out_tri1, Triangle out_tri2) {
        // Make sure plane normal is indeed normal
        plane_n = vectorNormalise(plane_n);

        out_tri1.getT()[0].set(in_tri.getT()[0]);
        out_tri2.getT()[0].set(in_tri.getT()[0]);
        out_tri1.getT()[1].set(in_tri.getT()[1]);
        out_tri2.getT()[1].set(in_tri.getT()[1]);
        out_tri1.getT()[2].set(in_tri.getT()[2]);
        out_tri2.getT()[2].set(in_tri.getT()[2]);

        // Create two temporary storage arrays to classify points either side of plane
        // If distance sign is positive, point lies on "inside" of plane
        Vec3d[] inside_points = new Vec3d[3];
        int nInsidePointCount = 0;
        Vec3d[] outside_points = new Vec3d[3];
        int nOutsidePointCount = 0;
        Vec2d[] inside_tex = new Vec2d[3];
        int nInsideTexCount = 0;
        Vec2d[] outside_tex = new Vec2d[3];
        int nOutsideTexCount = 0;

        // Get signed distance of each point in triangle to plane
        float d0 = dist(in_tri.getP()[0], plane_p, plane_n);
        float d1 = dist(in_tri.getP()[1], plane_p, plane_n);
        float d2 = dist(in_tri.getP()[2], plane_p, plane_n);

        if (d0 >= 0) { inside_points[nInsidePointCount++] = in_tri.getP()[0]; inside_tex[nInsideTexCount++] = in_tri.getT()[0]; }
        else {
            outside_points[nOutsidePointCount++] = in_tri.getP()[0]; outside_tex[nOutsideTexCount++] = in_tri.getT()[0];
        }
        if (d1 >= 0) {
            inside_points[nInsidePointCount++] = in_tri.getP()[1]; inside_tex[nInsideTexCount++] = in_tri.getT()[1];
        }
        else {
            outside_points[nOutsidePointCount++] = in_tri.getP()[1];  outside_tex[nOutsideTexCount++] = in_tri.getT()[1];
        }
        if (d2 >= 0) {
            inside_points[nInsidePointCount++] = in_tri.getP()[2]; inside_tex[nInsideTexCount++] = in_tri.getT()[2];
        }
        else {
            outside_points[nOutsidePointCount++] = in_tri.getP()[2];  outside_tex[nOutsideTexCount++] = in_tri.getT()[2];
        }

        // Now classify triangle points, and break the input triangle into
        // smaller output triangles if required. There are four possible
        // outcomes...

        if (nInsidePointCount == 0)
        {
            // All points lie on the outside of plane, so clip whole triangle
            // It ceases to exist

            return 0; // No returned triangles are valid
        }

        if ( nInsidePointCount == 3 )
        {
            // All points lie on the inside of plane, so do nothing
            // and allow the triangle to simply pass through
            out_tri1 = in_tri;

            return 1; // Just the one returned original triangle is valid
        }

        if ( nInsidePointCount == 1 && nOutsidePointCount == 2 )
        {
            // Triangle should be clipped. As two points lie outside
            // the plane, the triangle simply becomes a smaller triangle

            // Copy appearance info to new triangle
            out_tri1.setColor(0xffff00ff);// in_tri.getColor;

            // The inside point is valid, so keep that...
            out_tri1.getP()[0] = inside_points[0];
            out_tri1.getT()[0] = inside_tex[0];

            // but the two new points are at the locations where the
            // original sides of the triangle (lines) intersect with the plane
            float t = 0.0f;
            out_tri1.getP()[1] = vectorIntersectPlane(plane_p, plane_n, inside_points[0], outside_points[0], t);
            out_tri1.getT()[1].setX(t * (outside_tex[0].getX() - inside_tex[0].getX()) + inside_tex[0].getX());
            out_tri1.getT()[1].setY(t * (outside_tex[0].getY() - inside_tex[0].getY()) + inside_tex[0].getY());
            out_tri1.getT()[1].setZ(t * (outside_tex[0].getZ() - inside_tex[0].getZ()) + inside_tex[0].getZ());

            out_tri1.getP()[2] = vectorIntersectPlane(plane_p, plane_n, inside_points[0], outside_points[1], t);
            out_tri1.getT()[2].setX(t * (outside_tex[1].getX() - inside_tex[0].getX()) + inside_tex[0].getX());
            out_tri1.getT()[2].setY(t * (outside_tex[1].getY() - inside_tex[0].getY()) + inside_tex[0].getY());
            out_tri1.getT()[2].setZ(t * (outside_tex[1].getZ() - inside_tex[0].getZ()) + inside_tex[0].getZ());

            return 1; // Return the newly formed single triangle
        }

        if ( nInsidePointCount == 2 && nOutsidePointCount == 1 ) {
            // Triangle should be clipped. As two points lie inside the plane,
            // the clipped triangle becomes a "quad". Fortunately, we can
            // represent a quad with two new triangles

            // Copy appearance info to new triangles
            out_tri1.setColor(0xff00ff00);// in_tri.getColor();
            out_tri2.setColor(0xffff0000);// in_tri.getColor();

            // The first triangle consists of the two inside points and a new
            // point determined by the location where one side of the triangle
            // intersects with the plane
            out_tri1.getP()[0] = inside_points[0];
            out_tri1.getT()[0] = inside_tex[0];

            out_tri1.getP()[1] = inside_points[1];
            out_tri1.getT()[1] = inside_tex[1];

            float t = 0.0f;
            out_tri1.getP()[2] = vectorIntersectPlane(plane_p, plane_n, inside_points[0], outside_points[0], t);
            out_tri1.getT()[2].setX(t * (outside_tex[0].getX() - inside_tex[0].getX()) + inside_tex[0].getX());
            out_tri1.getT()[2].setY(t * (outside_tex[0].getY() - inside_tex[0].getY()) + inside_tex[0].getY());
            out_tri1.getT()[2].setZ(t * (outside_tex[0].getZ() - inside_tex[0].getZ()) + inside_tex[0].getZ());

            // The second triangle is composed of one of he inside points, a
            // new point determined by the intersection of the other side of the
            // triangle and the plane, and the newly created point above
            out_tri2.getP()[1] = inside_points[1];
            out_tri2.getT()[1] = inside_tex[1];
            out_tri2.getP()[0] = out_tri1.getP()[2];
            out_tri2.getT()[0] = out_tri1.getT()[2];
            out_tri2.getP()[2] = vectorIntersectPlane(plane_p, plane_n, inside_points[1], outside_points[0], t);
            out_tri2.getT()[2].setX(t * (outside_tex[0].getX() - inside_tex[1].getX()) + inside_tex[1].getX());
            out_tri2.getT()[2].setY(t * (outside_tex[0].getY() - inside_tex[1].getY()) + inside_tex[1].getY());
            out_tri2.getT()[2].setZ(t * (outside_tex[0].getZ() - inside_tex[1].getZ()) + inside_tex[1].getZ());
            return 2; // Return two newly formed triangles which form a quad
        }

        return 0;
    }

}
