package torumpca.pl.gut.mt.model;

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

    public double getSpeed(){
        //TODO calculate direction
        return Math.sqrt(u*u+v*v);
    }

    @Override
    public String toString() {
        return MessageFormat.format("[u:{0},v:{1}]", u, v);
    }
}
