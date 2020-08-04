package engine.gfx.shapes2d;

import engine.gfx.Drawable;
import engine.gfx.Renderer;
import engine.gfx.SelectableByMouse;
import engine.maths.points2d.Vec2DGeneralFloat;

/**
 * Esta clase pretende ser la clase generalizada de
 * cualquier figura bidimensional que se quiera
 * dibujar. Por ejemplo: un circulo, un poligono,
 * un triángulo...
 *
 * La ventaja de tener clases que dibujan cualquier
 * tipo de figura es que se pueden utilizar como
 * recursos para cualquier tipo de aplicación.
 * Compartimentalizando el código y teniendo así
 * una base sólida con la que trabajar.
 *
 * @class: Shape2D.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public abstract class Shape2D implements Drawable, SelectableByMouse {

    /**
     * Posición X.
     */
    protected float posX;

    /**
     * Posición Y.
     */
    protected float posY;


    /**
     * El color de la figura. Se utiliza un código hexadecimal para
     * representar el color.
     */
    protected int color;

    /**
     * El tamaño de la figura. Es necesario este campo para los
     * métodos de escalado, translación y rotación.
     */
    protected float size = 1.0f;

    /**
     * Si la figura esta seleccionada o no.
     * @see SelectableByMouse
     */
    protected boolean isSelected;

    /**
     * Si a la hora de dibujar la figura, se debe de mostrar información
     * o no de la figura. La información de la figura se muestra con el
     * método <method>showInfo</method>.
     */
    protected boolean isShowingInformation;

    /**
     * La forma de renderizado o dibujado de la figura.
     * @see WayToRender
     */
    protected WayToRender wayToRender = WayToRender.WIRE;

    /**
     * Constructor.
     *
     * @param posX Posición x.
     * @param posY Posición y.
     * @param color El color, en hexadecimal. Ejemplo: 0xffff0000 = rojo.
     */
    public Shape2D(float posX, float posY, int color) {
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        isSelected = false;
    }

    /**
     * Esta función serviría para mostrar información interesante
     * de la figura, como su posición, el código de color.
     *
     * @param r Es el objeto <class>Renderer</class> que sirve para poder utilizar todos los métodos de dibujado.
     * @param posX posición X donde se va a dibujar esta información.
     * @param posY posición Y donde se va a dibujar esta información.
     *
     * @deprecated En estos momentos no se utiliza para nada. Pero en un futuro podría ser interesante.
     */
    public void showInfo(Renderer r, int posX, int posY) {
        //r.drawFillRect();
    }

    /**
     * Función de dibujado. Debido a que esta clas es la generalización de todas las figuras
     * es
     *
     * @param r Es el objeto <class>Renderer</class> que sirve para poder utilizar todos los métodos de dibujado.
     */
    public void drawYourSelf(Renderer r) {
        if ( isSelected ) {
            r.drawText("¡Seleccionado!", (int)(posX), (int)(posY), 0xffff0000);
        }
    }

    /**
     * Función para comprobar si un punto se encuentra dentro del área de la figura
     * o no.
     *
     * @param x La posición x del punto que se va a probar si esta dentro del área.
     * @param y La posición y del punto que se va a probar si esta dentro del área.
     * @return Devuelve verdadero o falso, en función de si se encuentra fuera o
     *         dentro del área el punto.
     */
    public abstract boolean isPointInside(float x, float y);

    /**
     * Función para comprobar si un punto se encuentra dentro del área de la figura
     * o no. En este caso el parámetro que se le pasa es un objeto de tipo
     * <class>Vec2DFloat</class>.
     *
     * @param point Es la posición del punto que se va a probar si esta dentro del área.
     *              Se utiliza la clase <class>Vec2DFloat</class> que contiene los campos
     *              x e y de tipo <class>Float</class>.
     * @return Devuelve verdadero o falso, en función de si se encuentra fuera o
     *         dentro del área el punto.
     */
    public boolean isPointInside(Vec2DGeneralFloat point) {
        return isPointInside(point.getX(), point.getY());
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public int getColor() {
        return color;
    }

    public float getSize() {
        return size;
    }

    public WayToRender getWayToRender() {
        return wayToRender;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isShowingInformation() {
        return isShowingInformation;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setShowingInformation(boolean showingInformation) {
        isShowingInformation = showingInformation;
    }

    public void setWayToRender(WayToRender wayToRender) {
        this.wayToRender = wayToRender;
    }

}
