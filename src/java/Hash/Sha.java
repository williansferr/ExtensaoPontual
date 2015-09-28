package hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

 public  class Sha {

    public static String generateHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            hex.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return hex.toString();
    }
}


