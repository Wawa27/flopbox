package fil.coo.ftp;

import io.quarkus.vertx.web.Body;
import org.apache.commons.net.ftp.FTPClient;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Produces("application/json")
@Path("/v1/ftps")
public class FtpResource {
    private final JsonWebToken jsonWebToken;
    private final FtpClientService ftpClientService;
    private final FtpCredentialsHeader ftpCredentialsHeader;

    public FtpResource(
            JsonWebToken jsonWebToken,
            FtpClientService ftpClientService,
            FtpCredentialsHeader ftpCredentialsHeader) {
        this.jsonWebToken = jsonWebToken;
        this.ftpClientService = ftpClientService;
        this.ftpCredentialsHeader = ftpCredentialsHeader;
    }

    @GET
    @Path("/{alias}")
    @RolesAllowed("User")
    @Produces("application/json")
    public Response listFiles(@PathParam("alias") String alias) throws IOException {
        FTPClient ftpClient = ftpCredentialsHeader.connect(jsonWebToken.getSubject(), alias);
        return Response.ok(ftpClientService.getFilenames(ftpClient, "/")).build();
    }

    @GET
    @Path("/{alias}/{path:.*}")
    @RolesAllowed("User")
    public Response download(
            @PathParam("alias") String alias,
            @PathParam("path") String path,
            @QueryParam("action") String action,
            @QueryParam("recursive") boolean recursive) throws IOException {
        FTPClient ftpClient = ftpCredentialsHeader.connect(jsonWebToken.getSubject(), alias);

        if ("download".equals(action)) {
            return Response.ok(ftpClientService.download(ftpClient, path)).build();
        } else {
            return Response.ok(ftpClientService.getFilenames(ftpClient, path)).build();
        }
    }

    @POST
    @Path("/{alias}/{path:.*}")
    @RolesAllowed("User")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean renameOrCreateDirectory(
            @PathParam("path") String path,
            @PathParam("alias") String alias,
            @Body() String newPath) throws IOException {
        FTPClient ftpClient = ftpCredentialsHeader.connect(jsonWebToken.getSubject(), alias);
        if (newPath != null) {
            newPath = newPath.replaceAll("\"", "");
            return ftpClientService.rename(ftpClient, path, newPath);
        } else {
            return ftpClientService.makeDirectory(ftpClient, path);
        }
    }

    @PUT
    @Path("/{alias}/{path:.*}")
    @RolesAllowed("User")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public boolean upload(
            MultipartFormDataInput input,
            @PathParam("alias") String alias,
            @PathParam("path") String path) throws IOException {
        FTPClient ftpClient = ftpCredentialsHeader.connect(jsonWebToken.getSubject(), alias);

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");

        for (InputPart inputPart : inputParts) {
            MultivaluedMap<String, String> header = inputPart.getHeaders();
            String fileName = getFileName(header);
            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            return ftpClientService.uploadFileOrZip(ftpClient, fileName, inputStream, path);
        }

        return true;
    }

    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }


    @DELETE
    @Path("/{alias}/{path:.*}")
    @RolesAllowed("User")
    public boolean deleteFile(
            @PathParam("alias") String alias,
            @PathParam("path") String path) throws IOException {
        FTPClient ftpClient = ftpCredentialsHeader.connect(jsonWebToken.getSubject(), alias);
        return ftpClientService.deleteFile(ftpClient, path);
    }
}
