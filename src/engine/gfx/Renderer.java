package engine.gfx;

import engine.GameContainer;
import engine.gfx.font.Font;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Renderer {

    private Font font = Font.STANDARD;

    private ArrayList<ImageRequest> imageRequests = new ArrayList<>();

    private int pW;

    private int pH;

    private int[] p;

    private int[] zb; // Z Buffer

    private int zDepth = 0;

    private boolean processing = false;

    public Renderer(GameContainer gc) {
        pW = gc.getWidth();
        pH = gc.getHeight();
        p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zb = new int[p.length];
    }

    public void clear(int color) {
        //Arrays.fill(p, color);
        for ( int i = 0; i < p.length; i++ ) {
            p[i] = color;
            zb[i] = 0;
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
            p[index] = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue);
        }
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

    public void drawLineForFillTriangle(int sx, int ex, int ny, int color) {
        for (int i = sx; i <= ex; i++) {
            setPixel(i, ny, color);
        }
    }

    public void drawFillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int color) {
        int t1x, t2x, y, minx, maxx, t1xp, t2xp;
        boolean changed1 = false;
        boolean changed2 = false;
        int signx1, signx2, dx1, dy1, dx2, dy2;
        int e1, e2;

        // Ordenar los vertices por altura
        if ( y1 > y2 ) {
            int temp = y1;
            y1 = y2;
            y2 = temp;

            temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if ( y1 > y3 ) {
            int temp = y1;
            y1 = y3;
            y3 = temp;

            temp = x1;
            x1 = x3;
            x3 = temp;
        }
        if ( y2 > y3 ) {
            int temp = y2;
            y2 = y3;
            y3 = temp;

            temp = x2;
            x2 = x3;
            x3 = temp;
        }

        t1x = t2x = x1; y = y1;   // Puntos de inicio
        dx1 = (int)(x2 - x1); if (dx1<0) { dx1 = -dx1; signx1 = -1; }
        else signx1 = 1;
        dy1 = (int)(y2 - y1);

        dx2 = (int)(x3 - x1); if (dx2<0) { dx2 = -dx2; signx2 = -1; }
        else signx2 = 1;
        dy2 = (int)(y3 - y1);

        if ( dy1 > dx1 ) {
            int temp = dx1;
            dx1 = dy1;
            dy1 = temp;
            changed1 = true;
        }
        if ( dy2 > dx2 ) {
            int temp = dx2;
            dx2 = dy2;
            dy2 = temp;
            changed2 = true;
        }

        /*e2 = (int)(dx2 >> 1);
        // Flat top, just process the second half
        if (y1 == y2) {
            goto ;
        }
        e1 = (int)(dx1 >> 1);

        for (int i = 0; i < dx1;) {
            t1xp = 0; t2xp = 0;
            if (t1x<t2x) { minx = t1x; maxx = t2x; }
            else { minx = t2x; maxx = t1x; }
            // process first line until y value is about to change
            while (i<dx1) {
                i++;
                e1 += dy1;
                while (e1 >= dx1) {
                    e1 -= dx1;
                    if (changed1) t1xp = signx1;//t1x += signx1;
                    else          goto next1;
                }
                if (changed1) break;
                else t1x += signx1;
            }
            // Move line
            next1:
            // process second line until y value is about to change
            while (1) {
                e2 += dy2;
                while (e2 >= dx2) {
                    e2 -= dx2;
                    if (changed2) t2xp = signx2;//t2x += signx2;
                    else          goto next2;
                }
                if (changed2)     break;
                else              t2x += signx2;
            }
            next2:
            if (minx>t1x) minx = t1x;
            if (minx>t2x) minx = t2x;
            if (maxx<t1x) maxx = t1x;
            if (maxx<t2x) maxx = t2x;
            drawline(minx, maxx, y);    // Draw line from min to max points found on the y
            // Now increase y
            if (!changed1) t1x += signx1;
            t1x += t1xp;
            if (!changed2) t2x += signx2;
            t2x += t2xp;
            y += 1;
            if (y == y2) break;

        }
        next:
        // Second half
        dx1 = (int)(x3 - x2); if (dx1<0) { dx1 = -dx1; signx1 = -1; }
        else signx1 = 1;
        dy1 = (int)(y3 - y2);
        t1x = x2;

        if (dy1 > dx1) {   // swap values
            SWAP(dy1, dx1);
            changed1 = true;
        }
        else changed1 = false;

        e1 = (int)(dx1 >> 1);

        for (int i = 0; i <= dx1; i++) {
            t1xp = 0; t2xp = 0;
            if (t1x<t2x) { minx = t1x; maxx = t2x; }
            else { minx = t2x; maxx = t1x; }
            // process first line until y value is about to change
            while (i<dx1) {
                e1 += dy1;
                while (e1 >= dx1) {
                    e1 -= dx1;
                    if (changed1) { t1xp = signx1; break; }//t1x += signx1;
                    else          goto next3;
                }
                if (changed1) break;
                else   	   	  t1x += signx1;
                if (i<dx1) i++;
            }
            next3:
            // process second line until y value is about to change
            while (t2x != x3) {
                e2 += dy2;
                while (e2 >= dx2) {
                    e2 -= dx2;
                    if (changed2) t2xp = signx2;
                    else          goto next4;
                }
                if (changed2)     break;
                else              t2x += signx2;
            }
            next4:

            if (minx>t1x) minx = t1x;
            if (minx>t2x) minx = t2x;
            if (maxx<t1x) maxx = t1x;
            if (maxx<t2x) maxx = t2x;
            drawline(minx, maxx, y);
            if (!changed1) t1x += signx1;
            t1x += t1xp;
            if (!changed2) t2x += signx2;
            t2x += t2xp;
            y += 1;
            if (y>y3) return;
        }*/

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

    public int getZDepth() {
        return zDepth;
    }

    public void setZDepth(int zDepth) {
        this.zDepth = zDepth;
    }
}
