package objectcontract;

/**
 * Adapter for different asserting conventions.
 *
 * @author Oliver Gondža (ogondza@gmail.com)
 */
public interface EqualsHashCodeAsserter {

    /**
     * Assert that <code>instance.equals(instance)</code> is <code>true</code>.
     *
     * @param instance An instance to examine
     * @param message Description message
     */
    void checkEqualsReflexivity(final Object instance, final String message);

    /**
     * Assert that <code>instance.equals(null)</code> is <code>false</code>.
     *
     * @param instance An instance to examine
     * @param message Description message
     */
    void checkEqualsFailsForNull(final Object instance, final String message);

    /**
     * Assert that <code>lhs.equals(rhs)</code> is <code>true</code>.
     *
     * @param lhs Left hand side argument
     * @param rhs Right hand side argument
     * @param message Description message
     */
    void checkEquals(final Object lhs, final Object rhs, final String message);

    /**
     * Assert that <code>lhs.hashCode() == rhs.hashCode()</code> is <code>true</code>.
     *
     * @param lhs Left hand side argument
     * @param rhs Right hand side argument
     * @param message Description message
     */
    void checkHashCodeEquals(final Object lhs, final Object rhs, final String message);

    /**
     * Assert that <code>lhs.equals(rhs)</code> is <code>false</code>.
     *
     * @param lhs Left hand side argument
     * @param rhs Right hand side argument
     * @param message Description message
     */
    void checkDoesNotEqual(final Object lhs, final Object rhs, final String message);

    /**
     * Assert that <code>lhs.hashCode() != rhs.hashCode()</code> is <code>true</code>.
     *
     * @param lhs Left hand side argument
     * @param rhs Right hand side argument
     * @param message Description message
     */
    void checkHashCodeDoesNotEqual(final Object lhs, final Object rhs, final String message);
}
