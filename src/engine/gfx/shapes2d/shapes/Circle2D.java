package engine.gfx.shapes2d.shapes;

import engine.gfx.Renderer;
import engine.gfx.shapes2d.Shape2D;

public class Circle2D extends Shape2D {

    public Circle2D(float posX, float posY, float size, int color) {
        super(posX, posY, color);
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    @Override
    public void drawYourSelf(Renderer r) {
        switch (wayToRender) {
            case WIRE:
                r.drawCircle((int)(posX), (int)(posY), (int)(size), color);
                break;
            case SOLID:
                r.drawFillCircle((int)(posX), (int)(posY), (int)(size), color);
                break;
            case BLACKBOARD:
                r.drawCircle((int)(posX), (int)(posY), (int)(size), 0xffff0000);
                r.drawFillCircle((int)(posX), (int)(posY), 2, 0xff0000ff);
                r.drawLine((int)(posX), (int)(posY), (int)(posX + size), (int)(posY), 0xffffff00);
                break;
            case BLUEPRINT:
                r.drawCircle((int)(posX), (int)(posY), (int)(size), 0xffffffff);
                r.drawFillCircle((int)(posX), (int)(posY), 2, 0xffffffff);
                r.drawLine((int)(posX), (int)(posY), (int)(posX + size), (int)(posY), 0xffffffff);
                break;
        }
        super.drawYourSelf(r);
    }

    @Override
    public boolean isPointInside(float x, float y) {
        return Math.abs((posX - x) * (posX - x) + (posY - y) * (posY - y)) <= (size * size);
    }

}
