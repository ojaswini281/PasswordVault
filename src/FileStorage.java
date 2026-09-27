import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private static final Path VAULT_FILE = Path.of("data", "vault.dat");

    public static boolean vaultExists() {
        return Files.exists(VAULT_FILE);
    }

    public static boolean save(List<PasswordEntry> entries, char[] masterPassword) {
        try {
            Files.createDirectories(VAULT_FILE.getParent());

            byte[] plainData = serialize(entries);
            byte[] salt = CryptoService.generateSalt();
            byte[] iv = CryptoService.generateIv();

            SecretKey key = CryptoService.deriveKey(masterPassword, salt);
            byte[] encryptedData = CryptoService.encrypt(plainData, key, iv);

            try (DataOutputStream output = new DataOutputStream(
                    Files.newOutputStream(VAULT_FILE))) {
                output.writeInt(salt.length);
                output.write(salt);
                output.writeInt(iv.length);
                output.write(iv);
                output.write(encryptedData);
            }

            return true;
        } catch (IOException | GeneralSecurityException e) {
            System.out.println("Could not encrypt and save the vault.");
            return false;
        }
    }

    public static List<PasswordEntry> load(char[] masterPassword) {
        try (DataInputStream input = new DataInputStream(
                Files.newInputStream(VAULT_FILE))) {

            int saltLength = input.readInt();
            if (saltLength < 16 || saltLength > 64) {
                throw new IOException("Invalid vault file.");
            }
            byte[] salt = input.readNBytes(saltLength);

            int ivLength = input.readInt();
            if (ivLength != 12) {
                throw new IOException("Invalid vault file.");
            }
            byte[] iv = input.readNBytes(ivLength);

            byte[] encryptedData = input.readAllBytes();

            SecretKey key = CryptoService.deriveKey(masterPassword, salt);
            byte[] plainData = CryptoService.decrypt(encryptedData, key, iv);

            return deserialize(plainData);
        } catch (IOException | GeneralSecurityException | ClassNotFoundException e) {
            return null;
        }
    }

    private static byte[] serialize(List<PasswordEntry> entries) throws IOException {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream output = new ObjectOutputStream(bytes)) {

            output.writeObject(entries);
            return bytes.toByteArray();
        }
    }

    @SuppressWarnings("unchecked")
    private static List<PasswordEntry> deserialize(byte[] data)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(
                new ByteArrayInputStream(data))) {

            return (List<PasswordEntry>) input.readObject();
        }
    }
}
