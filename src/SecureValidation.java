public class SecureValidation implements ValidationStrategy {
    @Override
    public boolean validate(String inputPassword, String storedPassword) {
        if (inputPassword == null || storedPassword == null) {
            return false;
        }

        int separator = storedPassword.indexOf(':');
        if (separator <= 0 || separator >= storedPassword.length() - 1) {
            return false;
        }

        String salt = storedPassword.substring(0, separator);
        String storedHash = storedPassword.substring(separator + 1);
        String inputHash = SecureUser.hashPassword(inputPassword, salt);
        return storedHash.equals(inputHash);
    }
}
