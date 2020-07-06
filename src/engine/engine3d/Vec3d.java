package engine.engine3d;

public class Vec3d {

    private float x;

    private float y;

    private float z;

    public Vec3d() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }

    public Vec3d(float x, float y, float z) {
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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void addToX(double amount) {
        this.x += amount;
    }

    public void addToY(double amount) {
        this.y += amount;
    }

    public void addToZ(double amount) {
        this.z += amount;
    }

    public void multiplyXBy(double amount) {
        this.x *= amount;
    }

    public void multiplyYBy(double amount) {
        this.y *= amount;
    }

    public void multiplyZBy(double amount) {
        this.z *= amount;
    }

}
