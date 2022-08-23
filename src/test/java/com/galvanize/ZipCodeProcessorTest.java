package com.galvanize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZipCodeProcessorTest {
    Verifier verifier;
    ZipCodeProcessor processor;

    @BeforeEach
    void setup() {
        verifier = new Verifier();
        processor = new ZipCodeProcessor(verifier);
    }

    // write your tests here


    @Test
    void processShouldReturnValidStringWithValidZipcode() {
        String expected = "Thank you!  Your package will arrive soon.";
        String actual = processor.process("78704");

        assertEquals(expected, actual);

    }

    @Test
    void processShouldReturnInvalidLengthWithLongZipcode() {
        String expected = "The zip code you entered was the wrong length.";
        String actual = processor.process("87654321");

        assertEquals(expected, actual);
    }

    @Test
    void processShouldReturnInvalidLengthWithShortZipcode() {
        String expected = "The zip code you entered was the wrong length.";
        String actual = processor.process("876");

        assertEquals(expected, actual);
    }

    @Test
    void processShouldReturnOutOfRangeForZipcodeWithOne() {
        String expected = "We're sorry, but the zip code you entered is out of our range.";
        String actual = processor.process("12234");

        assertEquals(expected,actual);
    }




}