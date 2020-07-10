package engine.gfx.shapes2d.points2d;

import engine.gfx.shapes2d.QuickMath;

public class Vec2DFloat extends Vec2D<Float> {

    public Vec2DFloat() {
        x = 0.0f;
        y = 0.0f;
    }

    public Vec2DFloat(Float x, Float y) {
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
    public void add(Vec2D<Float> point) {
        x += point.getX();
        y += point.getY();
    }

    public void sub(Vec2D<Float> point) {
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

    public void multiply(Vec2D<Float> point) {
        x *= point.getX();
        y *= point.getY();
    }

    public Float mag() {
        return (float)Math.sqrt(x * x + y * y);
    }

    public Float mag2() {
        return x * x + y * y;
    }

    public Vec2D<Float> normal() {
        float r = 1 / mag();
        return new Vec2DFloat( x * r, y * r);
    }

    public Vec2D<Float> perpendicular() {
        return new Vec2DFloat(-y, x);
    }

    public Float dotProduct(Vec2D<Float> point) {
        return x * point.getX() + y * point.getY();
    }

    public Float crossProduct(Vec2D<Float> point) {
        return x * point.getY() - y * point.getX();
    }

    public void translateThisAngle(float angle) {
        angle *= (Math.PI / 180.0f);
        float x = (float)((this.x * Math.cos(angle)) - (this.y * Math.sin(angle)));
        float y = (float)((this.x * Math.sin(angle)) + (this.y * Math.cos(angle)));
        this.x = x;
        this.y = y;
    }

}
