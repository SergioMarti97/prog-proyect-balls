package engine.engine3d;

public class Triangle {

    private Vec3d[] p = new Vec3d[3];

    public Triangle() {

    }

    public Triangle(Vec3d[] p) {
        this.p = p;
    }

    public Vec3d[] getP() {
        return p;
    }

    public void setP(Vec3d[] p) {
        this.p = p;
    }

}
