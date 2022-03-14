package fil.coo.ftp;

public class BadFtpServerCredentialsException extends FtpException {

    public BadFtpServerCredentialsException(String host) {
        super("Bad password for host : " + host);
    }
}
