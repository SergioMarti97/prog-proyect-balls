package engine.gfx;

import engine.gfx.Renderer;

/**
 * Esta interfaz implementa el método <method>drawYourSelf</method>. Se
 * debe de implementar en todas las clases que se vayan a dibujar en la
 * pantalla.
 *
 * @class: Drawable.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public interface Drawable {

    /**
     * Este método se tendrá que sobrescribir en todos las clases que se deban de dibujar.
     * Todos los métodos de dibujado están en la clase <class>Renderer</class>, por ello
     * se le pasa como parámetro este objeto.
     *
     * @param r Es el objeto <class>Renderer</class> que sirve para poder utilizar todos los métodos dibujado.
     */
    void drawYourSelf(Renderer r);

}
