package engine.gfx.shapes2d.shapes;

import engine.gfx.Renderer;
import engine.gfx.shapes2d.Shape2D;

public class Circle2D extends Shape2D {

    protected float radius;

    public Circle2D(float posX, float posY, float radius, int color) {
        super(posX, posY, color);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void drawYourSelf(Renderer r) {
        r.drawCircle((int)(posX), (int)(posY), (int)(radius), color);
    }

    @Override
    public boolean isPointInside(float x, float y) {
        return Math.abs((posX - x) * (posX - x) + (posY - y) * (posY - y)) <= (radius * radius);
    }

}
