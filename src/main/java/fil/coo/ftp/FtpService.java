package fil.coo.ftp;

import fil.coo.ftp.FtpServer;
import fil.coo.user.User;
import fil.coo.user.UserService;

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class FtpService {
    private final UserService userService;

    public FtpService(UserService userService) {
        this.userService = userService;
    }

    public FtpServer findByAlias(String username, String alias) throws IOException {
        User user = this.userService.findByUsername(username);
        return user.getFtpServers().stream()
                .filter(ftpServer -> ftpServer.getAliases().contains(alias) || ftpServer.getHost().equals(alias))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ftp server not found with alias " + alias));
    }

    public FtpServer delete(String username, String alias) throws IOException {
        User user = userService.findByUsername(username);
        FtpServer ftpServer = findByAlias(username, alias);

        user.getFtpServers().remove(ftpServer);
        userService.save(user);

        return ftpServer;
    }
}
