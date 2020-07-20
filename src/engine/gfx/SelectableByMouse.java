package engine.gfx;

/**
 * Esta interfaz implementa el método <method>isPointInside</method>.
 * Este método sirve para saver cuando un punto se encuentra dentro
 * de un área. De esa forma, se puede saber si el ratón está dentro
 * de una figura, y así poder seleccionarse.
 *
 * Al ser una interfaz, no se pueden añadir los campos de
 * <field>isSelected</field>, que sería más correcto aún y serviría
 * para reciclar el código que se utiliza para seleccionar objetos
 * que se imprimen por pantalla. Para ello, se debería de crear
 * una clase abstracta.
 *
 * Ejemplos:
 *   Para saber si el punto (x, y) se encuentra dentro del círculo
 *   con origen en el punto (ox, oy) y de radio "r" se utiliza la
 *   siguiente fórmula:
 *
 *   return (ox - x) * (ox - x) + (oy - y) * (oy - y) < (r * r)
 *
 *   Esta fórmula viene de aplicar Pitágoras. No es necesario utilizar
 *   la raiz cuadrada ya que esa operación consume muchos recursos del
 *   ordenador. Es mejor comparar las áreas, es lo mismo al final.
 *
 *   Sin embargo, para comprobar que un punto se encuentra dentro de
 *   un triangulo, el algoritmo es mucho más complicado.
 *
 * @see "https://huse360.home.blog/2019/12/14/como-saber-si-un-punto-esta-dentro-de-un-triangulo/"
 *
 * @class: SelecteableByMouse.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public interface SelectableByMouse {

    /**
     * Esta función es la utilizada para saber si un punto (x, y) se encuentra dentro
     * de una figura. Normalmente, este punto son las coordenadas "x" e "y" del
     * ratón.
     *
     * @param x La posición x del punto que se va a probar si esta dentro del área.
     * @param y La posición y del punto que se va a probar si esta dentro del área.
     * @return Devuelve verdadero o falso, en función de si se encuentra fuera o
     *         dentro del área el punto.
     */
    boolean isPointInside(float x, float y);

}
