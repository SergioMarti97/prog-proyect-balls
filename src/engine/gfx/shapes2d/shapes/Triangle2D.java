package engine.gfx.shapes2d.shapes;

import engine.gfx.Renderer;
import engine.gfx.shapes2d.Shape2D;
import engine.gfx.shapes2d.points2d.Point2DFloat;

public class Triangle2D extends Shape2D {

    private final int numVertex = 3;

    private Point2DFloat[] pShape = new Point2DFloat[numVertex];

    private Point2DFloat[] pFinal = new Point2DFloat[numVertex];

    private float size = 10.0f;

    public Triangle2D(float posX, float posY, Point2DFloat[] p, int color) {
        super(posX, posY, color);
        this.pShape = p;
        for (int i = 0; i < numVertex; i++ ) {
            pFinal[i] = new Point2DFloat();
        }
        scaleAndOffsetPoints();
    }

    public Triangle2D(float posX, float posY, float x0, float y0, float x1, float y1, float x2, float y2, int color) {
        super(posX, posY, color);
        for (int i = 0; i < numVertex; i++ ) {
            pShape[i] = new Point2DFloat();
            pFinal[i] = new Point2DFloat();
        }
        pShape[0].setX(x0);
        pShape[0].setY(y0);
        pShape[1].setX(x1);
        pShape[1].setY(y1);
        pShape[2].setX(x2);
        pShape[2].setY(y2);
        scaleAndOffsetPoints();
    }

    private void scaleAndOffsetPoints() {
        // Calculate el centro teórico. Hay varias formas pero lo voy a hacer mediante la media de todos los puntos.
        float sumX = 0;
        float sumY = 0;
        for ( int i = 0; i < numVertex; i++ ) {
            sumX += pShape[i].getX();
            sumY += pShape[i].getY();
        }
        sumX /= numVertex;
        sumY /= numVertex;
        Point2DFloat center = new Point2DFloat(sumX, sumY);

        for (int i = 0; i < numVertex; i++ ) {
            pFinal[i].setX(((pShape[i].getX() - center.getX()) * size) + posX);
            pFinal[i].setY(((pShape[i].getY() - center.getY()) * size) + posY);
        }
    }

    @Override
    public void drawYourSelf(Renderer r) {
        switch ( wayToRender ) {
            case WIRE: default:
                r.drawTriangle(
                        (pFinal[0].getX()).intValue(), (pFinal[0].getY()).intValue(),
                        (pFinal[1].getX()).intValue(), (pFinal[1].getY()).intValue(),
                        (pFinal[2].getX()).intValue(), (pFinal[2].getY()).intValue(),
                        color
                );
                r.drawCircle((int)(posX), (int)(posY), 2, color);
                break;
            case SOLID:
                r.drawFillTriangle(
                        (pFinal[0].getX()).intValue(), (pFinal[0].getY()).intValue(),
                        (pFinal[1].getX()).intValue(), (pFinal[1].getY()).intValue(),
                        (pFinal[2].getX()).intValue(), (pFinal[2].getY()).intValue(),
                        color
                );
                break;
            case BLACKBOARD:
                r.drawLine(
                        (pFinal[0].getX()).intValue(), (pFinal[0].getY()).intValue(),
                        (pFinal[1].getX()).intValue(), (pFinal[1].getY()).intValue(),
                        0xffff0000 // Rojo
                );
                r.drawLine(
                        (pFinal[0].getX()).intValue(), (pFinal[0].getY()).intValue(),
                        (pFinal[2].getX()).intValue(), (pFinal[2].getY()).intValue(),
                        0xff0000ff // Azul
                );
                r.drawLine(
                        (pFinal[1].getX()).intValue(), (pFinal[1].getY()).intValue(),
                        (pFinal[2].getX()).intValue(), (pFinal[2].getY()).intValue(),
                        0xffffff00 // Amarillo
                );
                r.drawCircle((int)(posX), (int)(posY), 2, 0xffffffff);
                break;
            case BLUEPRINT:
                r.drawTriangle(
                        (pFinal[0].getX()).intValue(), (pFinal[0].getY()).intValue(),
                        (pFinal[1].getX()).intValue(), (pFinal[1].getY()).intValue(),
                        (pFinal[2].getX()).intValue(), (pFinal[2].getY()).intValue(),
                        0xffffffff
                );
                r.drawCircle((int)(posX), (int)(posY), 2, 0xffffffff);
                break;
        }
    }

    /**
     * En este caso, para saber si un punto esta dentro de un triangulo, es un poco complicado.
     * He utilizado la formula de la siguiente fuente:
     * https://huse360.home.blog/2019/12/14/como-saber-si-un-punto-esta-dentro-de-un-triangulo/
     */
    @Override
    public boolean isPointInside(float x, float y) {
        //Sea d el segmento ab.
        Point2DFloat d = new Point2DFloat(pFinal[1].getX() - pFinal[0].getX(), pFinal[1].getY() - pFinal[0].getY());

        //Sea e el segmento ac.
        Point2DFloat e = new Point2DFloat(pFinal[2].getX() - pFinal[0].getX(), pFinal[2].getY() - pFinal[0].getY());

        //Variable de ponderación a~b
        float w1 = (e.getX() * (pFinal[0].getY() - y) + e.getY() * (x - pFinal[0].getX())) / (d.getX() * e.getY() - d.getY() * e.getX());

        //Variable de ponderación a~c
        float w2 = (y - pFinal[0].getY() - w1 * d.getY()) / e.getY();

        //El punto p se encuentra dentro del triángulo
        // si se cumplen las 3 condiciones:
        return (w1 >= 0.0) && (w2 >= 0.0) && ((w1 + w2) <= 1.0);
    }

    @Override
    public void setPosX(float posX) {
        super.setPosX(posX);
        scaleAndOffsetPoints();
    }

    @Override
    public void setPosY(float posY) {
        super.setPosY(posY);
        scaleAndOffsetPoints();
    }

    public Point2DFloat[] getP() {
        return pFinal;
    }

    public float getSize() {
        return size;
    }

    public void setP(Point2DFloat[] p) {
        this.pFinal = p;
    }

    public void setSize(float size) {
        this.size = size;
        scaleAndOffsetPoints();
    }

}
