package engine.gfx.shapes2d;

/**
 * En esta enumeración quiero numerar las formas de renderizado que pueden tener las figuras bidimensionales
 * del paquete shapes2d.
 * Estas són:
 *   - SOLID: la forma plana, rellenado.
 *   - WIRE: únicamente lineas conectando los vértices.
 *   - BLACKBOARD: las figuras como en la pizzarra del cole, a colores con cada parte.
 *   - BLUEPRINT: las figuras como si fueran de unas instrucciones de fondo azul.
 */
public enum WayToRender {

    SOLID,
    WIRE,
    BLACKBOARD,
    BLUEPRINT,

}
