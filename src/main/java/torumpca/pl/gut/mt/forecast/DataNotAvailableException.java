package torumpca.pl.gut.mt.forecast;

import java.text.MessageFormat;

/**
 * Created by Tomasz Rumpca on 2016-04-11.
 */
public class DataNotAvailableException extends Exception {

    public DataNotAvailableException(String errorMessage){
        super(errorMessage);
    }

    public DataNotAvailableException(String errorMessage, String... args){
        super(MessageFormat.format(errorMessage, args));
    }

    public DataNotAvailableException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public DataNotAvailableException(String errorMessage, Throwable cause, String... args){
        super(MessageFormat.format(errorMessage, args), cause);
    }

}
