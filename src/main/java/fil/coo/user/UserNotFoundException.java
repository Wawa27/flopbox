package fil.coo.user;

public class UserNotFoundException extends RuntimeException {
    private final String name;

    public UserNotFoundException(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
