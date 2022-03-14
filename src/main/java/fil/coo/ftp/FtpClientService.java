package fil.coo.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class FtpClientService {

    public FtpClientService() {
    }

    private FTPClient connect(FtpServer ftpServer, String username, String password) throws IOException {
        FTPClient ftpClient = new FTPClient();
        if (!ftpClient.login(username, password)) {
            throw new BadFtpServerCredentialsException(ftpServer.getHost());
        }

        System.out.println("Connected to " + ftpServer.getHost() + ".");
        System.out.print(ftpClient.getReplyString());

        return ftpClient;
    }

    public void upload(FtpServer ftpServer, String username, String password) throws IOException {
        FTPClient ftpClient = connect(ftpServer, username, password);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
    }

    public List<String> listFiles(FtpServer ftpServer, String username, String password) throws IOException {
        FTPClient ftpClient = connect(ftpServer, username, password);
        ftpClient.enterLocalPassiveMode();
        return Arrays.stream(ftpClient.listFiles()).map(FTPFile::getName).collect(Collectors.toList());
    }
}
