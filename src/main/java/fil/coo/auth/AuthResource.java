package fil.coo.auth;

import fil.coo.user.UserService;
import fil.coo.user.User;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;
import org.jboss.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@Consumes("application/json")
@Path("/v1/auth")
public class AuthResource {
    private static final Logger LOG = Logger.getLogger(AuthResource.class);

    private final UserService userService;

    public AuthResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(AuthDto userAuthDto) throws IOException {
        User user = userService.findByUsername(userAuthDto.getUsername());

        String token = Jwt.issuer("http://coo.com")
                .subject(userAuthDto.getUsername())
                .groups(new HashSet<>(List.of("User")))
                .claim(Claims.sub.name(), "wawa")
                .sign();

        return Response.ok(user).cookie(new NewCookie("jwt", token)).build();
    }
}
