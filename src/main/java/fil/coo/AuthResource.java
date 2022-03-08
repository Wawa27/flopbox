package fil.coo;

import fil.coo.dtos.UserAuthDto;
import fil.coo.models.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.vertx.web.Body;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;
import org.jboss.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
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
import java.util.Optional;

@Consumes("application/json")
@Path("/auth")
public class AuthResource {
    private static final Logger LOG = Logger.getLogger(AuthResource.class);

    @Inject
    UserService userService;

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserAuthDto userAuthDto) throws IOException {
        Optional<User> optionalUser = userService.getAll().stream().filter(user1 -> user1.getUsername().equals(userAuthDto.getUsername())).findFirst();

        if (optionalUser.isEmpty() || !BcryptUtil.matches(userAuthDto.getPassword(), optionalUser.get().getPassword())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        User user = optionalUser.get();

        String token = Jwt.issuer("https://fil.coo/issuer")
                .groups(new HashSet<>(List.of("User")))
                .claim(Claims.sub.name(), "wawa")
                .sign();

        return Response.ok(user).cookie(new NewCookie("jwt", token)).build();
    }
}
