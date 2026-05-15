public class SecureAuthenticationDemo {
    public static void main(String[] args) {
        SecureUser secureUser = new SecureUser("admin", "SecurePass123");
        Context context = new Context();

        context.setStrategy(new SecureValidation());

        boolean validPassword = context.executeValidation(secureUser, "SecurePass123");
        boolean invalidPassword = context.executeValidation(secureUser, "wrongPassword");

        System.out.println("Resultado seguro correcto: " + validPassword);
        System.out.println("Resultado seguro incorrecto: " + invalidPassword);

        ReportGenerator generator = new ReportGenerator();
        generator.generateReport(secureUser);
    }
}
