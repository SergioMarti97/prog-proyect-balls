package engine.gfx.shapes2d.shapes;

import engine.gfx.Renderer;
import engine.gfx.shapes2d.Shape2D;

public class Polygon2D extends Shape2D {

    public Polygon2D(float posX, float posY, int color) {
        super(posX, posY, color);
    }

    @Override
    public void drawYourSelf(Renderer r) {

    }

    @Override
    public boolean isPointInside(float x, float y) {
        return false;
    }

}
