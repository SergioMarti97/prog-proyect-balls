package engine.gfx.shapes2d.shapes;

import engine.gfx.Renderer;
import engine.gfx.shapes2d.Shape2D;
import engine.gfx.shapes2d.points2d.Vec2DFloat;

import java.util.ArrayList;

/**
 * Esta clase permite representar un poligno de "n" número de vertices.
 * Además, incluye distintas funciones para transladar el polígono,
 * escalarlo o rotarlo.
 *
 * @class: Polygon2D.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public class Polygon2D extends Shape2D {

    /**
     * El número de vertices del polígono.
     */
    protected int numVertex;

    /**
     * Son los puntos de los vertices del polígono sin modificar,
     * es decir, los originales.
     */
    protected ArrayList<Vec2DFloat> pShape;

    /**
     * Son los puntos de los vertices del polígono modificados,
     * es decir, después de haber sido escalados, rotados, escalados...
     */
    protected ArrayList<Vec2DFloat> pFinal;

    /**
     * El centro teórico de la figura.
     */
    protected Vec2DFloat theoreticalCenter;

    /**
     * El ángulo de rotación de la figura.
     */
    protected float angle = 0.0f;

    /**
     * Constructor, en este caso sería el constructor nulo;
     * ya que no se definen los vertices que conforman el
     * polígono.
     *
     * @param posX Posición X del polígono.
     * @param posY Posición Y del polígono.
     * @param size Tamaño del polígono.
     * @param color El color del polígno.
     */
    public Polygon2D(float posX, float posY, float size, int color) {
        super(posX, posY, color);
        this.size = size;
        pShape = new ArrayList<>();
        pFinal = new ArrayList<>();
        theoreticalCenter = new Vec2DFloat();
        numVertex = 0;
    }

    /**
     * Constructor, mediante un array de puntos para los vértices
     * del polígono.
     *
     * @param posX Posición X del polígono.
     * @param posY Posición Y del polígono.
     * @param size Tamaño del polígono.
     * @param color El color del polígno.
     * @param points Los puntos que conforman el poligono. Se utiliza un array
     *               de objetos <class>Vec2DFloat</class>.
     */
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


    /**
     * Constructor, mediante un arrayList de puntos <class>Vec2DFloat</class>
     * para los vértices del polígono.
     *
     * @param posX Posición X del polígono.
     * @param posY Posición Y del polígono.
     * @param p <class>ArrayList</class>
     * @param size Tamaño del polígono.
     * @param color El color del polígno.
     */
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

    /**
     * Constructor para un polígono regular. Solo hace falta indicar
     * el número de vertices, y el constructor se encarga de crear
     * el polígono regular.
     *
     * @param posX Posición X del polígono.
     * @param posY Posición Y del polígono.
     * @param size Tamaño del polígono.
     * @param numVertex Número de vertices el polígono regular.
     * @param color El color del polígono.
     */
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
     * Calcula el centro teórico. Hay varias formas de hacerlo.
     * En este caso se calcula mediante la media de todos los puntos
     * que forman el poligono.
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

    /**
     * Este método calcula las posiciónes finales de los vertices
     * del polígono.
     */
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

    /**
     * Hay distintas formas de pintar un polígo sólido. En esta versión aún
     * no se ha implementado el código para poder rellenar un poligono.
     * Una primera aproximación es dividir el polígono en triángulos,
     * y gracias a que Renderer tiene un método para pintar un triángulo
     * relleno "drawFillTriangle", se pintan los triangulos que conforman el
     * polígono.
     *
     * El problema es que la triangulación de polígonos es un tema de programación
     * muy complicado. La forma más simple, pero que incrementa mucho el número
     * de triangulos en los que se subdivide un polígono y por lo tanto el costo,
     * es crear los triangulos desde el centro a los vertices. Como si fuera
     * una tarta.
     *
     * @param r El objeto <class>Renderer</class> que tiene todos los métodos de dibujado.
     * @see Renderer
     * @see "https://en.wikipedia.org/wiki/Polygon_triangulation"
     */
    private void drawSolidPolygon(Renderer r) {
        r.drawPolygon(pFinal, color);
        if ( pFinal.size() > 4 ) {
            for ( int i = 2; i <= pFinal.size() -2; i++ ) {
                r.drawLine(
                        pFinal.get(0).getX().intValue(), pFinal.get(0).getY().intValue(),
                        pFinal.get(i).getX().intValue(), pFinal.get(i).getY().intValue(),
                        color);
            }
        }
    }

    /**
     * El método de dibujado del polígono.
     *
     * @param r Es el objeto <class>Renderer</class> que sirve para poder utilizar todos los métodos de dibujado.
     */
    @Override
    public void drawYourSelf(Renderer r) {
        switch ( wayToRender) {
            case WIRE: default:
                r.drawPolygon(pFinal, color);
                break;
            case SOLID:
                drawSolidPolygon(r);
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

    /**
     * El método para saber si un punto esta dentro del polígono o no.
     *
     * @param x La posición x del punto que se va a probar si esta dentro del área.
     * @param y La posición y del punto que se va a probar si esta dentro del área.
     * @return Devuelve si el punto esta dentro o fuera del área del polígono.
     */
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
