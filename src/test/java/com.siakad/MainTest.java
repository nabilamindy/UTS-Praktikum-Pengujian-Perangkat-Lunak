package com.siakad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testMainConstructor() {
        Main main = new Main();
        assertNotNull(main);
    }

    @Test
    public void testMainMethodNoException() {
        assertDoesNotThrow(() -> {
            Main.main(new String[]{});
        });
    }

    @Test
    public void testMainMethodWithNullArgs() {
        assertDoesNotThrow(() -> {
            Main.main(null);
        });
    }

    @Test
    public void testMainMethodWithSampleArgs() {
        assertDoesNotThrow(() -> {
            Main.main(new String[]{"arg1", "arg2"});
        });
    }
}
