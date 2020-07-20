package engine.gfx.images;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Esta clase es el objeto que representa una imagen para
 * poder dibujarla.
 *
 * @class: Image.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public class Image {

    private int w;

    private int h;

    private int[] p;

    private boolean alpha = false;

    /**
     * El constructor de la clase en base a una imagen.
     * @param path Es la ruta absoluta o relativa de la cual se extrae la imagen.
     */
    public Image(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Image.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert image != null;
        w = image.getWidth();
        h = image.getHeight();
        p = image.getRGB(0, 0, w, h, null, 0, w);
        image.flush();
    }

    /**
     * El constructor de la clase en bae a un array de pixeles y
     * al ancho y alto.
     *
     * @param p El array de pixeles de la imagen.
     * @param w El ancho de la imagen.
     * @param h El alto de la imagen.
     */
    public Image(int[] p, int w, int h) {
        this.p = p;
        this.w = w;
        this.h = h;
    }

    /**
     * El constructor de la clase en base al ancho y
     * al alto. Es como el constructor nulo.
     *
     * @param w El ancho de la imagen.
     * @param h El alto de la imagen.
     */
    public Image(int w, int h) {
        this.w = w;
        this.h = h;
        p = new int[w * h];
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int[] getP() {
        return p;
    }

    public boolean isAlpha() {
        return alpha;
    }

    /**
     * Este método devuelve un pixel de una imagen.
     *
     * @param x La posición X del pixel dentro de la imagen.
     * @param y La posición Y del pixel dentro de la imagen.
     * @return devuelve un pixel de la imagen
     * @throws ArrayIndexOutOfBoundsException Si la posición "x" e "y" se encuentra fuera de la imagen.
     */
    public int getSample(int x, int y) throws ArrayIndexOutOfBoundsException {
        int index = x + w * y;
        if ( index < p.length ) {
            return p[x + w * y];
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setP(int[] p) {
        this.p = p;
    }

    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }

}
