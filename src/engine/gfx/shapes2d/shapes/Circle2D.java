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
        switch (wayToRender) {
            case WIRE:
                r.drawCircle((int)(posX), (int)(posY), (int)(radius), color);
                break;
            case SOLID:
                r.drawFillCircle((int)(posX), (int)(posY), (int)(radius), color);
                break;
            case BLACKBOARD:
                r.drawCircle((int)(posX), (int)(posY), (int)(radius), 0xffff0000);
                r.drawCircle((int)(posX), (int)(posY), 2, 0xff0000ff);
                r.drawLine((int)(posX), (int)(posY), (int)(posX + radius), (int)(posY), 0xffffff00);
                break;
            case BLUEPRINT:
                r.drawCircle((int)(posX), (int)(posY), (int)(radius), 0xffffffff);
                r.drawCircle((int)(posX), (int)(posY), 2, 0xffffffff);
                r.drawLine((int)(posX), (int)(posY), (int)(posX + radius), (int)(posY), 0xffffffff);
                break;
        }
    }

    @Override
    public boolean isPointInside(float x, float y) {
        return Math.abs((posX - x) * (posX - x) + (posY - y) * (posY - y)) <= (radius * radius);
    }

}
