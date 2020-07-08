package engine.gfx.shapes2d.points2d;

import engine.gfx.Renderer;
import engine.gfx.shapes2d.Drawable;

public abstract class Point2D<T> implements Drawable {

    protected final int color = 0xffffffff;

    protected final int radius = 2;

    protected T x;

    protected T y;

    public Point2D() {

    }

    public Point2D(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }

    public void setX(T x) {
        this.x = x;
    }

    public void set(Point2D<T> point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    public abstract void addToX(T amount);

    public abstract void addToY(T amount);

    public abstract void add(T amount);

    public abstract void add(Point2D<T> point);

    public abstract void multiplyXBy(T amount);

    public abstract void multiplyYBy(T amount);

    public abstract void multiply(T amount);

    public abstract void multiply(Point2D<T> point);

    public abstract T mag();

    public abstract T mag2();

    public abstract Point2D<T> normal();

    public abstract Point2D<T> perpendicular();

    public abstract T dotProduct(Point2D<T> point);

    public abstract T crossProduct(Point2D<T> point);

    public void drawYourSelf(Renderer r) {
        r.drawCircle((int)(x), (int)(y), radius, color);
    }

}
