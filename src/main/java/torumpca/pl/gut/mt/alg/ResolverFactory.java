package torumpca.pl.gut.mt.alg;

/**
 * Created by Tomasz Rumpca on 2016-04-11.
 */
public class ResolverFactory {

    public static ProblemResolver getResolver() {
        return new AStarResolver();
    }
}
