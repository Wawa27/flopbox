package fil.coo.ftp;

public class BadFtpServerCredentialsException extends FtpException {

    public BadFtpServerCredentialsException(String username) {
        super("Wrong FTP credentials (" + username + ")");
    }
}
