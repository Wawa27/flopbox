package fil.coo.ftp;

import java.util.List;

public class FtpServer {
    private String host;
    private List<String> aliases;

    public FtpServer() {
    }

    public FtpServer(String host, List<String> aliases) {
        this.host = host;
        this.aliases = aliases;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
