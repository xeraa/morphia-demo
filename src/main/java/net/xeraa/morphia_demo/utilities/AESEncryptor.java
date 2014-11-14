package net.xeraa.morphia_demo.utilities;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import java.util.logging.Logger;

public class AESEncryptor {

  private static final Logger LOG = Logger.getLogger(AESEncryptor.class.getName());

  private static StringKeyGenerator generator = KeyGenerators.string();

  private static final String PASSWORD = "secure";

  private static final Object LOCK = new Object();

  /**
   * Get a random salt for encryption.
   *
   * @return 8 byte random salt as a string.
   */
  public static String getSalt() {
    long start = System.nanoTime();
    String salt = generator.generateKey();
    long end = System.nanoTime();
    LOG.finer("Generating salt took: " + timeDiff(start, end));
    return salt;

  }

  /**
   * Encrypt the input with AES.
   *
   * @param input The string to be encrypted.
   * @param salt  The input specific salt.
   * @return The encrypted string - SHA256 with 1024 iterations.
   */
  public static String encrypt(String input, String salt) {
    long start = System.nanoTime();
    TextEncryptor encryptor = Encryptors.text(PASSWORD, salt);
    String cipher = encryptor.encrypt(input); // This will break intentionally if something goes wrong (bad characters in the password?)
    long end = System.nanoTime();
    LOG.finer("Encryption took: " + timeDiff(start, end));
    return cipher;
  }

  /**
   * Decrypt the cipher with AES.
   *
   * @param cipher The encrypted string.
   * @param salt   The cipher specific salt.
   * @return The decrypted cipher.
   */
  public static String decrypt(String cipher, String salt) {
    long start = System.nanoTime();
    TextEncryptor encryptor = Encryptors.text(PASSWORD, salt);
    String output = encryptor.decrypt(cipher); // This will break intentionally if something goes wrong (bad characters in the password?)
    long end = System.nanoTime();
    LOG.finer("Decryption took: " + timeDiff(start, end));
    return output;
  }

  /**
   * Internal method to calculate time differences.
   *
   * @param start Relative start time in ns.
   * @param end   Relative end time in ns.
   * @return Time difference in ms including the unit.
   */
  private static String timeDiff(long start, long end) {
    long diff = (end - start) / 1000;
    return String.format("%,d", diff) + "µs";
  }

}

