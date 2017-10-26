package com.galvanize;

import com.galvanize.util.ClassProxy;
import com.galvanize.util.InstanceProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Functional Requirements")
public class FunctionalRequirementsTest {

    @Test
    @DisplayName("Processor returns the correct messages")
    public void testZipCodeProcessor() {
        ClassProxy Verifier = ClassProxy.classNamed("com.galvanize.Verifier");

        ClassProxy ZipCodeProcessor = ClassProxy.classNamed("com.galvanize.ZipCodeProcessor")
                .ensureMethod(m -> m
                        .named("process")
                        .withParameters(String.class)
                        .returns(String.class))
                .ensureConstructor(Verifier);

        InstanceProxy verifier = Verifier.newInstance();
        InstanceProxy processor = ZipCodeProcessor.newInstance(verifier);

        String result = (String) processor.invoke("process", "10101");
        String expected = "We're sorry, but the zip code you entered is out of our range.";
        assertEquals(
                expected,
                result,
                "Expected ZipCodeProcessor.process to return \"" + expected + "\" " +
                        "when passed 10101"
        );

        result = (String) processor.invoke("process", "001");
        expected = "The zip code you entered was the wrong length.";
        assertEquals(
                expected,
                result,
                "Expected ZipCodeProcessor.process to return \"" + expected + "\" " +
                        "when passed 001"
        );

        result = (String) processor.invoke("process", "001001001");
        expected = "The zip code you entered was the wrong length.";
        assertEquals(
                expected,
                result,
                "Expected ZipCodeProcessor.process to return \"" + expected + "\" " +
                        "when passed 001001001"
        );

        result = (String) processor.invoke("process", "80022");
        expected = "Thank you!  Your package will arrive soon.";
        assertEquals(
                expected,
                result,
                "Expected ZipCodeProcessor.process to return \"" + expected + "\" " +
                        "when passed 80022"
        );

    }

    private void failFormat(String message, Object... params) {
        fail(String.format(message, params));
    }

}