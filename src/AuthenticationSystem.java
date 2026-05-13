// 1. Clase Main - Punto de entrada
public class AuthenticationSystem {
    public static void main(String[] args) {
        System.out.println("=== Sistema de Autenticación ===");
        
        // Crear usuario
        User user = new User("admin", "secret123");
        
        // Configurar estrategia de validación
        Context context = new Context();
        
        // Probar diferentes estrategias
        context.setStrategy(new SimpleValidation());
        boolean result1 = context.executeValidation(user, "secret123");
        
        context.setStrategy(new StrongValidation());
        boolean result2 = context.executeValidation(user, "Secret123!");
        
        System.out.println("Resultado Simple: " + result1);
        System.out.println("Resultado Fuerte: " + result2);
        
        // Generar reporte
        ReportGenerator generator = new ReportGenerator();
        generator.generateReport(user);
    }
}

// 2. Clase User - Modelo de datos
class User {
    private String username;
    private String password;
    private int loginAttempts;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.loginAttempts = 0;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public int getLoginAttempts() {
        return loginAttempts;
    }
    
    public void incrementAttempts() {
        this.loginAttempts++;
    }
    
    public void resetAttempts() {
        this.loginAttempts = 0;
    }
    
    // Método cifrado simple (para ingeniería inversa)
    public String getEncryptedPassword() {
        StringBuilder encrypted = new StringBuilder();
        for (char c : password.toCharArray()) {
            encrypted.append((char)(c + 2));
        }
        return encrypted.toString();
    }
}

// 3. Interface Strategy - Patrón de diseño
interface ValidationStrategy {
    boolean validate(String inputPassword, String storedPassword);
}

// 4. Clase SimpleValidation - Estrategia concreta 1
class SimpleValidation implements ValidationStrategy {
    @Override
    public boolean validate(String inputPassword, String storedPassword) {
        if (inputPassword == null || storedPassword == null) {
            return false;
        }
        return inputPassword.equals(storedPassword);
    }
}

// 5. Clase StrongValidation - Estrategia concreta 2
class StrongValidation implements ValidationStrategy {
    @Override
    public boolean validate(String inputPassword, String storedPassword) {
        if (inputPassword == null || storedPassword == null) {
            return false;
        }
        
        // Validación de longitud mínima
        if (inputPassword.length() < 8) {
            return false;
        }
        
        // Validar que tenga mayúscula, minúscula y número
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        
        for (char c : inputPassword.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        
        return hasUpper && hasLower && hasDigit && inputPassword.equals(storedPassword);
    }
}

// 6. Clase Context - Contexto para Strategy
class Context {
    private ValidationStrategy strategy;
    
    public void setStrategy(ValidationStrategy strategy) {
        this.strategy = strategy;
    }
    
    public boolean executeValidation(User user, String inputPassword) {
        if (strategy == null) {
            throw new IllegalStateException("Estrategia no configurada");
        }
        
        boolean isValid = strategy.validate(inputPassword, user.getPassword());
        
        if (isValid) {
            user.resetAttempts();
            System.out.println("✔ Validación exitosa para: " + user.getUsername());
        } else {
            user.incrementAttempts();
            System.out.println("✘ Fallo de validación. Intentos: " + user.getLoginAttempts());
        }
        
        return isValid;
    }
}

// 7. Clase ReportGenerator - Utilidad adicional
class ReportGenerator {
    public void generateReport(User user) {
        System.out.println("\n=== REPORTE DE SEGURIDAD ===");
        System.out.println("Usuario: " + user.getUsername());
        System.out.println("Intentos fallidos: " + user.getLoginAttempts());
        System.out.println("Password (cifrado): " + user.getEncryptedPassword());
        System.out.println("Longitud password: " + user.getPassword().length());
        System.out.println("==========================");
    }
}