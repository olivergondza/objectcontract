package objectcontract;

public class DefaultEqualsHashCodeAsserter implements EqualsHashCodeAsserter {

    private void assertTrue(final boolean condition, final String message) {

        if (!condition) throw new AssertionError (message);
    }

    public void checkEqualsReflexivity(Object instance, String message) {

        assertTrue(instance.equals(instance), message);
    }

    public void checkEqualsFailsForNull(Object instance, String message) {

        assertTrue(!instance.equals(null), message);
    }

    public void checkEquals(Object lhs, Object rhs, String message) {

        assertTrue(lhs.equals(rhs), message);
    }

    public void checkHashCodeEquals(Object lhs, Object rhs, String message) {

        assertTrue(lhs.hashCode() == rhs.hashCode(), message);
    }

    public void checkDoesNotEqual(Object lhs, Object rhs, String message) {

        assertTrue(!lhs.equals(rhs), message);
    }

    public void checkHashCodeDoesNotEqual(Object lhs, Object rhs, String message) {

        assertTrue(lhs.hashCode() != rhs.hashCode(), message);
    }

}
