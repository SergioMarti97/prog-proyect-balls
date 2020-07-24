package engine.gfx.engine3d.normal;

public class Vec3d {

    private float x;

    private float y;

    private float z;

    private float w;

    public Vec3d() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
        w = 1.0f;
    }

    public Vec3d(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1.0f;
    }

    public Vec3d(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
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

    public float getW() {
        return w;
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

    public void setW(float w) {
        this.w = w;
    }

    public void set(Vec3d vec3d) {
        this.x = vec3d.getX();
        this.y = vec3d.getY();
        this.z = vec3d.getZ();
        this.w = vec3d.getW();
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1.0f;
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

    public void addToW(double amount) {
        this.w += amount;
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

    public void multiplyWBy(double amount) {
        this.w *= amount;
    }

    public float getLength() {
        return (float)(Math.sqrt( x * x + y * y + z * z ));
    }

    public void normalize() {
        float l = getLength();
        x /= l;
        y /= l;
        z /= l;
    }

    public float dotProduct(Vec3d v) {
        return this.getX() * v.getX() + this.getY() * v.getY() + this.getZ() * v.getZ();
    }

}
