package fil.coo.ftp;

public class FtpFileDto {
    private String filePath;
    private long lastModification;
    private int type;

    public FtpFileDto(String filePath, long lastModification, int type) {
        this.filePath = filePath;
        this.lastModification = lastModification;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getLastModification() {
        return lastModification;
    }

    public void setLastModification(long lastModification) {
        this.lastModification = lastModification;
    }

    @Override
    public String toString() {
        return "FtpFileDto{" +
                "filePath='" + filePath + '\'' +
                ", lastModification=" + lastModification +
                ", type=" + type +
                '}';
    }
}
