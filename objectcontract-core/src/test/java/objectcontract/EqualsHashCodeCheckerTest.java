package objectcontract;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

public class EqualsHashCodeCheckerTest {

    private final EqualsHashCodeAsserter asserter = new DefaultEqualsHashCodeAsserter();

    /**
     * @return the asserter
     */
    protected EqualsHashCodeAsserter getAsserter() {

        return asserter;
    }

    @Test(
            expectedExceptions = { NullPointerException.class },
            expectedExceptionsMessageRegExp = "No asserter provided"
    )
    public final void putNullAsserter() {

        EqualsHashCodeChecker.getBuilder(null);
    }

    @Test(
            expectedExceptions = {IllegalStateException.class},
            expectedExceptionsMessageRegExp = "No groups provided"
    )
    public final void testNoBuilding() {

        EqualsHashCodeChecker.getBuilder(getAsserter()).getChecker();
    }

    @Test(
            expectedExceptions = {IllegalStateException.class},
            expectedExceptionsMessageRegExp = "Group 0 is empty"
    )
    public final void testEmptyGroupUsed() {

        EqualsHashCodeChecker.getBuilder(getAsserter()).setGroup().getChecker();
    }

    @Test(
            expectedExceptions = {IllegalStateException.class},
            expectedExceptionsMessageRegExp = "Null instance provided in group 0"
    )
    public final void testNullInstanceProvided() {

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(null, null)
                .getChecker()
        ;
    }

    @Test(
            expectedExceptions = {IllegalStateException.class},
            expectedExceptionsMessageRegExp = "Null instance provided in group 1"
    )
    public final void testNullInstanceProvidedInNthGroup() {

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(new Integer(0), new Integer(0))
                .setGroup(null, null)
                .getChecker()
        ;
    }

    @Test(
            expectedExceptions = {IllegalStateException.class},
            expectedExceptionsMessageRegExp = "Duplicate instances provided in group 0"
    )
    public final void testDuplicateInstanceProvided() {

        final Object instance = new Object();

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(instance, instance)
                .getChecker()
        ;
    }

    @Test(
            expectedExceptions = {IllegalStateException.class},
            expectedExceptionsMessageRegExp = "Duplicate instances provided in group 1"
    )
    public final void testDuplicateInstanceProvidedInNthGroup() {

        final Object instance = new Object();

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(new Integer(1), new Integer(1))
                .setGroup(instance, instance)
                .getChecker()
        ;
    }

    @Test
    public final void builderFactoryComparison() {

        final EqualsHashCodeChecker built = EqualsHashCodeChecker
                .getBuilder(getAsserter())
                .setGroup(new Integer(1), new Integer(1))
                .setGroup(new Integer(0), new Integer(0))
                .setGroup(new Integer(-1), new Integer(-1))
                .getChecker()
        ;

        final Object[][] groups = new Integer[][] {
                { new Integer(1), new Integer(1) },
                { new Integer(0), new Integer(0) },
                { new Integer(-1), new Integer(-1) }
        };

        final EqualsHashCodeChecker created = EqualsHashCodeChecker
                .getBuilder(getAsserter())
                .setGroups(groups)
                .getChecker()
        ;

        assertEquals(built, created);
    }

    @Test(
            expectedExceptions = { AssertionError.class },
            expectedExceptionsMessageRegExp = ".*equals is reflexive for \\[0;0\\]\\."
    )
    public final void brokenReflexivity() {

        final Object instance = new Object() {
            @Override
            public boolean equals(final Object o) {

                return false;
            }
        };

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(instance)
                .getChecker()
                .enforceInvariants ()
        ;
    }

    @Test(
            expectedExceptions = { AssertionError.class },
            expectedExceptionsMessageRegExp = ".*equals yealds false for null for \\[0;0\\]\\."
    )
    public final void brokenEqualsToNull() {

        final Object instance = new Object() {
            @Override
            public boolean equals(final Object o) {

                return true;
            }
        };

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(instance)
                .getChecker()
                .enforceInvariants ()
        ;
    }

    @Test(
            expectedExceptions = { AssertionError.class },
            expectedExceptionsMessageRegExp = ".*equals yealds false for null for \\[0;0\\]\\..*"
    )
    public final void brokenEqualsToEmptyObject() {

        final Object instance = new Object() {
            @Override
            public boolean equals(final Object o) {

                return o == this || o == null;
            }
        };

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(instance)
                .getChecker()
                .enforceInvariants ()
        ;
    }

    @Test(
            expectedExceptions = { AssertionError.class },
            expectedExceptionsMessageRegExp = ".* \\[0;0\\] equals \\[0;1\\]\\."
    )
    public final void brokenEqualityGroup() {

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(new Integer(0), new Integer(1))
                .getChecker()
                .enforceInvariants ()
        ;
    }

    @Test(
            expectedExceptions = { AssertionError.class },
            expectedExceptionsMessageRegExp = ".*\\[0;0\\] equals \\[0;2\\]\\."
    )
    public final void brokenEqualityGroupCrowdy() {

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(new Integer(0), new Integer(0), new Integer(1), new Integer(0))
                .getChecker()
                .enforceInvariants ()
        ;
    }

    @Test(
            expectedExceptions = { AssertionError.class },
            expectedExceptionsMessageRegExp = ".*\\[2;0\\] equals \\[2;2\\]\\."
    )
    public final void brokenEqualityGroupNthGroup() {

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(new Integer(0))
                .setGroup(new Integer(1), new Integer(1))
                .setGroup(new Integer(2), new Integer(2), new Integer(3))
                .getChecker()
                .enforceInvariants ()
        ;
    }

    private static class MissingHashCode {
        @Override
        public boolean equals(final Object o) {

            return o == null
                ? false
                : o.getClass() == this.getClass()
            ;
        }
    }

    @Test(
            expectedExceptions = { AssertionError.class },
            expectedExceptionsMessageRegExp = ".*\\[0;0\\] generates the same hashCode as \\[0;1\\]\\."
    )
    public final void brokenHashCodeOnEqualObjects() {

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(new MissingHashCode(), new MissingHashCode())
                .getChecker()
                .enforceInvariants ()
        ;
    }

    @Test(
            expectedExceptions = { AssertionError.class },
            expectedExceptionsMessageRegExp = ".*\\[0;0\\] does not equal \\[1;0\\]\\."
    )
    public final void brokenEqualityBetweenGroups() {

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(new MissingHashCode())
                .setGroup(new MissingHashCode())
                .getChecker()
                .enforceInvariants ()
        ;
    }

    private static class MissingEquals {
        @Override
        public int hashCode() {

            return 42;
        }
    }

    @Test(
            expectedExceptions = { AssertionError.class },
            expectedExceptionsMessageRegExp = ".*\\[0;0\\] generates different hashCode than \\[2;0\\]\\."
    )
    public final void hashCodeCollisionBetweenGroups() {

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(new MissingEquals())
                .setGroup(new Integer(0))
                .setGroup(new MissingEquals())
                .getChecker()
                .enforceInvariants ()
        ;
    }

    @Test
    public final void allowedHashCodeCollisionBetweenGroups() {

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroup(new MissingEquals())
                .setGroup(new Integer(0))
                .setGroup(new MissingEquals())
                .allowHashCodeCollision ()
                .getChecker()
                .enforceInvariants ()
        ;
    }

    @Test
    public final void testObjectConstract() {

        final EqualsHashCodeChecker.Builder builder = EqualsHashCodeChecker.getBuilder(getAsserter());
        builder.setGroup(new Integer(0));

        final EqualsHashCodeChecker differentAsserterChecker = EqualsHashCodeChecker
                .getBuilder(new DefaultEqualsHashCodeAsserter())
                .setGroup(new Integer(0))
                .getChecker()
        ;

        final Object[][] instances = {
                {
                        builder.getChecker()
                },
                {
                        builder.setGroup(new Integer(1)).getChecker()
                },
                {
                        builder.allowHashCodeCollision().getChecker()
                },
                {
                        builder.setGroup(new Integer(2)).getChecker()
                },
                {
                        differentAsserterChecker
                }
        };

        EqualsHashCodeChecker.getBuilder(getAsserter())
                .setGroups(instances)
                .getChecker()
                .enforceInvariants ()
        ;
    }
}
