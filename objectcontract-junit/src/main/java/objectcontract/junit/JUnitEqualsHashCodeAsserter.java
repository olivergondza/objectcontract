package objectcontract.junit;

import static org.junit.Assert.*;
import objectcontract.EqualsHashCodeAsserter;

/**
 * Asserter implementation using JUnit assertions.
 *
 * @author Oliver Gondža (ogondza@gmail.com)
 */
public final class JUnitEqualsHashCodeAsserter implements EqualsHashCodeAsserter {

    public void checkEqualsReflexivity(Object instance, String message) {

        assertTrue(message, instance.equals(instance));
    }

    public void checkEqualsFailsForNull(Object instance, String message) {

        assertFalse(message, instance.equals(null));
    }

    public void checkEquals(Object lhs, Object rhs, String message) {

        assertEquals(message, lhs, rhs);
    }

    public void checkHashCodeEquals(Object lhs, Object rhs, String message) {

        assertEquals(message, lhs.hashCode(), rhs.hashCode());
    }

    public void checkDoesNotEqual(Object lhs, Object rhs, String message) {

        assertFalse(message, lhs.equals(rhs));
    }

    public void checkHashCodeDoesNotEqual(Object lhs, Object rhs, String message) {

        assertFalse(message, lhs.hashCode() == rhs.hashCode());
    }
}
