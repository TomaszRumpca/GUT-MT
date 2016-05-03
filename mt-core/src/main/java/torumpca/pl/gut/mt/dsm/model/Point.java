package torumpca.pl.gut.mt.dsm.model;

import java.text.MessageFormat;

/**
 * Created by Tomasz Rumpca on 2016-05-03.
 */
public class Point extends java.awt.Point {

    public Point() {
        super();
    }

    public Point(Point p) {
        super(p);
    }

    public Point(java.awt.Point p) {
        super(p);
    }

    public Point(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return MessageFormat.format("[{0},{1}]", x, y);
    }
}
