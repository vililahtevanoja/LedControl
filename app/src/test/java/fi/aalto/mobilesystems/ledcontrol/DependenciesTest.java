package fi.aalto.mobilesystems.ledcontrol;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by vili on 14/03/16.
 */
public class DependenciesTest {
    @Test
    public void testCycles() throws IOException {
        JDepend jdepend = new JDepend();
        jdepend.addDirectory("target/classes");

        jdepend.analyze();

        assertThat(jdepend.containsCycles(), is(false));
    }
}
