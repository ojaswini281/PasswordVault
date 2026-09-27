//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Vault vault = new Vault();
        char[] masterPassword;

        if (FileStorage.vaultExists()) {
            masterPassword = unlockVault(scanner, vault);

            if (masterPassword == null) {
                scanner.close();
                return;
            }
        } else {
            masterPassword = createMasterPassword(scanner);
            System.out.println("New vault created. Save it when you exit.");
        }

        boolean running = true;

        while (running) {
            System.out.println("\n--- PASSWORD VAULT ---");
            System.out.println("1. Add credential");
            System.out.println("2. View all credentials");
            System.out.println("3. Search by service");
            System.out.println("4. Delete credential");
            System.out.println("5. Edit credential");
            System.out.println("6. Reveal password");
            System.out.println("7. Generate secure password");
            System.out.println("8. Save vault");
            System.out.println("9. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addCredential(scanner, vault, masterPassword);
                    break;
                case "2":
                    vault.displayAllEntries();
                    break;
                case "3":
                    searchCredential(scanner, vault);
                    break;
                case "4":
                    deleteCredential(scanner, vault, masterPassword);
                    break;
                case "5":
                    editCredential(scanner, vault, masterPassword);
                    break;
                case "6":
                    revealPassword(scanner, vault);
                    break;
                case "7":
                    generatePassword(scanner);
                    break;
                case "8":
                    if (FileStorage.save(vault.getEntries(), masterPassword)) {
                        System.out.println("Vault saved successfully.");
                    }
                    break;
                case "9":
                    FileStorage.save(vault.getEntries(), masterPassword);
                    running = false;
                    System.out.println("Vault saved and closed. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1 to 9.");
            }
        }

        scanner.close();
    }

    private static void addCredential(Scanner scanner, Vault vault, char[] masterPassword) {
        System.out.print("Service name: ");
        String serviceName = scanner.nextLine();

        System.out.print("Username or email: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Notes (optional): ");
        String notes = scanner.nextLine();
        if (serviceName.isBlank() || username.isBlank() || password.isBlank()) {
            System.out.println("Service name, username, and password cannot be empty.");
            return;
        }
        System.out.println("Password strength: " + PasswordStrength.check(password));

        PasswordEntry entry = new PasswordEntry(
                serviceName, username, password, notes
        );

        vault.addEntry(entry);
        FileStorage.save(vault.getEntries(), masterPassword);
        System.out.println("Credential added successfully.");
    }

    private static void searchCredential(Scanner scanner, Vault vault) {
        System.out.print("Enter service name to search: ");
        String serviceName = scanner.nextLine();

        PasswordEntry entry = vault.searchByServiceName(serviceName);

        if (entry == null) {
            System.out.println("No credential found for " + serviceName + ".");
        } else {
            System.out.println("\nCredential found:");
            System.out.println(entry);
        }
    }

    private static void deleteCredential(Scanner scanner, Vault vault, char[] masterPassword) {
        vault.displayAllEntries();

        if (vault.getEntries().isEmpty()) {
            FileStorage.save(vault.getEntries(), masterPassword);
            return;
        }

        System.out.print("\nEnter entry number to delete: ");

        try {
            int entryNumber = Integer.parseInt(scanner.nextLine());
            boolean deleted = vault.deleteEntry(entryNumber - 1);

            if (deleted) {
                System.out.println("Credential deleted.");
            } else {
                System.out.println("That entry number does not exist.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
    private static void generatePassword(Scanner scanner) {
        System.out.print("Password length (minimum 8): ");

        try {
            int length = Integer.parseInt(scanner.nextLine());
            String password = PasswordGenerator.generate(length);

            System.out.println("Generated password: " + password);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
    private static char[] createMasterPassword(Scanner scanner) {
        while (true) {
            System.out.print("Create a master password: ");
            String firstPassword = scanner.nextLine();

            System.out.print("Confirm master password: ");
            String secondPassword = scanner.nextLine();

            if (firstPassword.length() < 8) {
                System.out.println("Use at least 8 characters.");
            } else if (!firstPassword.equals(secondPassword)) {
                System.out.println("Passwords do not match. Try again.");
            } else {
                return firstPassword.toCharArray();
            }
        }
    }

    private static char[] unlockVault(Scanner scanner, Vault vault) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.print("Enter master password: ");
            char[] masterPassword = scanner.nextLine().toCharArray();

            var savedEntries = FileStorage.load(masterPassword);

            if (savedEntries != null) {
                vault.loadEntries(savedEntries);
                System.out.println("Vault unlocked.");
                return masterPassword;
            }

            System.out.println("Incorrect password or unreadable vault.");
        }

        System.out.println("Too many unsuccessful attempts. Vault closed.");
        return null;
    }
    private static void editCredential(Scanner scanner, Vault vault, char[] masterPassword) {
        vault.displayAllEntries();

        if (vault.getEntries().isEmpty()) {
            return;
        }

        System.out.print("\nEnter entry number to edit: ");

        try {
            int entryNumber = Integer.parseInt(scanner.nextLine());
            int index = entryNumber - 1;

            if (index < 0 || index >= vault.getEntries().size()) {
                System.out.println("That entry number does not exist.");
                return;
            }

            PasswordEntry entry = vault.getEntries().get(index);

            System.out.println("Leave a field blank to keep its current value.");

            System.out.print("New service name: ");
            String serviceName = scanner.nextLine();

            System.out.print("New username or email: ");
            String username = scanner.nextLine();

            System.out.print("New password: ");
            String password = scanner.nextLine();

            System.out.print("New notes: ");
            String notes = scanner.nextLine();

            if (!serviceName.isBlank()) {
                entry.setServiceName(serviceName);
            }
            if (!username.isBlank()) {
                entry.setUsername(username);
            }
            if (!password.isBlank()) {
                entry.setPassword(password);
            }
            if (!notes.isBlank()) {
                entry.setNotes(notes);
            }
            FileStorage.save(vault.getEntries(), masterPassword);
            System.out.println("Credential updated successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
    private static void revealPassword(Scanner scanner, Vault vault) {
        vault.displayAllEntries();

        if (vault.getEntries().isEmpty()) {
            return;
        }

        System.out.print("\nEnter entry number: ");

        try {
            int entryNumber = Integer.parseInt(scanner.nextLine());
            int index = entryNumber - 1;

            if (index < 0 || index >= vault.getEntries().size()) {
                System.out.println("That entry number does not exist.");
                return;
            }

            System.out.print("Type SHOW to reveal this password: ");
            String confirmation = scanner.nextLine();

            if (confirmation.equals("SHOW")) {
                PasswordEntry entry = vault.getEntries().get(index);
                System.out.println("Password: " + entry.getPassword());
            } else {
                System.out.println("Password was not revealed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }

    }
}