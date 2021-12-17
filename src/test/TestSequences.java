package test;

import main.Sequence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Sequences")
public class TestSequences {
    @Test
    @DisplayName("A number generator returns a cyclic sequence of numbers")
    void testSequence() {
        Sequence gen = new Sequence(new double[]{0.1, 0.5, 1.0});
        assertEquals(.1, gen.next());
        assertEquals(.5, gen.next());
        assertEquals(1.0, gen.next());
        assertEquals(.1, gen.next());
    }
}
