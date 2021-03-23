package Helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Hash {
    public static class PasswordWithSalt{
        String password;
        byte[] salt;
        public PasswordWithSalt(String password, byte[] salt){
            this.password = password;
            this.salt = salt;
        }

        public String getPassword() {
            return password;
        }

        public byte[] getSalt() {
            return salt;
        }
    }


    /**
     * Hash password with random salt
     * @param password
     * @return
     */
    public static PasswordWithSalt hashPassword(String password){
        byte[] salt = getSalt();
        String securePassword = getSecurePassword(password.trim(), salt);
        return new PasswordWithSalt(securePassword, salt);
    }

    /**
     * Hash password with the given salt
     * @param password
     * @param salt
     * @return
     */
    public static PasswordWithSalt hashPassword(String password, byte[] salt){
        String securePassword = getSecurePassword(password.trim(), salt);
        return new PasswordWithSalt(securePassword, salt);
    }

    private static String getSecurePassword(String passwordToHash, byte[] salt){
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(salt);
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString().trim();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    private static byte[] getSalt() {
        try {
            //Always use a SecureRandom generator
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            //Create array for salt
            byte[] salt = new byte[16];
            //Get a random salt
            sr.nextBytes(salt);
            //return salt
            return salt;
        }catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
}