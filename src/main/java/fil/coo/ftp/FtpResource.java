package fil.coo.ftp;

import fil.coo.user.User;
import fil.coo.user.UserService;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import java.io.IOException;
import java.util.List;

@Produces("application/json")
@Consumes("application/json")
@Path("/v1/ftps")
public class FtpResource {
    private static final Logger LOG = Logger.getLogger(FtpResource.class);

    private final JsonWebToken jsonWebToken;
    private final UserService userService;
    private final FtpService ftpService;
    private final FtpClientService ftpClientService;

    public FtpResource(
            JsonWebToken jsonWebToken,
            UserService userService,
            FtpService ftpService,
            FtpClientService ftpClientService) {
        this.jsonWebToken = jsonWebToken;
        this.userService = userService;
        this.ftpService = ftpService;
        this.ftpClientService = ftpClientService;
    }

    @GET
    @Path("/")
    @RolesAllowed("User")
    public List<FtpServer> getFtpServers() throws IOException {
        return userService.findByUsername(jsonWebToken.getSubject()).getFtpServers();
    }

    @POST
    @RolesAllowed("User")
    public void addFtpServer(FtpServer ftpServer) throws IOException {
        User user = this.userService.findByUsername(jsonWebToken.getSubject());
        user.getFtpServers().add(ftpServer);
        this.userService.save(user);
    }

    @DELETE
    @Path("/{alias}")
    @RolesAllowed("User")
    public FtpServer delete(@PathParam("alias") String alias) throws IOException {
        return ftpService.delete(jsonWebToken.getSubject(), alias);
    }

    @GET
    @Path("/{alias}")
    @RolesAllowed("User")
    public FtpServer get(@PathParam("alias") String alias) throws IOException {
        return ftpService.findByAlias(jsonWebToken.getSubject(), alias);
    }

    @GET
    @Path("/{alias}/files")
    @RolesAllowed("User")
    public List<String> get(
            @HeaderParam("Ftp-Username") String ftpUsername,
            @HeaderParam("Ftp-Password") String ftpPassword,
            @PathParam("alias") String alias) throws IOException {
        FtpServer ftpServer = ftpService.findByAlias(jsonWebToken.getSubject(), alias);
        return ftpClientService.listFiles(ftpServer, ftpUsername, ftpPassword);
    }

    @POST
    @Path("/{alias}/connect")
    @RolesAllowed("User")
    public void connect(
            @HeaderParam("Ftp-Username") String ftpUsername,
            @HeaderParam("Ftp-Password") String ftpPassword,
            @PathParam("alias") String alias
    ) throws IOException {
        FtpServer ftpServer = ftpService.findByAlias(jsonWebToken.getSubject(), alias);
    }
}
