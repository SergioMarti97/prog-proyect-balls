package engine.engine3d;

public class Triangle {

    private Vec3d[] p = new Vec3d[3];

    private Vec2d[] t = new Vec2d[3];

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

    public Triangle(Vec3d[] p, Vec2d[] t) {
        this.p = p;
        this.t = t;
        this.color = 0xffffffff;
    }

    public Triangle(Vec3d[] p, Vec2d[] t, int color) {
        this.p = p;
        this.t = t;
        this.color = color;
    }

    public Vec3d[] getP() {
        return p;
    }

    public Vec2d[] getT() {
        return t;
    }

    public int getColor() {
        return color;
    }

    public void setP(Vec3d[] p) {
        this.p = p;
    }

    public void setT(Vec2d[] t) {
        this.t = t;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
