package engine.gfx.shapes2d;

import engine.gfx.Renderer;

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

}
