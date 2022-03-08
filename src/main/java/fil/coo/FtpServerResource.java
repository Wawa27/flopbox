package fil.coo;

import com.fasterxml.jackson.databind.ObjectMapper;
import fil.coo.models.FtpServer;
import fil.coo.models.User;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.resource.spi.work.SecurityContext;
import javax.ws.rs.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Produces("application/json")
@Consumes("application/json")
@Path("/ftp-servers")
public class FtpServerResource {
    private static final Logger LOG = Logger.getLogger(FtpServerResource.class);

    @Inject
    JsonWebToken jsonWebToken;

    @Inject
    UserService userService;

    @GET
    public List<FtpServer> getFtpServers() throws IOException {
        return this.userService.getAll().get(0).getFtpServers();
    }

    @POST
    @RolesAllowed("User")
    public void addFtpServer(FtpServer ftpServer) throws IOException {
        List<FtpServer> ftpServers = this.userService.getAll().get(0).getFtpServers();
        ftpServers.add(ftpServer);

        Optional<User> user = this.userService.findByUsername(jsonWebToken.getSubject());

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(this.getClass().getClassLoader().getResource("users.json").toURI()))) {
            bufferedWriter.write(new ObjectMapper().writeValueAsString(ftpServers));
            bufferedWriter.flush();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
