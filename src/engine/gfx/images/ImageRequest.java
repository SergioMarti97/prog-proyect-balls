package engine.gfx.images;

public class ImageRequest {

    private Image image;

    private int zDepth;

    private int offX;

    private int offY;

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
