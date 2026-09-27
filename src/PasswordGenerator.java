import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*_-+=?";
    private static final String ALL_CHARACTERS =
            UPPERCASE + LOWERCASE + NUMBERS + SYMBOLS;

    private static final SecureRandom random = new SecureRandom();

    public static String generate(int length) {
        if (length < 8) {
            length = 8;
        }

        StringBuilder password = new StringBuilder();

        password.append(randomCharacter(UPPERCASE));
        password.append(randomCharacter(LOWERCASE));
        password.append(randomCharacter(NUMBERS));
        password.append(randomCharacter(SYMBOLS));

        for (int i = 4; i < length; i++) {
            password.append(randomCharacter(ALL_CHARACTERS));
        }

        return shuffle(password.toString());
    }

    private static char randomCharacter(String characters) {
        return characters.charAt(random.nextInt(characters.length()));
    }

    private static String shuffle(String text) {
        char[] characters = text.toCharArray();

        for (int i = characters.length - 1; i > 0; i--) {
            int randomIndex = random.nextInt(i + 1);

            char temporary = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temporary;
        }

        return new String(characters);
    }
}
