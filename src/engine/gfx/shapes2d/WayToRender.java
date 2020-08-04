package engine.gfx.shapes2d;

/**
 * En esta enumeración quiero que estén todas las formas de
 * renderizado (dibujado) que pueden tener las figuras
 * bidimensionales del paquete shapes2d.
 *
 * Estas són:
 *   - SOLID: la forma plana, rellenado.
 *   - WIRE: únicamente lineas conectando los vértices.
 *   - BLACKBOARD: las figuras como en la pizzarra del cole, a colores con cada parte.
 *   - BLUEPRINT: las figuras como si fueran de unas instrucciones de fondo azul.
 *
 * @class: WayToRender.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public enum WayToRender {

    SOLID,
    WIRE,
    BLACKBOARD,
    BLUEPRINT,
    TEXTURED

}
