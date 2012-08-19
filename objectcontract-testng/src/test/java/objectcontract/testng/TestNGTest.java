package objectcontract.testng;

import objectcontract.EqualsHashCodeAsserter;

import org.testng.annotations.Test;

public class TestNGTest {

    private EqualsHashCodeAsserter asserter = new TestNGEqualsHashCodeAsserter();

    @Test
    public void testSuccess() {

        final Integer zero = new Integer(0);
        final Integer one = new Integer(1);

        asserter.checkEqualsReflexivity(zero, "");
        asserter.checkEqualsFailsForNull(zero, "");

        asserter.checkEquals(zero, zero, "");
        asserter.checkHashCodeEquals(zero, zero, "");

        asserter.checkDoesNotEqual(zero, one, "");
        asserter.checkHashCodeDoesNotEqual(zero, one, "");
    }

    @Test(expectedExceptions = { AssertionError.class })
    public void failReflexivity() {

        asserter.checkEqualsReflexivity(new Object() {
            @Override
            public boolean equals(Object o) { return false; }
        }, "");
    }

    @Test(expectedExceptions = { AssertionError.class })
    public void failNotNull() {

        asserter.checkEqualsFailsForNull(new Object() {
            @Override
            public boolean equals(Object o) { return true; }
        }, "");
    }

    @Test(expectedExceptions = { AssertionError.class })
    public void failAssertEquals() {

        asserter.checkEquals(new Object(), new Object(), "");
    }

    @Test(expectedExceptions = { AssertionError.class })
    public void failAssertHashCodeEquals() {

        asserter.checkHashCodeEquals(new Object(), new Object(), "");
    }

    @Test(expectedExceptions = { AssertionError.class })
    public void failAssertDoesNotEqual() {

        final Object o = new Object();

        asserter.checkDoesNotEqual(o, o, "");
    }

    @Test(expectedExceptions = { AssertionError.class })
    public void failAssertHashCodeDoesNotEqual() {

        final Object o = new Object();

        asserter.checkHashCodeDoesNotEqual(o, o, "");
    }
}
