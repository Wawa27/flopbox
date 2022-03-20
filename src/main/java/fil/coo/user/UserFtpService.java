package fil.coo.user;

import fil.coo.ftp.FtpServer;
import fil.coo.ftp.FtpServerNotFoundException;

import javax.inject.Singleton;
import java.io.IOException;

/**
 * This service manages the ftp servers associated to a user,
 * The data are stored in the 'users.json' file in the resources
 */
@Singleton
public class UserFtpService {
    private final UserService userService;

    public UserFtpService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Find a server by its alias
     *
     * @param username The username containing the ftp server to find
     * @param alias    The target's ftp server's alias
     * @return The ftp server to find
     * @throws IOException Since users are stored in a file, IOException might be thrown, for example, if the file
     *                     is missing
     */
    public FtpServer findByAlias(String username, String alias) throws IOException {
        User user = this.userService.findByUsername(username);
        return user.getFtpServers().stream()
                .filter(ftpServer -> ftpServer.getAlias().equals(alias) || ftpServer.getHost().equals(alias))
                .findFirst()
                .orElseThrow(() -> new FtpServerNotFoundException(alias));
    }

    /**
     * Remove a ftp server from its alias
     *
     * @param username The username of the user requesting the deletion of the ftp server
     * @param alias    An alias of the target's ftp server
     * @return The deleted ftp server
     * @throws IOException Since users are stored in a file, IOException might be thrown, for example, if the file
     *                     is missing
     */
    public FtpServer delete(String username, String alias) throws IOException {
        User user = userService.findByUsername(username);
        FtpServer ftpServer = findByAlias(username, alias);

        user.getFtpServers().remove(ftpServer);

        userService.save(user);
        return ftpServer;
    }

    /**
     * Replace an ftp server's alias
     *
     * @param username The username containing the ftp server to update
     * @param alias    The target's ftp server's alias
     * @param newAlias The new ftp server's alias
     * @return The updated server
     * @throws IOException Since users are stored in a file, IOException might be thrown, for example, if the file
     *                     is missing
     */
    public FtpServer replaceAlias(String username, String alias, String newAlias) throws IOException {
        User user = userService.findByUsername(username);
        FtpServer ftpServer = user.getFtpServers().get(user.getFtpServers().indexOf(findByAlias(username, alias)));
        ftpServer.setAlias(newAlias);

        userService.save(user);
        return ftpServer;
    }
}
