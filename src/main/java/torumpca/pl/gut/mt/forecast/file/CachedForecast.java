package torumpca.pl.gut.mt.forecast.file;

public class CachedForecast {

    private final String metaDataFileName;
    private final String uWindFileName;
    private final String vWindFileName;

    public CachedForecast(String metaDataFileName, String uWindFileName, String vWindFileName) {
        this.metaDataFileName = metaDataFileName;
        this.uWindFileName = uWindFileName;
        this.vWindFileName = vWindFileName;
    }

    public String getMetaDataFileName() {
        return metaDataFileName;
    }

    public String getuWindFileName() {
        return uWindFileName;
    }

    public String getvWindFileName() {
        return vWindFileName;
    }
}
