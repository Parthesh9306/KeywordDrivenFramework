package mail;

import org.apache.commons.codec.binary.Base64;

/**
 * Password encode decode has been created for not to 
 * show email password in environment property file
 * 
 * @author Chetan.Aher
 *
 */
public class EncoderDecoder {
    
    /**
     * Get decoded encoded password used ASCI encryption type 
     *  
     * @param encodedPassword
     * @return
     */
    public static String decodePassword(String encodedPassword) {
	byte[]   bytesEncoded = Base64.decodeBase64(encodedPassword);
	return  new String(bytesEncoded);
    }
    
    /**
     * Encode password used ASCI encryption type
     * 
     * @param passwordToEncode
     * @return
     */
    public static String encodePassword(String passwordToEncode) {
	byte[]   bytesEncoded = Base64.encodeBase64(passwordToEncode.getBytes());
	return  new String(bytesEncoded);
    }
}
