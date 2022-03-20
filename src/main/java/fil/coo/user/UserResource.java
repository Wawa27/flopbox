package fil.coo.user;

import fil.coo.ftp.FtpServer;
import io.quarkus.vertx.web.Body;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import java.io.IOException;
import java.util.List;

@Produces("application/json")
@Consumes("application/json")
@Path("/v1/users/me")
public class UserResource {
    private final JsonWebToken jsonWebToken;
    private final UserService userService;
    private final UserFtpService userFtpService;

    public UserResource(JsonWebToken jsonWebToken, UserService userService, UserFtpService userFtpService) {
        this.jsonWebToken = jsonWebToken;
        this.userService = userService;
        this.userFtpService = userFtpService;
    }

    /**
     * @return A list of servers of the current user
     */
    @GET
    @Path("/servers")
    @RolesAllowed("User")
    public List<FtpServer> getFtpServers() throws IOException {
        return userService.findByUsername(jsonWebToken.getSubject()).getFtpServers();
    }

    /**
     * Add a ftp server to the current user
     *
     * @param ftpServer The ftp server to add
     */
    @POST
    @Path("/servers")
    @RolesAllowed("User")
    public void addFtpServer(FtpServer ftpServer) throws IOException {
        User user = this.userService.findByUsername(jsonWebToken.getSubject());
        user.getFtpServers().add(ftpServer);
        this.userService.save(user);
    }

    /**
     * Remove a ftp server from its alias
     *
     * @param alias An alias of the target's ftp server
     * @return The
     */
    @DELETE
    @Path("/servers/{alias}")
    @RolesAllowed("User")
    public FtpServer delete(@PathParam("alias") String alias) throws IOException {
        return userFtpService.delete(jsonWebToken.getSubject(), alias);
    }

    /**
     * Replace an alias
     *
     * @param alias    The ftp server's alias
     * @param newAlias The new alias
     * @return The updated ftp server
     */
    @PUT
    @Path("/servers/{alias}")
    @RolesAllowed("User")
    public FtpServer replaceAlias(@PathParam("alias") String alias, @Body() String newAlias) throws IOException {
        return userFtpService.replaceAlias(jsonWebToken.getSubject(), alias, newAlias);
    }
}
