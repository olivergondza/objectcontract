package objectcontract.testng;

import static org.testng.Assert.*;
import objectcontract.EqualsHashCodeAsserter;

/**
 * Asserter implementation using TestNG assertions.
 *
 * @author Oliver Gondža (ogondza@gmail.com)
 */
public final class TestNGEqualsHashCodeAsserter implements EqualsHashCodeAsserter {

    public void checkEqualsReflexivity(Object instance, String message) {

        assertTrue(instance.equals(instance), message);
    }

    public void checkEqualsFailsForNull(Object instance, String message) {

        assertFalse(instance.equals(null), message);
    }

    public void checkEquals(final Object lhs, final Object rhs, final String message) {

        assertEquals(lhs, rhs, message);
    }

    public void checkHashCodeEquals(final Object lhs, final Object rhs, final String message) {

        assertEquals(lhs.hashCode(), rhs.hashCode(), message);
    }

    public void checkDoesNotEqual(final Object lhs, final Object rhs, final String message) {

        assertFalse(lhs.equals(rhs), message);
    }

    public void checkHashCodeDoesNotEqual(final Object lhs, final Object rhs, final String message) {

        assertFalse(lhs.hashCode() == rhs.hashCode(), message);
    }
}
