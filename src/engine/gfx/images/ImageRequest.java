package engine.gfx.images;

/**
 * Los requisitos que tienen las imagen de la <class>Image</class>.
 *
 * @class: HexColors.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public class ImageRequest {

    private Image image;

    private int zDepth;

    private int offX;

    private int offY;

    /**
     * Constructor.
     *
     * @param image La imagen.
     * @param zDepth La altura en el eje Z de la imagen. Es decir, para la superposición de unas imagenes con otras.
     * @param offX El offset, o desplazamiento en el eje X.
     * @param offY El offset, o desplazamiento en el eje Y.
     */
    public ImageRequest(Image image, int zDepth, int offX, int offY) {
        this.image = image;
        this.zDepth = zDepth;
        this.offX = offX;
        this.offY = offY;
    }

    public Image getImage() {
        return image;
    }

    public int getzDepth() {
        return zDepth;
    }

    public int getOffX() {
        return offX;
    }

    public int getOffY() {
        return offY;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }

    public void setOffX(int offX) {
        this.offX = offX;
    }

    public void setOffY(int offY) {
        this.offY = offY;
    }

}
