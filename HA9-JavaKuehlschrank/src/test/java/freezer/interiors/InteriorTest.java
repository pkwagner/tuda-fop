package freezer.interiors;

import freezer.Freezer;

import org.junit.Test;

import java.util.stream.Stream;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Interior tests
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class InteriorTest {

    @Test
    public void getAvailableInteriorsTest() throws Exception {
        //inner volume = 1
        Freezer testFreezer = new Freezer(1, 1, 1, 0);
        assertTrue(containsClass(Interior.getAvailableInteriors(testFreezer), ArcticSpecial.class));

        //> 1
        testFreezer = new Freezer(5, 3, 2, 0.005);
        assertTrue(containsClass(Interior.getAvailableInteriors(testFreezer), ArcticSpecial.class));

        //< 1
        testFreezer = new Freezer(1, 1, 1, 0.005);
        assertFalse(containsClass(Interior.getAvailableInteriors(testFreezer), ArcticSpecial.class));
    }

    private boolean containsClass(Object[] components, Class<?> targetClazz) {
        return Stream.of(components)
                .filter(Objects::nonNull)
                .map(Object::getClass)
                .anyMatch(clazz -> clazz.equals(targetClazz));
    }
}