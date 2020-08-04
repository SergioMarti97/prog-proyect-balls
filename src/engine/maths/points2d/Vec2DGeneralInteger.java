package engine.maths.points2d;

public class Vec2DGeneralInteger extends Vec2dGeneral<Integer> {

    public Vec2DGeneralInteger() {
        x = 0;
        y = 0;
    }

    public Vec2DGeneralInteger(Integer x, Integer y) {
        super(x, y);
    }

    @Override
    public void addToX(Integer amount) {
        x += amount;
    }

    @Override
    public void addToY(Integer amount) {
        y += amount;
    }

    @Override
    public void add(Integer amount) {
        x += amount;
        y += amount;
    }

    @Override
    public void add(Vec2dGeneral<Integer> point) {
        x += point.getX();
        y += point.getY();
    }

    public void sub(Vec2dGeneral<Integer> point) {
        x -= point.getX();
        y -= point.getY();
    }

    @Override
    public void multiplyXBy(Integer amount) {
        x *= amount;
    }

    @Override
    public void multiplyYBy(Integer amount) {
        y *= amount;
    }

    @Override
    public void multiply(Integer amount) {
        x *= amount;
        y *= amount;
    }

    @Override
    public void multiply(Vec2dGeneral<Integer> point) {
        x *= point.getX();
        y *= point.getY();
    }

    public void division(Vec2dGeneral<Integer> point) {
        x /= point.getX();
        y /= point.getY();
    }

    @Override
    public Integer mag() {
        return (int)(Math.sqrt(x * x + y * y));
    }

    @Override
    public Integer mag2() {
        return x * x + y * y;
    }

    @Override
    public Vec2dGeneral<Integer> normal() {
        int r = 1 / mag();
        return new Vec2DGeneralInteger( x * r, y * r);
    }

    @Override
    public Vec2dGeneral<Integer> perpendicular() {
        return new Vec2DGeneralInteger(-y, x);
    }

    @Override
    public Integer dotProduct(Vec2dGeneral<Integer> point) {
        return x * point.getX() + y * point.getY();
    }

    @Override
    public Integer crossProduct(Vec2dGeneral<Integer> point) {
        return x * point.getY() - y * point.getX();
    }

}
