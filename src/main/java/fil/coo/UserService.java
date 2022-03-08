package fil.coo;

import com.fasterxml.jackson.databind.ObjectMapper;
import fil.coo.models.FtpServer;
import fil.coo.models.User;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserService {

    public List<User> getAll() throws IOException {
        InputStream usersResource = this.getClass().getClassLoader().getResourceAsStream("users.json");
        String contentJson = new String(usersResource.readAllBytes());
        usersResource.close();
        return new ArrayList<>(Arrays.asList(new ObjectMapper().readValue(contentJson, User[].class)));
    }

    public Optional<User> findByUsername(String username) throws IOException {
        List<User> users = this.getAll();
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }
}
