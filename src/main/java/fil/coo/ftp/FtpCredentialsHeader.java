package fil.coo.ftp;

import fil.coo.user.UserFtpService;
import io.vertx.core.http.HttpServerRequest;
import org.apache.commons.net.ftp.FTPClient;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import java.io.IOException;

@RequestScoped
public class FtpCredentialsHeader {
    private final String ftpUsername;
    private final String ftpPassword;
    private UserFtpService userFtpService;
    private FTPClient ftpClient;
    private final String mode;

    public FtpCredentialsHeader(HttpServerRequest httpServerRequest, UserFtpService userFtpService) {
        this.ftpUsername = httpServerRequest.getHeader("Ftp-Username");
        this.ftpPassword = httpServerRequest.getHeader("Ftp-Password");
        this.mode = httpServerRequest.getParam("mode", "passive");
        this.userFtpService = userFtpService;
    }

    /**
     * Connect to the ftp server
     *
     * @param username The user's name trying to connect to the ftp server
     * @param alias    The target's ftp server alias
     * @return A ftp client ready to use, in passive mode
     */
    public FTPClient connect(String username, String alias) throws IOException {
        FtpServer ftpServer = userFtpService.findByAlias(username, alias);
        ftpClient = new FTPClient();
        ftpClient.connect(ftpServer.getHost(), 2121);
        if (!ftpClient.login(ftpUsername, ftpPassword)) {
            throw new BadFtpServerCredentialsException(ftpUsername);
        }
        if ("active".equals(mode)) {
            ftpClient.enterLocalActiveMode();
        } else {
            ftpClient.enterLocalPassiveMode();
        }
        return ftpClient;
    }

    /**
     * Disconnect from the ftp server
     */
    @PreDestroy
    public void disconnect() throws IOException {
        this.ftpClient.disconnect();
    }

    public String getFtpUsername() {
        return ftpUsername;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }
}
