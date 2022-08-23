package com.galvanize;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VerifierTest {


    @Test
    void verifyShouldThrowInvalidFormatExceptionWithLongZipcode() {
        Verifier verifier = new Verifier();

        assertThrows(InvalidFormatException.class, () -> verifier.verify("7777777"));

    }

    @Test
    void verifyShouldThrowInvalidFormatExceptionWithShortZipcode() {
        Verifier verifier = new Verifier();
        Throwable result = assertThrows(InvalidFormatException.class, () -> verifier.verify("333"));

        assertEquals("ERRCODE 22: INPUT_TOO_SHORT", result.getMessage());


//        try {
//            verifier.verify("333");
//        } catch (Exception e) {
//            assertEquals("ERRCODE 22: INPUT_TOO_SHORT", e.getMessage());
//        }

    }

    @Test
    void verifyShouldThrowNoServiceExceptionWithInvalidZipcode() {
        Verifier verifier = new Verifier();

        assertThrows(NoServiceException.class, () -> verifier.verify("12233"));

    }

}
