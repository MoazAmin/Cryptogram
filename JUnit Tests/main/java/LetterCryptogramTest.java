package main.java;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LetterCryptogramTest {
    @Test
    void generateCryptogram() {
        LetterCryptogram crypto = new LetterCryptogram(0);
        // Test to check that alphabet is different
        String encryptedPhrase = crypto.generateCryptogram();

        // Checking if the encrypted Phrase and the Phrase that is generated from the using the GenerateCryptogram() are the same...
        assertEquals(encryptedPhrase, crypto.getEnryptedPhrase());

        //This is to check if the encrypted phrase mapping is not the same as the original phrase mapping..
        Character firstChracterOfOriginal = crypto.getPhrase().charAt(0); // Gets the original Phrase
        Character firstCharacterOfEncrypted = crypto.getEnryptedPhrase().charAt(0); // Gets the Encrypted Phrase
        assertNotEquals(firstChracterOfOriginal, firstCharacterOfEncrypted);

        //The size of the hashmap should be 11 ignoring repeated values.
        assertEquals(crypto.getCrypto().size(), 16);

        //Testig to see if the getplainCharacter works
       // System.out.println(crypto.getPlainChar('l'));

        // this test shows the mapping
        System.out.println(crypto.getCrypto());
    }
}