package engine.engine3d.orthographic;

import engine.gfx.images.Image;

public class Renderable {

    private Image image;

    public Renderable() {

    }

    public void load(String path) {
        image = new Image(path);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
