package engine.gfx.shapes2d.shapes;

import engine.gfx.Renderer;
import engine.gfx.shapes2d.Shape2D;
import engine.gfx.shapes2d.points2d.Vec2DFloat;

import java.util.ArrayList;

public class Polygon2D extends Shape2D {

    protected int numVertex;

    protected ArrayList<Vec2DFloat> pShape;

    protected ArrayList<Vec2DFloat> pFinal;

    protected Vec2DFloat theoreticalCenter;

    protected float angle = 0.0f;

    public Polygon2D(float posX, float posY, float size, int color) {
        super(posX, posY, color);
        this.size = size;
        pShape = new ArrayList<>();
        pFinal = new ArrayList<>();
        theoreticalCenter = new Vec2DFloat();
        numVertex = 0;
    }

    public Polygon2D(float posX, float posY, float size, int color, Vec2DFloat... points) {
        super(posX, posY, color);
        this.size = size;
        pShape = new ArrayList<>();
        pFinal = new ArrayList<>();
        for ( Vec2DFloat point : points ) {
            pShape.add(point);
            pFinal.add(point);
        }
        numVertex = points.length;
        theoreticalCenter = new Vec2DFloat();
        normalizePShape();
        calculateTheoreticalCenter();
        rotateScaleOffsetPoints();
    }

    public Polygon2D(float posX, float posY, ArrayList<Vec2DFloat> p, float size, int color) {
        super(posX, posY, color);
        this.size = size;
        pShape = p;
        pFinal = p;
        numVertex = p.size();
        theoreticalCenter = new Vec2DFloat();
        normalizePShape();
        calculateTheoreticalCenter();
        rotateScaleOffsetPoints();
    }

    public Polygon2D(float posX, float posY, float size, int numVertex, int color) {
        super(posX, posY, color);
        this.size = size;
        pShape = new ArrayList<>();
        pFinal = new ArrayList<>();
        this.numVertex = numVertex;
        theoreticalCenter = new Vec2DFloat();
        float theta = 3.14159f * 2.0f / this.numVertex;
        for (int i = 0; i < this.numVertex; i++) {
            pShape.add(new Vec2DFloat((float)(this.size * Math.cos(theta * i)), (float)(this.size * Math.sin(theta * i))));
            pFinal.add(new Vec2DFloat((float)(this.size * Math.cos(theta * i)), (float)(this.size * Math.sin(theta * i))));
        }
        normalizePShape();
        calculateTheoreticalCenter();
        rotateScaleOffsetPoints();
    }

    protected void normalizePShape() {
        for ( int i = 0; i < pShape.size(); i++ ) {
            pShape.get(i).set(pShape.get(i).normal());
        }
    }

    /**
     * calculateTheoreticalCenter()
     * Calcula el centro teórico. Hay varias formas pero lo voy a hacer mediante la media de todos los puntos.
     */
    protected void calculateTheoreticalCenter(){
        float sumX = 0;
        float sumY = 0;
        for ( int i = 0; i < numVertex; i++ ) {
            sumX += pShape.get(i).getX();
            sumY += pShape.get(i).getY();
        }
        sumX /= numVertex;
        sumY /= numVertex;
        theoreticalCenter = new Vec2DFloat(sumX, sumY);
    }

    protected void rotateScaleOffsetPoints() {
        for (int i = 0; i < numVertex; i++ ) {
            Vec2DFloat point = new Vec2DFloat(pShape.get(i).getX(), pShape.get(i).getY());
            point.sub(theoreticalCenter);
            point.translateThisAngle(angle);
            point.setX((point.getX() * size) + posX);
            point.setY((point.getY() * size) + posY);
            pFinal.get(i).setX(point.getX());
            pFinal.get(i).setY(point.getY());
        }
    }

    @Override
    public void drawYourSelf(Renderer r) {
        switch ( wayToRender) {
            case WIRE: case SOLID: default:
                r.drawPolygon(pFinal, color);
                break;
            case BLUEPRINT:
                r.drawPolygon(pFinal, 0xffffffff);
                r.drawLine(
                        pFinal.get(0).getX().intValue(), pFinal.get(0).getY().intValue(),
                        (int) (theoreticalCenter.getX().intValue() + posX), (int)(theoreticalCenter.getY().intValue() + posY),
                        0xffffffff);
                r.drawCircle((int)(posX), (int)(posY), 2, 0xffffffff);
                r.drawFillCircle((int)(theoreticalCenter.getX().intValue() + posX), (int)(theoreticalCenter.getY().intValue() + posY), 2, 0xffffffff);
                break;
            case BLACKBOARD:
                r.drawPolygon(pFinal, 0xffff0000);
                r.drawLine(
                        pFinal.get(0).getX().intValue(), pFinal.get(0).getY().intValue(),
                        (int) (theoreticalCenter.getX().intValue() + posX), (int)(theoreticalCenter.getY().intValue() + posY),
                        0xffffff00);
                r.drawCircle((int)(posX), (int)(posY), 2, 0xff0000ff);
                r.drawFillCircle((int)(theoreticalCenter.getX().intValue() + posX), (int)(theoreticalCenter.getY().intValue() + posY), 2, 0xff0000ff);
                break;
        }
        super.drawYourSelf(r);
    }

    @Override
    public boolean isPointInside(float x, float y) {
        return Math.abs((posX - x) * (posX - x) + (posY - y) * (posY - y)) <= (size * size);
    }

    @Override
    public void setPosX(float posX) {
        super.setPosX(posX);
        rotateScaleOffsetPoints();
    }

    @Override
    public void setPosY(float posY) {
        super.setPosY(posY);
        rotateScaleOffsetPoints();
    }

    public ArrayList<Vec2DFloat> getP() {
        return pFinal;
    }

    public void addP(Vec2DFloat p) {
        pShape.add(p);
        pFinal.add(p);
        normalizePShape();
        calculateTheoreticalCenter();
    }

    public float getSize() {
        return size;
    }

    public float getAngle() {
        return angle;
    }

    public void setP(ArrayList<Vec2DFloat> p) {
        this.pShape = p;
        this.pFinal = p;
        normalizePShape();
        calculateTheoreticalCenter();
    }

    public void setSize(float size) {
        this.size = size;
        rotateScaleOffsetPoints();
    }

    public void setAngle(float angle) {
        this.angle = angle;
        rotateScaleOffsetPoints();
    }

}
