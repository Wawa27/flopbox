package fil.coo.ftp;

public class FtpException extends RuntimeException {

    public FtpException() {
    }

    public FtpException(String message) {
        super(message);
    }

    public FtpException(String message, Throwable cause) {
        super(message, cause);
    }

    public FtpException(Throwable cause) {
        super(cause);
    }

    public FtpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
