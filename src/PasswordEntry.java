import java.io.Serializable;

public class PasswordEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private String serviceName;
    private String username;
    private String password;
    private String notes;

    public PasswordEntry(String serviceName, String username,
                         String password, String notes) {
        this.serviceName = serviceName;
        this.username = username;
        this.password = password;
        this.notes = notes;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNotes() {
        return notes;
    }
    public String getMaskedPassword() {
        return "*".repeat(password.length());
    }

    @Override
    public String toString() {
        return "Service: " + serviceName
                + "\nUsername: " + username
                + "\nPassword: " + getMaskedPassword()
                + "\nNotes: " + notes;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
