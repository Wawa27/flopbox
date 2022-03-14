package fil.coo.ftp;

public class FtpServerNotFoundException extends FtpException {
    private final String alias;

    public FtpServerNotFoundException(String alias) {
        super();
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }
}
