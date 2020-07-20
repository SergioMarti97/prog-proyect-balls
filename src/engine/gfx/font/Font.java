package engine.gfx.font;

import engine.gfx.images.Image;

/**
 * Esta clase es la fuente de la tipografía de los textos que se muestran por pantalla.
 *
 * @class: Font.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public class Font {

    public static final Font STANDARD = new Font("/fonts/consolas.png");

    public static final Font STANDARD24 = new Font("/fonts/consolas24.png");

    public static final Font COMICSANS = new Font("/fonts/comicsans.png");

    private Image fontImage;

    private int[] offsets;

    private int[] widths;

    public Font(String path) {
        fontImage = new Image(path);
        offsets = new int[256];
        widths = new int[256];
        int unicode = 0;
        for ( int i = 0; i < fontImage.getW(); i++ ) {
            if ( fontImage.getP()[i] == 0xff0000ff ) {
                offsets[unicode] = i;
            }
            if ( fontImage.getP()[i] == 0xffffff00 ) {
                widths[unicode] = i - offsets[unicode];
                unicode++;
            }
        }
    }

    public Image getFontImage() {
        return fontImage;
    }

    public int[] getOffsets() {
        return offsets;
    }

    public int[] getWidths() {
        return widths;
    }

    public void setFontImage(Image fontImage) {
        this.fontImage = fontImage;
    }

    public void setOffsets(int[] offsets) {
        this.offsets = offsets;
    }

    public void setWidths(int[] widths) {
        this.widths = widths;
    }

}
