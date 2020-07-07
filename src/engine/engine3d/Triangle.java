package engine.engine3d;

public class Triangle {

    private Vec3d[] p = new Vec3d[3];

    private int color;

    public Triangle() {

    }

    public Triangle(Vec3d[] p) {
        this.p = p;
    }

    public Triangle(Vec3d[] p, int color) {
        this.p = p;
        this.color = color;
    }

    public Vec3d[] getP() {
        return p;
    }

    public int getColor() {
        return color;
    }

    public void setP(Vec3d[] p) {
        this.p = p;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
