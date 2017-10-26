package com.galvanize;

public class Verifier {

    public void verify(final String zipCode) throws Exception {
        if (zipCode.length() > 5) {
            throw new Exception("ERRCODE 21: INPUT_TOO_LONG");
        }
        if (zipCode.length() < 5) {
            throw new Exception("ERRCODE 22: INPUT_TOO_SHORT");
        }
        if (zipCode.matches("^1[\\d]+$")) {
            throw new Exception("ERRCODE 27: NO_SERVICE");
        }
    }

}
