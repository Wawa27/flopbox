package fil.coo.dtos;

import fil.coo.models.FtpServer;
import fil.coo.models.User;

import java.util.List;

public class UserGetDto extends User {

    public UserGetDto() {
    }

    public UserGetDto(List<FtpServer> ftpServers, String username) {
        super(ftpServers, username, "");
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public void setPassword(String password) {

    }
}
