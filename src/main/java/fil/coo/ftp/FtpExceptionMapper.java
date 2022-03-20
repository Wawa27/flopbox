package fil.coo.ftp;

import org.jboss.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FtpExceptionMapper implements ExceptionMapper<FtpException> {
    private static final Logger LOG = Logger.getLogger(FtpResource.class);

    @Override
    public Response toResponse(FtpException exception) {
        if (exception instanceof FtpServerNotFoundException) {
            Response.status(Response.Status.NOT_FOUND).build();
        } else if (exception instanceof BadFtpServerCredentialsException) {
            Response.status(Response.Status.fromStatusCode(401)).build();
        }
        LOG.error(exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
