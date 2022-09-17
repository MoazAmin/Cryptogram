package main.java;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberCryptogramTest {
    @Test
    void generateCryptogram() {
        NumberCryptogram testPhrase = new NumberCryptogram(0);
        //Getting an encrypted version of testPhrase
        String encryptedTestPhrase = testPhrase.generateCryptogram();
        //Testing if the original testPhrase is different from the encryptedTestPhrase that was made using the generateCryptogram() method
        assertEquals(encryptedTestPhrase, testPhrase.getEnryptedPhrase());

        //This is to check if the encrypted phrase mapping is not the same as the original phrase mapping..
        Character firstChracterTestPhrase = testPhrase.getPhrase().charAt(0); // Gets the original Phrase
        Character firstCharacterEncryptedTestPhrase = testPhrase.getEnryptedPhrase().charAt(0); // Gets the Encrypted Phrase
        assertNotEquals(firstChracterTestPhrase, firstCharacterEncryptedTestPhrase);


        //The size of the hashmap should be 11 ignoring repeated values.
        assertEquals(testPhrase.getCrypto().size(), 16);

        //Testig to see if the getplainCharacter works
        // System.out.println(testPhrase.getPlainChar(23));

        // this test shows the mapping
        System.out.println(testPhrase.getCrypto());


    }
}
