package engine.gfx.images;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Image {

    private int w;

    private int h;

    private int[] p;

    private boolean alpha = false;

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

    public Image(int[] p, int w, int h) {
        this.p = p;
        this.w = w;
        this.h = h;
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

    public int getSample(int x, int y) {
        return p[x + w * y];
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
