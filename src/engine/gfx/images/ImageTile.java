package engine.gfx.images;

/**
 * Una ImageTile es realmente útil porque es una imagen grande que contiene todas las imagenes
 * que se van a utilizar en el programa. Es una forma de ahorrar y no aumentar el número
 * de archivos de recursos.
 * Importante: todas las imagenes contenidas en la imagen original deben de tener el mismo tamaño
 * de ancho y alto.
 * Como es un tipo de imaagen, hereda de la clase <class>Image</class>.
 *
 * @class: ImageTile.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public class ImageTile extends Image {

    private int tileW;

    private int tileH;

    /**
     * El constructor de la clase.
     *
     * @param path Es la ruta absoluta o relativa de la cual se extrae la imagen.
     * @param tileW Es el tamaño que tienen las imagenes pequeñas de ancho.
     * @param tileH Es el tamaño que tienen las imagenes pequeñas de alto.
     */
    public ImageTile(String path, int tileW, int tileH) {
        super(path);
        this.tileW = tileW;
        this.tileH = tileH;
    }


    /**
     * El método para obtener una imagen pequeña de la
     * imagen original.
     *
     * @param tileX La posición X de la imagen pequeña dentro de la imagen original.
     * @param tileY La posición Y de la imagen pequeña dentro de la imagen original.
     * @return devuelve una imagen pequeña de la imagen original.
     */
    public Image getTileImage(int tileX, int tileY) {
        int[] p = new int[tileW * tileH];
        for ( int y = 0; y < tileH; y++ ) {
            for ( int x = 0; x < tileW; x++ ) {
                try {
                    p[x + y * tileW] = this.getP()[(x + tileX * tileW) + (y + tileY * tileH) * this.getW()];
                } catch ( ArrayIndexOutOfBoundsException e ) {
                    p[x + y * tileW] = 0xffffffff;
                }
            }
        }
        return new Image(p, tileW, tileH);
    }

    public int getTileW() {
        return tileW;
    }

    public int getTileH() {
        return tileH;
    }

}
