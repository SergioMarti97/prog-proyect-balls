package engine.gfx.shapes2d.points2d;

public class Vec2DInteger extends Vec2D<Integer> {

    public Vec2DInteger() {
        x = 0;
        y = 0;
    }

    public Vec2DInteger(Integer x, Integer y) {
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
    public void add(Vec2D<Integer> point) {
        x += point.getX();
        y += point.getY();
    }

    public void sub(Vec2D<Integer> point) {
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
    public void multiply(Vec2D<Integer> point) {
        x *= point.getX();
        y *= point.getY();
    }

    public void division(Vec2D<Integer> point) {
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
    public Vec2D<Integer> normal() {
        int r = 1 / mag();
        return new Vec2DInteger( x * r, y * r);
    }

    @Override
    public Vec2D<Integer> perpendicular() {
        return new Vec2DInteger(-y, x);
    }

    @Override
    public Integer dotProduct(Vec2D<Integer> point) {
        return x * point.getX() + y * point.getY();
    }

    @Override
    public Integer crossProduct(Vec2D<Integer> point) {
        return x * point.getY() - y * point.getX();
    }

}
