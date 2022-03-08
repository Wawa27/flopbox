package fil.coo.models;

public class FtpServer {
    private String host;
    private String[] aliases;

    public FtpServer() {
    }

    public FtpServer(String host, String[] aliases) {
        this.host = host;
        this.aliases = aliases;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
