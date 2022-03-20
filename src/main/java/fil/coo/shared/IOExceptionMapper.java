package fil.coo.shared;

import fil.coo.ftp.FtpResource;
import org.jboss.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class IOExceptionMapper implements ExceptionMapper<IOException> {
    private static final Logger LOG = Logger.getLogger(FtpResource.class);

    @Override
    public Response toResponse(IOException exception) {
        LOG.error("An IO exception occurred, this might certainly be caused by the users.json file missing");
        LOG.error(exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
