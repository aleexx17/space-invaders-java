/*
 * This source file was generated by the Gradle 'init' task
 */
package aleexx17.spaceinvaders;

import aleexx17.spaceinvaders.main.App;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }
}
