package fil.coo.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import fil.coo.user.UserNotFoundException;
import fil.coo.user.User;

import javax.inject.Singleton;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class UserService {

    public List<User> getAll() throws IOException {
        InputStream usersResource = this.getClass().getClassLoader().getResourceAsStream("users.json");
        String contentJson = new String(usersResource.readAllBytes());
        usersResource.close();
        return new ArrayList<>(Arrays.asList(new ObjectMapper().readValue(contentJson, User[].class)));
    }

    public User findByUsername(String username) throws IOException, UserNotFoundException {
        List<User> users = this.getAll();
        return users.stream().filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public void save(User newUser) throws IOException {
        User userToUpdate = findByUsername(newUser.getUsername());
        userToUpdate.setFtpServers(newUser.getFtpServers());
        userToUpdate.setPassword(newUser.getPassword());

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(this.getClass().getClassLoader().getResource("users.json").toURI()))) {
            bufferedWriter.write(new ObjectMapper().writeValueAsString(userToUpdate));
            bufferedWriter.flush();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
