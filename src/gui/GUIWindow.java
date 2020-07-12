package gui;

import engine.Input;
import engine.gfx.Renderer;
import engine.gfx.font.Font;

public class GUIWindow extends GUIElement {

    private final int BACKGROUND_COLOR = 0xff024a86;

    private final int FRONT_COLOR = 0xffbba9bb;

    private final int SELECTED_COLOR = 0xffe7d40a;

    private final int HEADER_HEIGHT = 20;

    private Font font;

    private String headerTitle;

    private boolean isSelected;

    public GUIWindow(Input input, int posX, int posY, int width, int height) {
        super(input, posX, posY, width, height);
        font = Font.STANDARD;
        headerTitle = "title";
        isSelected = false;
    }

    public GUIWindow(Input input, int posX, int posY, int width, int height, String headerTitle) {
        super(input, posX, posY, width, height);
        font = Font.STANDARD;
        this.headerTitle = headerTitle;
        isSelected = false;
    }

    @Override
    public void drawYourSelf(Renderer r) {
        r.drawFillRect(posX, posY, width, height + HEADER_HEIGHT, BACKGROUND_COLOR);
        r.drawFillRect(posX, posY, width, HEADER_HEIGHT, FRONT_COLOR);
        r.drawText(headerTitle, posX, posY, 0xff000000, font);
        if ( isSelected ) {
            r.drawRect(posX, posY, width, HEADER_HEIGHT, SELECTED_COLOR);
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
