package engine.gfx.shapes2d;

/*
* todo: revisar en Java que operaciones con números en coma flotante son más rapidas. Si float, si double o extended.
*  Si mi programa es de 32Bits (son ejecutables de 32 bits) el más rápido son double. Pero extended tiene
*  nativo el co-procesador matemático.
*  He probado a substutir a Math.sin y .cos por esta clase y va más lento y además pasamos de rotación a escalado?
*/

public class QuickMath {

    private static final int DEGREES = 360;

    private static double[] sins;

    private static double[] coss;

    public QuickMath() {
        double pi = 3.14159265359;
        sins = new double[DEGREES];
        coss = new double[DEGREES];
        for ( int i = 0; i < DEGREES; i++ ) {
            sins[i] = Math.sin(i * pi / 180.0);
            sins[i] = Math.cos(i * pi / 180.0);
        }
    }

    public static double sin(int degree) {
       return degree < DEGREES? sins[degree] : sins[degree % DEGREES];
    }

    public static double cos(int degree) {
        return degree < DEGREES? coss[degree] : coss[degree % DEGREES];
    }

}
