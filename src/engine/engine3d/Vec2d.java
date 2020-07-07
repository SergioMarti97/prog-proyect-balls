package engine.engine3d;

public class Vec2d {

    private float x;

    private float y;

    private float z;

    public Vec2d() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }

    public Vec2d(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void set(Vec2d vec2d) {
        this.x = vec2d.getX();
        this.y = vec2d.getY();
        this.z = vec2d.getZ();
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

}
