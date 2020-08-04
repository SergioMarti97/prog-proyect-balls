package engine.gfx;

import engine.GameContainer;
import engine.gfx.font.Font;
import engine.gfx.images.Image;
import engine.gfx.images.ImageRequest;
import engine.gfx.images.ImageTile;
import engine.maths.Mat3x3;
import engine.maths.MatrixOperations;
import engine.maths.points2d.Vec2DGeneralFloat;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Es la clase de renderización. Aquí es donde están todos los
 * métodos de dibujado. Los métodos están pensandos para trabajar
 * sobre un array unidimensional de pixeles.
 * En este caso los pixeles son ints. En estos ints se almacenan
 * códigos tipo hexadecimales (cómo 0xff000000 = negro) que
 * posteriormente se interpretarán como colores.
 *
 * @class: Renderer.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public class Renderer {

    protected Font font = Font.STANDARD24;

    protected ArrayList<ImageRequest> imageRequests = new ArrayList<>();

    protected int pW;

    protected int pH;

    protected int[] p;

    protected int[] zb; // Z Buffer

    protected int zDepth = 0;

    protected int[] lm;

    protected int[] lb;

    protected boolean processing = false;

    protected int ambientColor = 0xffffffff;//0xff6b6b6b;

    public Renderer(GameContainer gc) {
        pW = gc.getWidth();
        pH = gc.getHeight();
        p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zb = new int[p.length];
        lm = new int[p.length];
        lb = new int[p.length];
    }

    public void clear(int color) {
        //Arrays.fill(p, color);
        for ( int i = 0; i < p.length; i++ ) {
            p[i] = color;
            zb[i] = 0;
            lm[i] = ambientColor;
            lb[i] = 0;
        }
    }

    public void clear() {
        clear(0xff000000);
    }

    public void process() {
        processing = true;

        Collections.sort(imageRequests, new Comparator<ImageRequest>() {
                    @Override
                    public int compare(ImageRequest i0, ImageRequest i1) {
                        if ( i0.getzDepth() < i1.getzDepth() ) {
                            return -1;
                        } else if ( i0.getzDepth() > i1.getzDepth() ) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }
        );

        for ( int i = 0; i < imageRequests.size(); i++ ) {
            ImageRequest ir = imageRequests.get(i);
            setZDepth(ir.getzDepth());
            drawImage(ir.getImage(), ir.getOffX(), ir.getOffY());
        }

        for ( int i = 0; i < p.length; i++ ) {
            float r = ((lm[i] >> 16) & 0xff) / 255.0f;
            float g = ((lm[i] >> 8) & 0xff) / 255.0f;
            float b = (lm[i] & 0xff) / 255.0f;
            p[i] = ( (int)(((p[i] >> 16) & 0xff) * r) << 16 | (int)(((p[i] >> 8) & 0xff) * g) << 8 | (int)((p[i] & 0xff) * b));
        }

        imageRequests.clear();
        processing = false;
    }

    public void setPixel(int x, int y, int value) {
        int alpha = ((value >> 24) & 0xff);
        if ( (x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0 ) { // value == 0xffff00ff
            return;
        }
        int index = x + y * pW;
        if ( zb[index] > zDepth ) {
            return;
        }
        zb[index] = zDepth;
        if ( alpha == 255 ) {
            p[index] = value;
        } else {
            int pixelColor = p[index];
            int newRed = ((pixelColor >> 16) & 0xff) - (int)((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255.0f));
            int newGreen = ((pixelColor >> 8) & 0xff) - (int)((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255.0f));
            int newBlue = (pixelColor & 0xff) - (int)(((pixelColor & 0xff) - (value & 0xff)) * (alpha / 255.0f));
            p[index] = (newRed << 16 | newGreen << 8 | newBlue); //(255 << 24 | newRed << 16 | newGreen << 8 | newBlue)
        }
    }

    public void setLightMap(int x, int y, int value) {
        if ( x < 0 || x >= pW || y < 0 || y >= pH ) {
            return;
        }
        int baseColor = lm[x + y * pW];

        int maxRed = Math.max(((baseColor >> 16) & 0xff), ((value >> 16) & 0xff));
        int maxGreen = Math.max(((baseColor >> 8) & 0xff), ((value >> 8) & 0xff));
        int maxBlue = Math.max((baseColor & 0xff), (value & 0xff));

        lm[x + y * pW] = (maxRed << 16 | maxGreen << 8 | maxBlue);
    }

    public void drawLine(int x1, int y1, int x2, int y2, int color) {
        int x, y, dx, dy, dx1, dy1, px, py, xe, ye, i;
        dx = x2 - x1; dy = y2 - y1;

        // straight lines idea by gurkanctn
        if (dx == 0) // Line is vertical
        {
            if (y2 < y1) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }
            for (y = y1; y <= y2; y++) {
                setPixel(x1, y, color);
            }
            return;
        }

        if (dy == 0) // Line is horizontal
        {
            if (x2 < x1) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }
            for (x = x1; x <= x2; x++) {
                setPixel(x, y1, color);
            }
            return;
        }

        // Line is Funk-aye
        dx1 = Math.abs(dx); dy1 = Math.abs(dy);
        px = 2 * dy1 - dx1;	py = 2 * dx1 - dy1;
        if (dy1 <= dx1)
        {
            if (dx >= 0)
            {
                x = x1; y = y1; xe = x2;
            }
            else
            {
                x = x2; y = y2; xe = x1;
            }

            setPixel(x, y, color);

            for (i = 0; x<xe; i++)
            {
                x = x + 1;
                if (px<0)
                    px = px + 2 * dy1;
                else
                {
                    if ((dx<0 && dy<0) || (dx>0 && dy>0)) y = y + 1; else y = y - 1;
                    px = px + 2 * (dy1 - dx1);
                }
                setPixel(x, y, color);
            }
        }
        else
        {
            if (dy >= 0)
            {
                x = x1; y = y1; ye = y2;
            }
            else
            {
                x = x2; y = y2; ye = y1;
            }

            setPixel(x, y, color);

            for (i = 0; y<ye; i++)
            {
                y = y + 1;
                if (py <= 0)
                    py = py + 2 * dx1;
                else
                {
                    if ((dx<0 && dy<0) || (dx>0 && dy>0)) x = x + 1; else x = x - 1;
                    py = py + 2 * (dx1 - dy1);
                }
                setPixel(x, y, color);
            }
        }
    }

    public void drawRectangle(int offX, int offY, int width, int height, int color) {
        // Don't render code
        if ( offX < -width ) {
            return;
        }
        if ( offY < -height ) {
            return;
        }
        if ( offX >= pW ) {
            return;
        }
        if ( offY >= pH ) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWidth = width;
        int newHeight = height;

        // Clipping Code
        if ( offX < 0 ) {
            newX -= offX;
        }
        if ( offY < 0 ) {
            newY -= offY;
        }
        if ( newWidth + offX >= pW ) {
            newWidth -= (newWidth + offX - pW);
        }
        if ( newHeight + offY >= pH ) {
            newHeight -= (newHeight + offY - pH);
        }

        for ( int y = newY; y <= newHeight; y++ ) {
            setPixel(offX, y + offY, color);
            setPixel(offX + width, y + offY, color);
        }
        for ( int x = newX; x <= newWidth; x++ ) {
            setPixel(x + offX, offY, color);
            setPixel(x + offX, offY + height, color);
        }
    }

    public void drawFillRectangle(int offX, int offY, int width, int height, int color) {
        // Don't render code
        if ( offX < -width ) {
            return;
        }
        if ( offY < -height ) {
            return;
        }
        if ( offX >= pW ) {
            return;
        }
        if ( offY >= pH ) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWidth = width;
        int newHeight = height;

        // Clipping Code
        if ( offX < 0 ) {
            newX -= offX;
        }
        if ( offY < 0 ) {
            newY -= offY;
        }
        if ( newWidth + offX >= pW ) {
            newWidth -= (newWidth + offX - pW);
        }
        if ( newHeight + offY >= pH ) {
            newHeight -= (newHeight + offY - pH);
        }

        for ( int y = newY; y < newHeight; y++ ) {
            for ( int x = newX; x < newWidth; x++ ) {
                setPixel(x + offX, y + offY, color);
            }
        }
    }

    public void drawRect(int x, int y, int w, int h, int color) {
        drawLine(x, y, x + w, y, color);
        drawLine(x + w, y, x + w, y + h, color);
        drawLine(x + w, y + h, x, y + h, color);
        drawLine(x, y + h, x, y, color);
    }

    public void drawFillRect(int x, int y, int w, int h, int color) {
        int x2 = x + w;
        int y2 = y + h;

        if (x < 0) x = 0;
        if (x >= pW) x = pW;
        if (y < 0) y = 0;
        if (y >= pH) y = pH;

        if (x2 < 0) x2 = 0;
        if (x2 >= pW) x2 = pW;
        if (y2 < 0) y2 = 0;
        if (y2 >= pH) y2 = pH;

        for (int i = x; i < x2; i++) {
            for (int j = y; j < y2; j++) {
                setPixel(i, j, color);
            }
        }
    }

    public void drawCircle(int x, int y, int radius, int color) {
        int x0 = 0;
        int y0 = radius;
        int d = 3 - 2 * radius;
        if ( radius == 0) {
            return;
        }

        while (y0 >= x0) // only formulate 1/8 of circle
        {
            setPixel(x + x0, y - y0, color);
            setPixel(x + y0, y - x0, color);
            setPixel(x + y0, y + x0, color);
            setPixel(x + x0, y + y0, color);
            setPixel(x - x0, y + y0, color);
            setPixel(x - y0, y + x0, color);
            setPixel(x - y0, y - x0, color);
            setPixel(x - x0, y - y0, color);
            if (d < 0) d += 4 * x0++ + 6;
            else d += 4 * (x0++ - y0--) + 10;
        }
    }

    private void drawLineForFillCircle(int sx, int ex, int ny, int color) {
        for (int i = sx; i <= ex; i++) {
            setPixel(i, ny, color);
        }
    }

    public void drawFillCircle(int x, int y, int radius, int color) {
        int x0 = 0;
        int y0 = radius;
        int d = 3 - 2 * radius;
        if ( radius == 0 ) {
            return;
        }

        while (y0 >= x0)
        {
            // Modified to draw scan-lines instead of edges
            drawLineForFillCircle(x - x0, x + x0, y - y0, color);
            drawLineForFillCircle(x - y0, x + y0, y - x0, color);
            drawLineForFillCircle(x - x0, x + x0, y + y0, color);
            drawLineForFillCircle(x - y0, x + y0, y + x0, color);
            if (d < 0) d += 4 * x0++ + 6;
            else d += 4 * (x0++ - y0--) + 10;
        }
    }

    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int color) {
        drawLine(x1, y1, x2, y2, color);
        drawLine(x2, y2, x3, y3, color);
        drawLine(x3, y3, x1, y1, color);
    }

    public void drawFillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int color) {
        if (y2 < y1) {
            int tempInteger = y1;
            y1 = y2;
            y2 = tempInteger;

            tempInteger = x1;
            x1 = x2;
            x2 = tempInteger;
        }

        if (y3 < y1) {
            int tempInteger = y1;
            y1 = y3;
            y3 = tempInteger;

            tempInteger = x1;
            x1 = x3;
            x3 = tempInteger;
        }

        if (y3 < y2) {
            int tempInteger = y2;
            y2 = y3;
            y3 = tempInteger;

            tempInteger = x2;
            x2 = x3;
            x3 = tempInteger;
        }

        int dy1 = y2 - y1;
        int dx1 = x2 - x1;

        int dy2 = y3 - y1;
        int dx2 = x3 - x1;

        float dax_step = 0, dbx_step = 0;

        if ( dy1 != 0 ) {
            dax_step = dx1 / (float)Math.abs(dy1);
        }
        if ( dy2 != 0 ) {
            dbx_step = dx2 / (float)Math.abs(dy2);
        }

        if ( dy1 != 0 ) {
            for ( int i = y1; i <= y2; i++ )
            {
                int ax = (int)(x1 + (float)(i - y1) * dax_step);
                int bx = (int)(x1 + (float)(i - y1) * dbx_step);

                if ( ax > bx ) {
                    int tempInteger = ax;
                    ax = bx;
                    bx = tempInteger;
                }

                float tstep = 1.0f / ((float)(bx - ax));
                float t = 0.0f;

                for (int j = ax; j < bx; j++) {
                    int index = i * pW + j;
                    if (index < p.length) {
                        setPixel(j, i, color);
                    }
                    t += tstep;
                }
            }
        }

        dy1 = y3 - y2;
        dx1 = x3 - x2;

        if ( dy1 != 0 ) {
            dax_step = dx1 / (float)Math.abs(dy1);
        }
        if ( dy2 != 0 ) {
            dbx_step = dx2 / (float)Math.abs(dy2);
        }

        if ( dy1 != 0 )
        {
            for (int i = y2; i <= y3; i++)
            {
                int ax = (int)(x2 + (float)(i - y2) * dax_step);
                int bx = (int)(x1 + (float)(i - y1) * dbx_step);

                if (ax > bx) {
                    int tempInteger = ax;
                    ax = bx;
                    bx = tempInteger;
                }

                float tstep = 1.0f / ((float)(bx - ax));
                float t = 0.0f;

                for (int j = ax; j < bx; j++) {
                    int index = i * pW + j;
                    if (index < p.length) {
                        setPixel(j, i, color);
                        //p[i * pW + j] = tex_w;
                    }
                    t += tstep;
                }
            }
        }
    }

    public void drawPolygon(ArrayList<Vec2DGeneralFloat> points, int color) {
        for ( int i = 0; i < points.size() - 1; i++ ) {
            drawLine(
                    points.get(i).getX().intValue(), points.get(i).getY().intValue(),
                    points.get(i + 1).getX().intValue(), points.get(i + 1).getY().intValue(),
                    color);
        }
        drawLine(
                points.get(points.size() - 1).getX().intValue(), points.get(points.size() - 1).getY().intValue(),
                points.get(0).getX().intValue(), points.get(0).getY().intValue(),
                color);
    }

    public void drawImage(Image image, int offX, int offY) {

        if ( image.isAlpha() && !processing) {
            imageRequests.add(new ImageRequest(image, zDepth, offX, offY));
            return;
        }

        // Don't render code
        if ( offX < -pW ) {
            return;
        }
        if ( offY < -pH ) {
            return;
        }
        if ( offX >= pW ) {
            return;
        }
        if ( offY >= pH ) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWidth = image.getW();
        int newHeight = image.getH();

        // Clipping Code
        if ( offX < 0 ) {
            newX -= offX;
        }
        if ( offY < 0 ) {
            newY -= offY;
        }
        if ( newWidth + offX >= pW ) {
            newWidth -= (newWidth + offX - pW);
        }
        if ( newHeight + offY >= pH ) {
            newHeight -= (newHeight + offY - pH);
        }

        for ( int y = newY; y < newHeight; y++ ) {
            for ( int x = newX; x < newWidth; x++ ) {
                setPixel(x + offX, y + offY, image.getP()[x + y * image.getW()]);
            }
        }
    }

    public void drawImage(Image image, Mat3x3 transformation) {
        Mat3x3 transformationInv;
        transformationInv = MatrixOperations.invert(transformation);

        Vec2DGeneralFloat p = MatrixOperations.forward(transformation, 0.0f, 0.0f);
        Vec2DGeneralFloat end = new Vec2DGeneralFloat();
        Vec2DGeneralFloat start = new Vec2DGeneralFloat();

        start.set(p);
        end.set(p);

        p = MatrixOperations.forward(transformation, (float)(image.getW()), (float)(image.getH()));
        start.setX(Math.min(start.getX(), p.getX()));
        start.setY(Math.min(start.getY(), p.getY()));
        end.setX(Math.max(end.getX(), p.getX()));
        end.setY(Math.max(end.getY(), p.getY()));

        p = MatrixOperations.forward(transformation, 0.0f, (float)(image.getH()));
        start.setX(Math.min(start.getX(), p.getX()));
        start.setY(Math.min(start.getY(), p.getY()));
        end.setX(Math.max(end.getX(), p.getX()));
        end.setY(Math.max(end.getY(), p.getY()));

        p = MatrixOperations.forward(transformation, (float)(image.getW()), 0.0f);
        start.setX(Math.min(start.getX(), p.getX()));
        start.setY(Math.min(start.getY(), p.getY()));
        end.setX(Math.max(end.getX(), p.getX()));
        end.setY(Math.max(end.getY(), p.getY()));

        int pixel;
        Vec2DGeneralFloat newPos;
        for ( int x = start.getX().intValue(); x < end.getX().intValue(); x++ ) {
            for ( int y = start.getY().intValue(); y < end.getY().intValue(); y++ ) {
                newPos = MatrixOperations.forward(transformationInv, (float)(x), (float)(y));
                if ( newPos.getX() >= 0 && newPos.getX() < image.getW() && newPos.getY() >= 0 && newPos.getY() < image.getH() ) {
                    pixel = image.getSample((int) (newPos.getX().intValue() + 0.5f), (int) (newPos.getY().intValue() + 0.5f));
                    setPixel(x, y, pixel);
                }
            }
        }
    }

    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY) {
        if ( image.isAlpha() && !processing) {
            imageRequests.add(new ImageRequest(image.getTileImage(tileX, tileY), zDepth, offX, offY));
            return;
        }

        // Don't render code
        if ( offX < -image.getTileW() ) {
            return;
        }
        if ( offY < -image.getTileH() ) {
            return;
        }
        if ( offX >= image.getTileW() ) {
            return;
        }
        if ( offY >= image.getTileH() ) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWidth = image.getTileW();
        int newHeight = image.getTileH();

        // Clipping Code
        if ( offX < 0 ) {
            newX -= offX;
        }
        if ( offY < 0 ) {
            newY -= offY;
        }
        if ( newWidth + offX >= pW ) {
            newWidth -= (newWidth + offX - pW);
        }
        if ( newHeight + offY >= pH ) {
            newHeight -= (newHeight + offY - pH);
        }

        for ( int y = newY; y < newHeight; y++ ) {
            for ( int x = newX; x < newWidth; x++ ) {
                setPixel(x + offX, y + offY,
                        image.getP()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getW()]);
            }
        }
    }

    public void drawText(String text, int offX, int offY, int color) {
        int offset = 0;
        for ( int i = 0; i < text.length(); i++ ) {
            int unicode = text.codePointAt(i);
            for ( int y = 0; y < font.getFontImage().getH(); y++ ) {
                for ( int x = 0; x < font.getWidths()[unicode]; x++ ) {
                    if ( font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getW()] == 0xffffffff ) {
                        setPixel(x + offset + offX, y + offY, color);
                    }
                }
            }
            offset += font.getWidths()[unicode];
        }
    }

    public void drawText(String text, int offX, int offY, int color, Font font) {
        int offset = 0;
        for ( int i = 0; i < text.length(); i++ ) {
            int unicode = text.codePointAt(i);
            for ( int y = 0; y < font.getFontImage().getH(); y++ ) {
                for ( int x = 0; x < font.getWidths()[unicode]; x++ ) {
                    if ( font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getW()] == 0xffffffff ) {
                        setPixel(x + offset + offX, y + offY, color);
                    }
                }
            }
            offset += font.getWidths()[unicode];
        }
    }

    public int getZDepth() {
        return zDepth;
    }

    public int getAmbientColor() {
        return ambientColor;
    }

    public void setZDepth(int zDepth) {
        this.zDepth = zDepth;
    }

    public void setAmbientColor(int ambientColor) {
        this.ambientColor = ambientColor;
    }

}
