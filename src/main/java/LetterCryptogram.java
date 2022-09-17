package main.java;
import java.util.HashMap;
import java.util.Random;

public class LetterCryptogram extends Cryptogram{
    private HashMap<Character, Character> crypto;
    private String Originalphrase;
    private String encryptedPhrase;
    private final String BASE_QUOTES_FILE = "src/resources/phrases.txt";


    public LetterCryptogram(){
        super();
    }

    public LetterCryptogram(int phraseNumber){
        super();
        crypto = new HashMap<>();
        Originalphrase = super.getPhraseForEncryption(BASE_QUOTES_FILE, phraseNumber).toLowerCase();
    }

    public LetterCryptogram(String fileName, int phraseNumber){
        super();
        crypto = new HashMap<>();
        Originalphrase = super.getPhraseForEncryption(fileName, phraseNumber).toLowerCase();
    }


    /*
    This is the method which is called inorder to generate the encrypted letter phrase
    @return String- this method returns the encrypted string
     */
    @Override
    public String generateCryptogram() {
        Random rand = new Random();
        int shiftNumber = rand.nextInt(25)+1;
        for(int i = 0 ; i < Originalphrase.length(); i++){
            char characterAtPhrase = Originalphrase.charAt(i);
            if(!(crypto.containsKey(characterAtPhrase)) && characterAtPhrase >= 'a' && characterAtPhrase <= 'z'){
                char encryptedLetter = (char) (characterAtPhrase + shiftNumber);
                if (encryptedLetter > 'z'){
                    encryptedLetter = (char) (characterAtPhrase - (26 - shiftNumber));
                    crypto.put(characterAtPhrase, encryptedLetter);
                }
                else{
                    crypto.put(characterAtPhrase, encryptedLetter);
                }
            }
        }
        return printMethod(Originalphrase);
    }

    /*
    This method is used to get the encrypted phrase
    @param String phrase - this is the originial phrase
    @return String - returns the encrypted phrase
     */
    @Override
    public String printMethod(String phrase){
        System.out.println();
        String toPrint = "";
        for (int i = 0; i < phrase.length(); i++) {
            if (phrase.charAt(i) == ' ') {
                toPrint = toPrint.concat(" ");
            }else if(!(phrase.charAt(i) >= 'a' && phrase.charAt(i)<= 'z')){
                toPrint = toPrint.concat(phrase.charAt(i)+"");
            }
            else {
                toPrint = toPrint.concat(crypto.get(phrase.charAt(i)) + "");
            }
        }
        setEncrypted(toPrint);
        return toPrint;
    }

    /*
    This method is used to get the plain character from the hashmap
    @param Character cryptoValue - passes in the hashed value
    @return Character - returns the original mapping
     */
    public Character getPlainChar(Character cryptoValue) {
        char letterForKey = 'a';
        char plainCHar = '\0';
        if(crypto.containsValue(cryptoValue)){
            for(int indexForMapping = 0; indexForMapping < 26; indexForMapping++){
                    if(crypto.get(letterForKey).equals(cryptoValue)){
                        plainCHar = letterForKey;
                    }
                letterForKey = (char) (letterForKey+1);
            }
        }
        else{
            System.out.println("No associated key to value");
        }
        return plainCHar;
    }
    public String getEnryptedPhrase(){
        return encryptedPhrase;
    }
    public void setEncrypted(String encrypted){
        encryptedPhrase = encrypted;
    }

    public HashMap<Character, Character> getCrypto() {
        return crypto;
    }
}