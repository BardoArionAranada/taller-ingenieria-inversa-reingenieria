import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class SecureUser extends User {
    private static final int SALT_BYTES = 16;

    private final String salt;
    private final String hashedPassword;

    public SecureUser(String username, String password) {
        super(username, "[PROTECTED]");
        this.salt = generateSalt();
        this.hashedPassword = hashPassword(password, salt);
    }

    private static String generateSalt() {
        byte[] saltBytes = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update((password + salt).getBytes(StandardCharsets.UTF_8));
            byte[] hash = digest.digest();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new IllegalStateException("Error hashing password", e);
        }
    }

    public String getSalt() {
        return salt;
    }

    public String getPasswordHash() {
        return hashedPassword;
    }

    @Override
    public String getPassword() {
        return salt + ":" + hashedPassword;
    }

    @Override
    public String getEncryptedPassword() {
        return hashedPassword;
    }

    public boolean verifyPassword(String inputPassword) {
        if (inputPassword == null) {
            return false;
        }
        return hashedPassword.equals(hashPassword(inputPassword, salt));
    }
}
