package torumpca.pl.gut.mt.algorithm.model;

import java.text.MessageFormat;

/**
 * Created by Tomasz Rumpca on 2016-04-04.
 */
public class VectorComponents {

    public double u;
    public double v;

    public VectorComponents(double u, double v) {
        this.u = u;
        this.v = v;
    }

    @Override
    public String toString() {
        return MessageFormat.format("[u:{0},v:{1}]", u, v);
    }

    public double getU() {
        return u;
    }

    public double getV() {
        return v;
    }
}
