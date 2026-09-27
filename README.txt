PASSWORD VAULT — JAVA PROJECT

A console-based password vault application built using Java.

FEATURES
- Create and unlock a vault using a master password
- Encrypt saved credentials using AES-GCM encryption
- Add, view, search, edit, and delete credentials
- Generate strong passwords
- Check password strength
- Mask passwords during normal viewing
- Reveal a selected password after confirmation
- Automatically save changes to the encrypted vault file

HOW TO RUN
1. Open the project in IntelliJ IDEA.
2. Run Main.java.
3. On the first run, create a master password.
4. Use the menu to manage credentials.
5. Use option 9 to save and exit.

PROJECT FILES
- Main.java: Runs the menu and handles user input.
- PasswordEntry.java: Represents one saved credential.
- Vault.java: Stores and manages credentials.
- PasswordGenerator.java: Generates secure passwords.
- PasswordStrength.java: Checks whether a password is weak, medium, or strong.
- CryptoService.java: Encrypts and decrypts vault data.
- FileStorage.java: Saves and loads the encrypted vault.

SECURITY NOTE
The vault uses a master password with PBKDF2 key derivation and AES-GCM encryption. Do not share your master password. If it is forgotten, the encrypted vault cannot be recovered.