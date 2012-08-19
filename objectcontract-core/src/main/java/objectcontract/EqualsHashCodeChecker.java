package objectcontract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to enforce contract of equals and hashCode.
 *
 * <p>Class checks for reflexivity, symmetricity, transitivity and unequality to
 * null and Object instance of <code>equals</code> method. Additionally,
 * verifies that two equal instances provides same <code>hashCode</code> and
 * optionally that unequal instances provide different <code>hashCode</code>.</p>
 *
 * <p>Class is configured by a number of groups containing instances that are
 * supposed to be equal to each other and unequal to instances of other groups.
 * There has to be nonzero number of nonempty equality groups consisting of
 * distinct instances different from <code>null</code>.</p>
 *
 * <ul>
 *   <li>Ensures that every instance is equal to itself (<code>a.equals(a)</code>).</li>
 *   <li>Ensures that no instance is equal to <code>null</code> (<code>!a.equals(null)</code>).</li>
 *   <li>Ensures that no instance is equal to <code>new Object()</code>
 *   (<code>!a.equals(new Object())</code>).</li>
 * </ul>
 * <ul>
 *   <li>Ensures that every instance is equal to all other instances in the same
 *   group (<code>a.equals(b) == b.equals(a)</code>).</li>
 *   <li>Ensures that every instance produces hashCode equal to hashCode of all
 *   other instances in the same group (<code>a.hashCode() == b.hashCode()</code>).</li>
 * </ul>
 * <ul>
 *   <li>Ensures that none instance is equal to any instance of any other group
 *   (<code>a.equals(b) == b.equals(a)</code>).</li>
 *   <li>Ensures that none instance produces hashCode equal to hashCode of any
 *   instance of any other group (<code>a.hashCode() != b.hashCode ()</code>
 *   provided <code>a</code> and <code>b</code> are not equal).</li>
 * </ul>
 *
 * @author Oliver Gondža (ogondza@gmail.com)
 * @see EqualsHashCodeAsserter
 */
public final class EqualsHashCodeChecker {

    private static final String PREFIX = "Failed asserting that ";

    private final EqualsHashCodeAsserter asserter;
    private final Object[][] groups;
    private final boolean allowHashCodeCollision;

    /**
     * Helper class to facilitate Checker configuration
     *
     * @author Oliver Gondža (ogondza@gmail.com)
     */
    public static final class Builder {

        final EqualsHashCodeAsserter asserter;
        List<List<Object>> groups = new ArrayList<List<Object>>();
        boolean allowHashCodeCollision = false;

        Builder(final EqualsHashCodeAsserter asserter) {

            if (asserter == null) throw new NullPointerException(
                    "No asserter provided"
            );

            this.asserter = asserter;
        }

        /**
         * Allow instances from different equality groups to have conflicting
         * hash code.
         *
         * @return this
         */
        public Builder allowHashCodeCollision() {

            allowHashCodeCollision = true;
            return this;
        }

        /**
         * Set equality group.
         *
         * @param group A group to set.
         * @return this
         */
        public Builder setGroup(final Object... group) {

            groups.add(validateGroup(group));
            return this;
        }

        /**
         * Set several equality groups.
         *
         * @param groups Groups to set
         * @return this
         */
        public Builder setGroups(final Object[]... groups) {

            for ( final Object[] groupCandidate: groups ) {

                this.setGroup(groupCandidate);
            }

            return this;
        }

        private List<Object> validateGroup(final Object[] groupCandidate) {

            final int groupNumber = groups.size();

            if (groupCandidate.length == 0) throw new IllegalStateException(
                    "Group " + groupNumber + " is empty"
            );

            List<Object> group = Arrays.asList(groupCandidate);

            if (group.contains(null)) throw new IllegalStateException(
                    "Null instance provided in group " + groupNumber
            );

            if (!isUnique(group)) throw new IllegalStateException(
                    "Duplicate instances provided in group " + groupNumber
            );

            return group;
        }

        private boolean isUnique(final List<Object> group) {

            Map<Object, Object> set = new IdentityHashMap<Object, Object>(
                    group.size()
            );

            final Object fake = new Object ();
            for ( final Object instance: group ) {

                final boolean isNew = set.put(instance, fake) == null;
                if (!isNew) return false;
            }

            return true;
        }

        /**
         * Instantiate Checker using accumulated configuration.
         *
         * @return Configured Checker.
         */
        public EqualsHashCodeChecker getChecker() {

            if (!groups.isEmpty()) return new EqualsHashCodeChecker(this);

            throw new IllegalStateException("No groups provided");
        }
    }

    /**
     * Instantiate checker builder.
     *
     * @param asserter An asserter to use.
     * @return A builder preconfigured with Asserter
     */
    public static EqualsHashCodeChecker.Builder getBuilder(final EqualsHashCodeAsserter asserter) {

        return new EqualsHashCodeChecker.Builder(asserter);
    }

    private EqualsHashCodeChecker(final Builder builder) {

        this.asserter = builder.asserter;
        this.allowHashCodeCollision = builder.allowHashCodeCollision;

        this.groups = new Object[ builder.groups.size() ][];

        for ( int i = 0; i < builder.groups.size (); i++ ) {

            this.groups[ i ] = builder.groups.get(i).toArray();
        }
    }

    @Override
    public boolean equals(final Object o) {

        if (o == null || o.getClass() != this.getClass()) return false;

        EqualsHashCodeChecker checker = (EqualsHashCodeChecker) o;

        if (allowHashCodeCollision != checker.allowHashCodeCollision) return false;
        if (!asserter.equals(checker.asserter)) return false;

        return Arrays.deepEquals(groups, checker.groups);
    }

    @Override
    public int hashCode() {

        int hash = 7;
        hash += Arrays.deepHashCode(groups) * 31;
        hash += asserter.hashCode() * 31;
        hash += allowHashCodeCollision ? 1 : 0 * 31;

        return hash;
    }

    /**
     * Enforce Object invariants
     *
     * @return this
     */
    public EqualsHashCodeChecker enforceInvariants() {

        for (int groupIndex = 0; groupIndex < groups.length; groupIndex++) {

            final Object[] singleGroup = groups[groupIndex];

            for (int instanceIndex = 0; instanceIndex < singleGroup.length; instanceIndex++) {

                final Object instance = singleGroup[ instanceIndex ];

                applyOnItself(instance, groupIndex, instanceIndex);
                applyOnGroup(instance, groupIndex, instanceIndex);
                applyOnOtherGroups(instance, groupIndex, instanceIndex);
            }
        }

        return this;
    }

    private void applyOnItself(
            final Object instance,
            final int groupIndex,
            final int instanceIndex
    ) {

        final String format = String.format(
                PREFIX + "%%s for %s.", getIdentifier(groupIndex, instanceIndex)
        );

        assertOnItself(instance, format);
    }

    private void applyOnGroup(
            final Object lhs,
            final int groupIndex,
            final int lhsIndex
    ) {

        final String formatFormat =
                PREFIX + getIdentifier(groupIndex, lhsIndex) + " %%s %s."
        ;

        for (int rhsIndex = 0; rhsIndex < groups[groupIndex].length; rhsIndex++) {

            // skip current instance
            if (lhsIndex == rhsIndex) continue;

            final String format = String.format(
                    formatFormat, getIdentifier(groupIndex, rhsIndex)
            );

            assertWithinGroup(lhs, groups[groupIndex][ rhsIndex ], format);
        }
    }

    private void applyOnOtherGroups(
            final Object lhs,
            final int lhsGroupIndex,
            final int lhsIndex
    ) {

       final String format = PREFIX + getIdentifier(lhsGroupIndex, lhsIndex);

       for (int rhsGroupIndex = 0; rhsGroupIndex < this.groups.length; rhsGroupIndex++) {

           // skip current group
           if (rhsGroupIndex == lhsGroupIndex) continue;

           proccessOtherGroup(lhs, format, rhsGroupIndex);
       }
    }

    private void proccessOtherGroup(
            final Object lhsInstance,
            final String formatPrefix,
            final int rhsGroupIndex
    ) {

        final String formatFormat = formatPrefix + " %%s %s.";

        final Object[] rhsGroup = this.groups[ rhsGroupIndex ];

        for (int rhsIndex = 0; rhsIndex < rhsGroup.length; rhsIndex++) {

            final String format = String.format(
                    formatFormat, getIdentifier(rhsGroupIndex, rhsIndex)
            );

            assertBetweenGroups(lhsInstance, rhsGroup[ rhsIndex ], format);
        }
    }

    private String getIdentifier(final int groupIndex, final int instanceIndex) {

        return String.format("[%s;%s]", groupIndex, instanceIndex);
    }

    private void assertOnItself(final Object instance, final String format) {

        asserter.checkEqualsReflexivity(instance, String.format(
                format, "equals is reflexive"
        ) );

        asserter.checkEqualsFailsForNull(instance, String.format(
                format, "equals yealds false for null"
        ) );
    }

    private void assertWithinGroup(
            final Object lhs, final Object rhs, final String format
    ) {

        asserter.checkEquals(lhs, rhs, String.format(
                format, "equals"
        ) );

        asserter.checkHashCodeEquals(lhs, rhs, String.format(
                format, "generates the same hashCode as"
        ) );
    }

    private void assertBetweenGroups(
            final Object lhs, final Object rhs, final String format
    ) {

        asserter.checkDoesNotEqual(lhs, rhs, String.format(
                format, "does not equal"
        ) );

        if (!allowHashCodeCollision) {

            asserter.checkHashCodeDoesNotEqual(lhs, rhs, String.format(
                    format, "generates different hashCode than"
            ) );
        }
    }
}
