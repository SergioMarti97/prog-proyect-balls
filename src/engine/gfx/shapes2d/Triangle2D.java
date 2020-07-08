package engine.gfx.shapes2d;

import engine.gfx.Renderer;
import engine.gfx.shapes2d.points2d.Point2DFloat;

public class Triangle2D extends Shape2D {

    private Point2DFloat[] p = new Point2DFloat[3];

    private boolean isSolid = false;

    public Triangle2D(float posX, float posY, Point2DFloat[] p, int color) {
        super(posX, posY, color);
        this.p = p;
    }

    public Triangle2D(float posX, float posY, float x0, float y0, float x1, float y1, float x2, float y2, int color) {
        super(posX, posY, color);
        for ( int i = 0; i < p.length; i++ ) {
            p[i] = new Point2DFloat();
        }
        p[0].setX(x0);
        p[0].setY(y0);
        p[1].setX(x1);
        p[1].setY(y1);
        p[2].setX(x2);
        p[2].setY(y2);
    }

    @Override
    public void drawYourSelf(Renderer r) {
        if ( isSolid ) {
            r.drawFillTriangle(
                    (p[0].getX()).intValue(), (p[0].getY()).intValue(),
                    (p[1].getX()).intValue(), (p[1].getY()).intValue(),
                    (p[2].getX()).intValue(), (p[2].getY()).intValue(),
                    color
            );
        } else {
            r.drawTriangle(
                    (p[0].getX()).intValue(), (p[0].getY()).intValue(),
                    (p[1].getX()).intValue(), (p[1].getY()).intValue(),
                    (p[2].getX()).intValue(), (p[2].getY()).intValue(),
                    color
            );
        }
        r.drawCircle((int)(posX), (int)(posY), 2, color);
    }

    public Point2DFloat[] getP() {
        return p;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public void setP(Point2DFloat[] p) {
        this.p = p;
    }

    public void setSolid(boolean solid) {
        isSolid = solid;
    }

}
