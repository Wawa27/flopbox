package fil.coo.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class User {
    private List<FtpServer> ftpServers;
    private String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    public User() {
    }

    public User(List<FtpServer> ftpServers, String username, String password) {
        this.ftpServers = ftpServers;
        this.username = username;
        this.password = password;
    }

    public List<FtpServer> getFtpServers() {
        return ftpServers;
    }

    public void setFtpServers(List<FtpServer> ftpServers) {
        this.ftpServers = ftpServers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
