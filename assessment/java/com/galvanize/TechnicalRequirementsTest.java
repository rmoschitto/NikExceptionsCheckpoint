package com.galvanize;

import com.galvanize.util.ClassProxy;
import com.galvanize.util.InstanceProxy;
import net.sf.cglib.proxy.Enhancer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Technical Requirements")
public class TechnicalRequirementsTest {

    @Test
    @DisplayName("Verifier throws the appropriate errors")
    public void testVerifier() throws Throwable {
        ClassProxy InvalidFormatException = ClassProxy
                .classNamed("com.galvanize.InvalidFormatException")
                .ensureCheckedException()
                .ensureConstructor(String.class);

        ClassProxy NoServiceException = ClassProxy
                .classNamed("com.galvanize.NoServiceException")
                .ensureCheckedException()
                .ensureConstructor(String.class);

        ClassProxy Verifier = ClassProxy.classNamed("com.galvanize.Verifier")
                .ensureMethod(m -> m.named("verify")
                        .withParameters(String.class)
                        .throwsExactly(InvalidFormatException, NoServiceException));

        InstanceProxy verifier = Verifier.newInstance();

        assertEquals(
                "ERRCODE 21: INPUT_TOO_LONG",
                verifier.assertInvokeThrows(InvalidFormatException, "verify", "999999").getMessage(),
                "Expected Verifier to throw an InvalidFormatException with the correct message when passed '999999'");

        assertEquals(
                "ERRCODE 22: INPUT_TOO_SHORT",
                verifier.assertInvokeThrows(InvalidFormatException, "verify", "234").getMessage(),
                "Expected Verifier to throw an InvalidFormatException with the correct message when passed '234'");

        assertEquals(
                "ERRCODE 27: NO_SERVICE",
                verifier.assertInvokeThrows(NoServiceException, "verify", "10012").getMessage(),
                "Expected Verifier to throw a NoServiceException with the correct message");

        Enhancer Subclass = Verifier.subclassWithComplexMethods(m -> m
                .put("verify", (args) -> {
                    if (args.length == 0) throw new RuntimeException("You called Verifier.verify with no arguments");

                    switch ((String) args[0]) {
                        case "too short":
                            throw (Exception) InvalidFormatException.newInstance("it's too short").getDelegate();
                        case "too long":
                            throw (Exception) InvalidFormatException.newInstance("it's too long").getDelegate();
                        case "no service":
                            throw (Exception) NoServiceException.newInstance("there's no service").getDelegate();
                    }
                    return "This should never happen";
                })
        );

        Object newVerifier = Subclass.create();

        ClassProxy Processor = ClassProxy
                .classNamed("com.galvanize.ZipCodeProcessor")
                .ensureConstructor(Verifier)
                .ensureMethod(m -> m
                        .named("process")
                        .withParameters(String.class));

        InstanceProxy processor = Processor.newInstance(newVerifier);

        String expected = "The zip code you entered was the wrong length.";
        assertEquals(
                expected,
                processor.invoke("process", "too short"),
                "Expected ZipCodeProcessor.process to return \"" + expected + "\" " +
                        "when the Verifier throws a InvalidFormatException"
        );

        expected = "We're sorry, but the zip code you entered is out of our range.";
        assertEquals(
                expected,
                processor.invoke("process", "no service"),
                "Expected ZipCodeProcessor.process to return \"" + expected + "\" " +
                        "when the Verifier throws a NoServiceException"
        );
    }

    private void failFormat(String message, Object... params) {
        fail(String.format(message, params));
    }

}