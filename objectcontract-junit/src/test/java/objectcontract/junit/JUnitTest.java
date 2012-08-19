package objectcontract.junit;

import objectcontract.EqualsHashCodeAsserter;
import org.junit.Test;
import static org.junit.Assert.*;

public class JUnitTest {

    private EqualsHashCodeAsserter asserter = new JUnitEqualsHashCodeAsserter();

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

    @Test
    public void failReflexivity() {

        try {

            asserter.checkEqualsReflexivity(new Object() {
                @Override
                public boolean equals(Object o) { return false; }
            }, "");
        } catch (AssertionError ex) {

            return;
        } catch (Exception e) {

        }

        fail("AssertionError not thrown");
    }

    @Test
    public void failNotNull() {

        try {

            asserter.checkEqualsFailsForNull(new Object() {
                @Override
                public boolean equals(Object o) { return true; }
            }, "");
        } catch (AssertionError ex) {

            return;
        } catch (Exception e) {

        }

        fail("AssertionError not thrown");
    }

    @Test
    public void failAssertEquals() {

        try {

            asserter.checkEquals(new Object(), new Object(), "");
        } catch (AssertionError ex) {

            return;
        } catch (Exception e) {

        }

        fail("AssertionError not thrown");
    }

    @Test
    public void failAssertHashCodeEquals() {

        try {

            asserter.checkHashCodeEquals(new Object(), new Object(), "");
        } catch (AssertionError ex) {

            return;
        } catch (Exception e) {

        }

        fail("AssertionError not thrown");
    }

    @Test
    public void failAssertDoesNotEqual() {

        try {

        final Object o = new Object();

            asserter.checkDoesNotEqual(o, o, "");
        } catch (AssertionError ex) {

            return;
        } catch (Exception e) {

        }

        fail("AssertionError not thrown");
    }

    @Test
    public void failAssertHashCodeDoesNotEqual() {

        final Object o = new Object();

        try {

            asserter.checkHashCodeDoesNotEqual(o, o, "");
        } catch (AssertionError ex) {

            return;
        } catch (Exception e) {

        }

        fail("AssertionError not thrown");
    }
}
