package main.java;
import java.util.HashMap;
import java.util.Random;

public class NumberCryptogram extends Cryptogram{
    private HashMap<Character, Integer> crypto;
    private String Originalphrase;
    private String encryptedPhrase;
    private final String BASE_QUOTES_FILE = "src/resources/phrases.txt";


    public NumberCryptogram(){
        super();
    }

    public NumberCryptogram(int phraseNumber){
        crypto = new HashMap<>();
        Originalphrase = super.getPhraseForEncryption(BASE_QUOTES_FILE,phraseNumber).toLowerCase();
    }

    public NumberCryptogram(String fileName, int phraseNumber){
        crypto = new HashMap<>();
        Originalphrase = super.getPhraseForEncryption(fileName,phraseNumber).toLowerCase();
    }

    /**
     * This method gets the phrases from the file and generates them into randomised number
     * each letter of the alphabet will be specified a random number
     * @return printmethod
     */
    @Override
    public String generateCryptogram() {
        Random rand = new Random();
        for (int i = 0; i < Originalphrase.length(); i++) {
            char characterAtPhrase = Originalphrase.charAt(i);
            if (!(crypto.containsKey(characterAtPhrase)) && characterAtPhrase >= 'a' && characterAtPhrase <= 'z') {
                int encryptNumber = rand.nextInt(26)+1;
                while(crypto.containsValue(encryptNumber)){
                    encryptNumber = rand.nextInt(26)+1;
                }
                crypto.put(characterAtPhrase, encryptNumber);
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
        phrase = phrase.toLowerCase();
        for (int i = 0; i < phrase.length(); i++) {
            if (phrase.charAt(i) == ' ') {
                toPrint = toPrint.concat("  ");
            }else if(!(phrase.charAt(i) >= 'a' && phrase.charAt(i)<= 'z')){
                toPrint = toPrint.concat(phrase.charAt(i)+"");
            }
            else {
                if((i+1) >= phrase.length()){
                    toPrint = toPrint.concat(crypto.get(phrase.charAt(i)) + "");
                }
                else {
                    if (!(phrase.charAt(i + 1) >= 'a' && phrase.charAt(i) <= 'z')) {
                        toPrint = toPrint.concat(crypto.get(phrase.charAt(i)) + "");
                    } else {
                        toPrint = toPrint.concat(crypto.get(phrase.charAt(i)) + "-");
                    }
                }
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
    public Character getPlainChar(int cryptoValue) {
        char letterForKey = 'a';
        char plainCHar = '\0';

        if(crypto.containsValue(cryptoValue)){
            for(int indexForMapping = 0; indexForMapping < 26; indexForMapping++){
                if(crypto.containsValue(cryptoValue)){
                    if(crypto.get(letterForKey).equals(cryptoValue)){
                        plainCHar = letterForKey;
                    }
                }
                letterForKey = (char) (letterForKey+1);
            }
        }
        else{
            System.out.println("No associated key to value");
        }
        return plainCHar;
    }

    @Override
    public HashMap<?, ?> getCrypto() {
        return crypto;
    }

    public String getEnryptedPhrase(){
        return encryptedPhrase;
    }

    public void setEncrypted(String encrypted){
        encryptedPhrase = encrypted;
    }
}