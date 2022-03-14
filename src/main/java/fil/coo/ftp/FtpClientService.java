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
        System.out.println("ftpClient = " + ftpClient.getReplyString());
        ftpClient.connect("localhost", 21);
        System.out.println("ftpClient = " + ftpClient.getReplyString());
        try {
            if (!ftpClient.login("bob", "12345")) {
                throw new BadFtpServerCredentialsException(ftpClient.getReplyString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(ftpClient.getReplyString());
        }

        return ftpClient;
    }

    public void upload(FtpServer ftpServer, String username, String password) throws IOException {
        FTPClient ftpClient = connect(ftpServer, username, password);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
    }

    public List<String> listFiles(FtpServer ftpServer, String username, String password, String path) throws IOException {
        FTPClient ftpClient = connect(ftpServer, username, password);
        ftpClient.enterLocalPassiveMode();
        return Arrays.stream(ftpClient.listFiles(path)).map(FTPFile::getName).collect(Collectors.toList());
    }
}
