package engine.engine3d;

import engine.GameContainer;
import engine.gfx.Image;
import engine.gfx.Renderer;

public class Graphics3D extends Renderer  {

    private RenderFlags renderFlag = RenderFlags.RENDER_TEXTURED;

    private float depthBuffer;

    public Graphics3D(GameContainer gc) {
        super(gc);
    }

    public void configureDisplay() {

    }

    public void clearDepth() {

    }

    public void addTriangleToScene(Triangle triangle) {

    }

    public void renderScene() {

    }

    public void drawTriangleFlat(Triangle triangle) {
        drawFillTriangle(
                (int)(triangle.getP()[0].getX()), (int)(triangle.getP()[0].getY()),
                (int)(triangle.getP()[1].getX()), (int)(triangle.getP()[1].getY()),
                (int)(triangle.getP()[2].getX()), (int)(triangle.getP()[2].getY()),
                triangle.getColor()
        );
    }

    public void drawTriangleWire(Triangle triangle, int color) {
        drawTriangle(
                (int)(triangle.getP()[0].getX()), (int)(triangle.getP()[0].getY()),
                (int)(triangle.getP()[1].getX()), (int)(triangle.getP()[1].getY()),
                (int)(triangle.getP()[2].getX()), (int)(triangle.getP()[2].getY()),
                color);
    }

    public void drawTexturedTriangle(
            int x1, int y1, float u1, float v1, float w1,
            int x2, int y2, float u2, float v2, float w2,
            int x3, int y3, float u3, float v3, float w3, Image image
    ) {
        if (y2 < y1) {
            int tempInteger = y1;
            y1 = y2;
            y2 = tempInteger;

            tempInteger = x1;
            x1 = x2;
            x2 = tempInteger;

            float tempFloat = u1;
            u1 = u2;
            u2 = tempFloat;

            tempFloat = v1;
            v1 = v2;
            v2 = tempFloat;

            tempFloat = w1;
            w1 = w2;
            w2 = tempFloat;
        }

        if (y3 < y1) {
            int tempInteger = y1;
            y1 = y3;
            y3 = tempInteger;

            tempInteger = x1;
            x1 = x3;
            x3 = tempInteger;

            float tempFloat = u1;
            u1 = u3;
            u3 = tempFloat;

            tempFloat = v1;
            v1 = v3;
            v3 = tempFloat;

            tempFloat = w1;
            w1 = w3;
            w3 = tempFloat;
        }

        if (y3 < y2) {
            int tempInteger = y2;
            y2 = y3;
            y3 = tempInteger;

            tempInteger = x2;
            x2 = x3;
            x3 = tempInteger;

            float tempFloat = u2;
            u2 = u3;
            u3 = tempFloat;

            tempFloat = v2;
            v2 = v3;
            v3 = tempFloat;

            tempFloat = w2;
            w2 = w3;
            w3 = tempFloat;
        }

        int dy1 = y2 - y1;
        int dx1 = x2 - x1;
        float dv1 = v2 - v1;
        float du1 = u2 - u1;
        float dw1 = w2 - w1;

        int dy2 = y3 - y1;
        int dx2 = x3 - x1;
        float dv2 = v3 - v1;
        float du2 = u3 - u1;
        float dw2 = w3 - w1;

        float tex_u;
        float tex_v;
        float tex_w;

        float dax_step = 0, dbx_step = 0;
        float du1_step = 0, dv1_step = 0;
        float du2_step = 0, dv2_step = 0;
        float dw1_step = 0, dw2_step = 0;

        if ( dy1 != 0 ) {
            dax_step = dx1 / (float)Math.abs(dy1);
        }
        if ( dy2 != 0 ) {
            dbx_step = dx2 / (float)Math.abs(dy2);
        }

        if ( dy1 != 0 ) {
            du1_step = du1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dv1_step = dv1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dw1_step = dw1 / (float)Math.abs(dy1);
        }

        if ( dy2 != 0 ) {
            du2_step = du2 / (float)Math.abs(dy2);
        }
        if ( dy2 != 0 ) {
            dv2_step = dv2 / (float)Math.abs(dy2);
        }
        if ( dy2 != 0 ) {
            dw2_step = dw2 / (float)Math.abs(dy2);
        }

        if ( dy1 != 0 ) {
            for ( int i = y1; i <= y2; i++ )
            {
                int ax = (int)(x1 + (float)(i - y1) * dax_step);
                int bx = (int)(x1 + (float)(i - y1) * dbx_step);

                float tex_su = u1 + (float)(i - y1) * du1_step;
                float tex_sv = v1 + (float)(i - y1) * dv1_step;
                float tex_sw = w1 + (float)(i - y1) * dw1_step;

                float tex_eu = u1 + (float)(i - y1) * du2_step;
                float tex_ev = v1 + (float)(i - y1) * dv2_step;
                float tex_ew = w1 + (float)(i - y1) * dw2_step;

                if ( ax > bx ) {
                    int tempInteger = ax;
                    ax = bx;
                    bx = tempInteger;

                    float tempFloat = tex_su;
                    tex_su = tex_eu;
                    tex_eu = tempFloat;

                    tempFloat = tex_sv;
                    tex_sv = tex_ev;
                    tex_ev = tempFloat;

                    tempFloat = tex_sw;
                    tex_sw = tex_ew;
                    tex_ew = tempFloat;
                }

                tex_u = tex_su;
                tex_v = tex_sv;
                tex_w = tex_sw;

                float tstep = 1.0f / ((float)(bx - ax));
                float t = 0.0f;

                for (int j = ax; j < bx; j++)
                {
                    tex_u = (1.0f - t) * tex_su + t * tex_eu;
                    tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;
                    if (tex_w > p[i * pW + j])
                    {
                        setPixel(j, i, image.getSample((int)(tex_u / tex_w), (int)(tex_v / tex_w)));
                        //p[j + i * pW] = (int)(tex_w); // todo esto en el código de Javidx9 es el m_depthBuffer... Aquí no se que es...
                    }
                    t += tstep;
                }
            }
        }

        dy1 = y3 - y2;
        dx1 = x3 - x2;
        dv1 = v3 - v2;
        du1 = u3 - u2;
        dw1 = w3 - w2;

        if ( dy1 != 0 ) {
            dax_step = dx1 / (float)Math.abs(dy1);
        }
        if ( dy2 != 0 ) {
            dbx_step = dx2 / (float)Math.abs(dy2);
        }

        du1_step = 0;
        dv1_step = 0;
        if ( dy1 != 0 ) du1_step = du1 / (float)Math.abs(dy1);
        if ( dy1 != 0 ) dv1_step = dv1 / (float)Math.abs(dy1);
        if ( dy1 != 0 ) dw1_step = dw1 / (float)Math.abs(dy1);

        if ( dy1 != 0 )
        {
            for (int i = y2; i <= y3; i++)
            {
                int ax = (int)(x2 + (float)(i - y2) * dax_step);
                int bx = (int)(x1 + (float)(i - y1) * dbx_step);

                float tex_su = u2 + (float)(i - y2) * du1_step;
                float tex_sv = v2 + (float)(i - y2) * dv1_step;
                float tex_sw = w2 + (float)(i - y2) * dw1_step;

                float tex_eu = u1 + (float)(i - y1) * du2_step;
                float tex_ev = v1 + (float)(i - y1) * dv2_step;
                float tex_ew = w1 + (float)(i - y1) * dw2_step;

                if (ax > bx) {
                    int tempInteger = ax;
                    ax = bx;
                    bx = tempInteger;

                    float tempFloat = tex_su;
                    tex_su = tex_eu;
                    tex_eu = tempFloat;

                    tempFloat = tex_sv;
                    tex_sv = tex_ev;
                    tex_ev = tempFloat;

                    tempFloat = tex_sw;
                    tex_sw = tex_ew;
                    tex_ew = tempFloat;
                }

                tex_u = tex_su;
                tex_v = tex_sv;
                tex_w = tex_sw;

                float tstep = 1.0f / ((float)(bx - ax));
                float t = 0.0f;

                for (int j = ax; j < bx; j++)
                {
                    tex_u = (1.0f - t) * tex_su + t * tex_eu;
                    tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;

                    if (tex_w > p[i * pW + j]) {
                        setPixel(j, i, image.getSample((int)(tex_u / tex_w), (int)(tex_v / tex_w)));
                        //p[i * pW + j] = tex_w;
                    }
                    t += tstep;
                }
            }
        }
    }

    public void drawTriangleTex(Triangle triangle, Image image) {
        drawTexturedTriangle(
                (int)(triangle.getP()[0].getX()), (int)(triangle.getP()[0].getY()), triangle.getT()[0].getX(), triangle.getT()[0].getY(), triangle.getT()[0].getZ(),
                (int)(triangle.getP()[1].getX()), (int)(triangle.getP()[1].getY()), triangle.getT()[1].getX(), triangle.getT()[1].getY(), triangle.getT()[1].getZ(),
                (int)(triangle.getP()[2].getX()), (int)(triangle.getP()[2].getY()), triangle.getT()[2].getX(), triangle.getT()[2].getY(), triangle.getT()[2].getZ(),
                image);
    }

    public RenderFlags getRenderFlag() {
        return renderFlag;
    }

    public void setRenderFlag(RenderFlags renderFlag) {
        renderFlag = renderFlag;
    }

}
