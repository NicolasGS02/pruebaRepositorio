import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions; 
import org.openqa.selenium.support.ui.Select; // Necesario para interactuar con los dropdowns
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Set;
import static org.testng.Assert.assertTrue;

/**
 * Clase de prueba para verificar el funcionamiento de dos formularios (Buzón de Sugerencias
 * y Formulario de Contacto/Preinscripción) en la web de la EPS.
 * Se utilizan esperas explícitas para resolver problemas de sincronización en los clics.
 */
public class FormulariosInteraccionTest { 
    
    private static WebDriver driver;
    private static WebDriverWait wait; 

    /**
     * Configuración inicial antes de que se ejecute *cualquier* prueba en esta clase (@BeforeClass).
     * Inicializa el WebDriver (Firefox) y el objeto WebDriverWait.
     */
    @BeforeClass
    static void setUp() {
        driver = new FirefoxDriver();
        // Inicializar WebDriverWait con un tiempo de espera robusto
        wait = new WebDriverWait(driver, Duration.ofSeconds(15)); 
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    /**
     * Limpieza después de que *todos* los métodos de prueba de esta clase han finalizado (@AfterClass).
     * Cierra la instancia del navegador (WebDriver).
     */
    @AfterClass
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Método reutilizable para esperar la aparición de una nueva ventana de forma dinámica.
     *
     * @param oldWindowHandles Conjunto de handles de las ventanas antes de abrir la nueva.
     * @return El handle de la nueva ventana abierta.
     */
    private static String waitForNewWindow(Set<String> oldWindowHandles) {
        wait.until(ExpectedConditions.numberOfWindowsToBe(oldWindowHandles.size() + 1));
        
        Set<String> allWindowHandles = driver.getWindowHandles();
        allWindowHandles.removeAll(oldWindowHandles);
        
        return allWindowHandles.iterator().next();
    }

    /**
     * Prueba que navega al Buzón de Sugerencias y a otro formulario (Agenda/Contacto)
     * y rellena los campos en ambas ventanas emergentes.
     */
    @Test
    void testInteraccionConMultiplesFormularios() {
        
        // 1. Navegación inicial y configuración
        driver.get("https://www.upo.es/escuela-politecnica-superior/es/");
        driver.manage().window().setSize(new Dimension(1936, 1048));
        String rootWindowHandle = driver.getWindowHandle();

        
        // Clic en el menú de Contacto/Agenda (elemento 9)
        driver.findElement(By.cssSelector(".mainnav-lvl1:nth-child(9) > .mainnav-lvl1")).click();

        // --- 2. Interacción con el Buzón de Sugerencias ---
        
        // A. Preparación para la nueva ventana
        Set<String> oldHandles1 = driver.getWindowHandles();
        
        // B. SOLUCIÓN AL PROBLEMA 1: Espera Explícita para Buzón de sugerencias
        By buzonLocator = By.linkText("Buzón de sugerencias");
        wait.until(ExpectedConditions.elementToBeClickable(buzonLocator)).click();
        
        // C. Cambio de foco y relleno del formulario
        String buzonWindowHandle = waitForNewWindow(oldHandles1);
        driver.switchTo().window(buzonWindowHandle);
        
        // Relleno de campos de Buzón de Sugerencias
        driver.findElement(By.id("irsf-nombre")).sendKeys("AAA");
        driver.findElement(By.id("irsf-apellido1")).sendKeys("VVV");
        driver.findElement(By.id("irsf-apellido2")).sendKeys("AAA");
        
        // Se simplifican los clics innecesarios
        driver.findElement(By.id("irsf-telefono")).sendKeys("123456789");
        driver.findElement(By.id("irsf-email")).sendKeys("correo@correo.es");
        
        // Interacción con Dropdown usando la clase Select (Buena Práctica)
        WebElement dropdownElement = driver.findElement(By.id("irsf-tipo"));
        Select dropdown = new Select(dropdownElement);
        // Seleccionamos por texto visible (la opción larga que usaba el código original)
        dropdown.selectByVisibleText("Felicitación: Manifestación expresa de la satisfacción y/o agradecimiento experimentados por los servicios recibidos");

        // Se elimina el clic redundante en la opción
        
        // Dropdown de Titulación
        new Select(driver.findElement(By.id("irsf-id_titulacion"))).selectByIndex(0);
        
        driver.findElement(By.id("asunto")).sendKeys("Ejemplo TestNG");
        driver.findElement(By.id("mensajeirsf-descripcion")).sendKeys("Ejemplo de Test automatizado para el Buzón de Sugerencias.");
        
        driver.findElement(By.id("irsf-aceptado")).click(); // Clic en el checkbox de aceptación
        
        // D. Verificación y cierre de la ventana del Buzón
        assertTrue(driver.getCurrentUrl().contains("buzon"), 
                   "ERROR: La URL del Buzón de Sugerencias no se abrió correctamente.");
                   
        driver.close();
        driver.switchTo().window(rootWindowHandle); // Volver a la ventana principal

        // --- 3. Interacción con el segundo formulario (Agenda/Contacto) ---

        // Volver a clicar en el menú (necesario si la página se recarga o si es un paso secuencial)
        driver.findElement(By.cssSelector(".navbar-brand > .img-responsive")).click();
        driver.findElement(By.cssSelector(".mainnav-lvl1:nth-child(9) > .mainnav-lvl1")).click();
        
        // A. Preparación para la nueva ventana
        Set<String> oldHandles2 = driver.getWindowHandles();
        
        // B. SOLUCIÓN AL PROBLEMA 2: Espera Explícita para el segundo enlace
        By secondFormLocator = By.cssSelector(".feature-block-brand-second .text-icon");
        wait.until(ExpectedConditions.elementToBeClickable(secondFormLocator)).click();
        
        // C. Cambio de foco y relleno del formulario
        String secondFormWindowHandle = waitForNewWindow(oldHandles2);
        driver.switchTo().window(secondFormWindowHandle);
        
        // Relleno de campos del segundo formulario
        driver.findElement(By.id("dni")).sendKeys("12345678A");
        driver.findElement(By.id("nombre")).sendKeys("AAA BBB");
        driver.findElement(By.id("email")).sendKeys("correo@correo.es");
        driver.findElement(By.id("movil")).sendKeys("123456789");
        
        driver.findElement(By.id("obser")).sendKeys("Segundo formulario rellenado.");

        // D. Verificación y cierre
        String nombreValue = driver.findElement(By.id("nombre")).getAttribute("value");
        assertTrue(nombreValue.equals("AAA BBB"), 
                   "ERROR: El valor introducido en el campo Nombre del segundo formulario no coincide.");
        
        driver.close();
        driver.switchTo().window(rootWindowHandle);
    }
}