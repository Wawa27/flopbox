package fil.coo.ftp;

public class FtpServer {
    private String host;
    private String alias;

    public FtpServer() {
    }

    public FtpServer(String host, String alias) {
        this.host = host;
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
