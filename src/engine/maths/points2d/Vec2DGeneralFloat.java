package engine.maths.points2d;

public class Vec2DGeneralFloat extends Vec2dGeneral<Float> {

    public Vec2DGeneralFloat() {
        x = 0.0f;
        y = 0.0f;
    }

    public Vec2DGeneralFloat(Float x, Float y) {
        super(x, y);
    }

    @Override
    public void addToX(Float amount) {
        x += amount;
    }

    @Override
    public void addToY(Float amount) {
        y += amount;
    }

    @Override
    public void add(Float amount) {
        x += amount;
        y += amount;
    }

    @Override
    public void add(Vec2dGeneral<Float> point) {
        x += point.getX();
        y += point.getY();
    }

    public void sub(Vec2dGeneral<Float> point) {
        x -= point.getX();
        y -= point.getY();
    }

    public void multiplyXBy(Float amount) {
        x *= amount;
    }

    public void multiplyYBy(Float amount) {
        y *= amount;
    }

    public void multiply(Float amount) {
        x *= amount;
        y *= amount;
    }

    public void multiply(Vec2dGeneral<Float> point) {
        x *= point.getX();
        y *= point.getY();
    }

    public void division(Vec2dGeneral<Float> point) {
        x /= point.getX();
        y /= point.getY();
    }

    public Float mag() {
        return (float)Math.sqrt(x * x + y * y);
    }

    public Float mag2() {
        return x * x + y * y;
    }

    public Vec2dGeneral<Float> normal() {
        float r = 1 / mag();
        return new Vec2DGeneralFloat( x * r, y * r);
    }

    public Vec2dGeneral<Float> perpendicular() {
        return new Vec2DGeneralFloat(-y, x);
    }

    public Float dotProduct(Vec2dGeneral<Float> point) {
        return x * point.getX() + y * point.getY();
    }

    public Float crossProduct(Vec2dGeneral<Float> point) {
        return x * point.getY() - y * point.getX();
    }

    public void translateThisAngle(float angle) {
        angle *= (Math.PI / 180.0f);
        float x = (float)((this.x * Math.cos(angle)) - (this.y * Math.sin(angle)));
        float y = (float)((this.x * Math.sin(angle)) + (this.y * Math.cos(angle)));
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj instanceof Vec2DGeneralFloat) {
            Vec2DGeneralFloat vec2DFloat = (Vec2DGeneralFloat)(obj);
            return this.x.equals(vec2DFloat.getX()) && this.y.equals(vec2DFloat.getY());
        } else {
            return false;
        }
    }

}
