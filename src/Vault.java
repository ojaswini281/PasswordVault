import java.util.ArrayList;
import java.util.List;

public class Vault {
    private final List<PasswordEntry> entries;

    public Vault() {
        entries = new ArrayList<>();
    }

    public void addEntry(PasswordEntry entry) {
        entries.add(entry);
    }

    public List<PasswordEntry> getEntries() {
        return entries;
    }

    public void displayAllEntries() {
        if (entries.isEmpty()) {
            System.out.println("Your vault is empty.");
            return;
        }

        for (int i = 0; i < entries.size(); i++) {
            System.out.println("\nEntry #" + (i + 1));
            System.out.println(entries.get(i));
        }
    }

    public PasswordEntry searchByServiceName(String serviceName) {
        for (PasswordEntry entry : entries) {
            if (entry.getServiceName().equalsIgnoreCase(serviceName)) {
                return entry;
            }
        }
        return null;
    }

    public boolean deleteEntry(int index) {
        if (index < 0 || index >= entries.size()) {
            return false;
        }

        entries.remove(index);
        return true;
    }
    public void loadEntries(List<PasswordEntry> savedEntries) {
        entries.clear();
        entries.addAll(savedEntries);
    }
}