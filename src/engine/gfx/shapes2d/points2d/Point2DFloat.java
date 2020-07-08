package engine.gfx.shapes2d.points2d;

public class Point2DFloat extends Point2D<Float> {

    public Point2DFloat() {
        x = 0.0f;
        y = 0.0f;
    }

    public Point2DFloat(Float x, Float y) {
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
    public void add(Point2D<Float> point) {
        x += point.getX();
        y += point.getY();
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

    public void multiply(Point2D<Float> point) {
        x *= point.getX();
        y *= point.getY();
    }

    public Float mag() {
        return (float)Math.sqrt(x * x + y * y);
    }

    public Float mag2() {
        return x * x + y * y;
    }

    public Point2D<Float> normal() {
        float r = 1 / mag();
        return new Point2DFloat( x * r, y * r);
    }

    public Point2D<Float> perpendicular() {
        return new Point2DFloat(-y, x);
    }

    public Float dotProduct(Point2D<Float> point) {
        return x * point.getX() + y * point.getY();
    }

    public Float crossProduct(Point2D<Float> point) {
        return x * point.getY() - y * point.getX();
    }

}
