package torumpca.pl.gut.mt.dsm.model;

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
        return Math.sqrt(u*u+v*v);
    }
}
