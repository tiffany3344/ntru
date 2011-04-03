/**
 * This software is dual-licensed. You may choose either the
 * Non-Profit Open Software License version 3.0, or any license
 * agreement into which you enter with Security Innovation, Inc.
 * 
 * Use of this code, or certain portions thereof, implements
 * inventions covered by claims of one or more of the following
 * U.S. Patents and/or foreign counterpart patents, owned by
 * Security Innovation, Inc.:
 * 7,308,097, 7,031,468, 6,959,085, 6,298,137, and 6,081,597.
 * Practice or sale of the inventions embodied in the code hereof
 * requires a license from Security Innovation Inc. at:
 * 
 * 187 Ballardvale St, Suite A195
 * Wilmington, MA 01887
 * USA
 */

package net.sf.ntru;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class NtruEncryptTest {
    
    @Test
    public void testEncryptDecrypt() throws NoSuchAlgorithmException {
        EncryptionParameters params = EncryptionParameters.MAR2011_439;
        EncryptionKeyPair kp = NtruEncrypt.generateKeyPair(params);

        // encrypt and decrypt a polynomial
        IntegerPolynomial m = IntegerPolynomial.generateRandom(params.N);
        IntegerPolynomial r = IntegerPolynomial.generateRandomSmall(params.N, params.dr, params.dr);
        IntegerPolynomial e = NtruEncrypt.encrypt(m, r, kp.pub.h, params);
        IntegerPolynomial c = NtruEncrypt.decrypt(e, kp.priv.f, params);
        assertArrayEquals(m.coeffs, c.coeffs);
        
        // encrypt and decrypt text
        byte[] plainText = "text to encrypt".getBytes();
        byte[] encrypted = NtruEncrypt.encrypt(plainText, kp.pub, params);
        byte[] decrypted = NtruEncrypt.decrypt(encrypted, kp, params);
        assertArrayEquals(plainText, decrypted);
        
        // test an empty message
        plainText = "".getBytes();
        encrypted = NtruEncrypt.encrypt(plainText, kp.pub, params);
        decrypted = NtruEncrypt.decrypt(encrypted, kp, params);
        assertArrayEquals(plainText, decrypted);
        
        // test a max length message
        plainText = new byte[params.maxMsgLenBytes];
        System.arraycopy("secret encrypted text".getBytes(), 0, plainText, 0, 21);
        encrypted = NtruEncrypt.encrypt(plainText, kp.pub, params);
        decrypted = NtruEncrypt.decrypt(encrypted, kp, params);
        assertArrayEquals(plainText, decrypted);
        
        // test a message that is too long
        try {
            plainText = new byte[params.maxMsgLenBytes+1];
            System.arraycopy("secret encrypted text".getBytes(), 0, plainText, 0, 21);
            encrypted = NtruEncrypt.encrypt(plainText, kp.pub, params);
            decrypted = NtruEncrypt.decrypt(encrypted, kp, params);
            assertArrayEquals(plainText, decrypted);
            fail("An exception should have been thrown!");
        }
        catch (RuntimeException ex) {
        }
        
        // encrypt and decrypt text using an encoded key pair
        EncryptionPrivateKey priv = new EncryptionPrivateKey(new byte[] {53, 25, -80, 30, -34, -30, 24, -8, 87, 70, 116, 67, -103, -50, 29, -79, -32, 0, -53, -1, -54, -18, 85, 6, 40, -62, 123, -27, -61, 21, -63, 1, 10, -41, -84, 26, -106, 47, -105, -22, 1, -2, -41, -10, -34, -56, -81, -65, -94, -61, -44, 68, 2, -43, -64, -68, -64, -111, -70, -101, -35, 9, 28, 85, 68, 27, 93, -108, -27, -23, 34, 114, -22, 111, 63, 86, 122, -75, -98, 39, 46, -82, 54, 108, -94, 76, -76}, params);
        EncryptionPublicKey pub = new EncryptionPublicKey(new byte[] {37, 73, -24, 62, -72, 91, 23, -79, 120, 3, -9, 96, -109, 126, 40, 46, 102, 24, 82, 17, -64, -26, -87, 91, 15, -27, -1, -25, 1, 68, -72, -30, -13, 20, 114, 92, 68, -96, -82, 12, 44, 46, -124, 73, 121, 52, -112, -101, 99, -113, -5, -113, -63, -125, -40, -9, -53, -48, -123, 108, 16, -99, -3, -94, -72, 125, -82, 111, 71, -38, -38, 105, -37, -101, 34, 10, 91, 92, -122, -56, 96, -103, -89, -86, 109, -65, 111, -118, 51, 31, 5, 25, 86, -79, -42, -127, 39, 101, -18, 90, -58, 32, -105, -98, 30, -82, -16, -127, 115, -122, 45, 27, -95, 25, 108, -87, -92, 103, 112, -83, -31, 56, -58, -33, 59, 98, -35, 20, 112, 17, 122, -85, -41, 99, -65, -107, 16, -108, -19, -9, -42, -75, -11, -39, -91, 28, -81, -34, 91, -108, 63, 70, 28, -15, 54, -46, -113, -4, 115, 91, -2, 51, -46, 47, -96, -71, 81, -104, -51, -41, -61, -3, -78, -32, -105, 85, -55, -40, -105, 69, -10, -90, 92, 24, 41, 121, -37, -51, -56, -73, 83, 81, -38, -5, -2, 92, 19, 68, 42, 35, 90, -122, 2, -113, 77, -53, -8, -64, 102, -22, -39, -107, -1, -104, 117, 36, -13, 39, -39, 125, 65, -6, -95, -61, 103, 103, -91, -97, 29, 48, -14, -79, -15, 102, 42, 101, 67, 97, 17, -92, 78, 70, 106, -73, 127, 82, -26, 106, 57, -91, 53, 39, 30, 44, -1, 12, 117, -67, 18, -28, -38, -29, 76, -31, 105, 2, -16, -30, 2, -33, -69, 106, -77, 29, -14, -119, 76, -4, -46, -59, -121, 28, 72, 105, -38, 80, -105, -56, -119, 7, -73, -29, -103, 80, -26, 5, 30, -93, -123, 49, 37, -91, -8, 99, 0, 101, -121, 51, -92, -8, -62, -77, -45, 48, 4, -17, -52, 36, 31, -44, -84, -42, -24, -65, 9, -31, 46, 122, 31, -83, -120, 50, -92, -15, -23, -67, -117, -120, 40, -2, 74, 3, -33, -26, 125, -126, 35, -112, -68, -27, 80, 92, 21, -14, 47, -10, -122, -64, -38, -32, 82, -93, -5, -62, -69, 26, 99, 103, -55, 72, -123, 126, 11, -74, 91, -121, -106, -34, -52, -92, -39, 23, 34, -26, -124, 115, 48, -91, -43, 20, 107, -107, -1, 69, -68, -85, 43, -20, 60, 2, 72, 22, 33, 72, -75, -8, 14, 98, -28, 48, -80, 86, 17, 78, -61, 113, -32, -70, 105, 55, 7, 11, 50, 20, 4, -110, 52, 26, 103, -79, -114, 33, -11, 8, 124, 85, 50, -41, -122, 27, 27, 98, -36, -81, 82, -110, -63, 37, 91, -119, 67, 103, -11, -46, 127, 64, 42, -40, -68, -106, -47, -90, 36, 19, 82, -70, -24, 3, 39, -82, -100, -5, -91, 95, -45, -98, 45, 47, -110, -31, 37, 60, -60, 13, 5, -82, -122, 24, 8, 68, 38, 64, 121, 8, -21, -15, 64, -27, 111, 64, 59, -77, 5, -85, -25, 93, -74, -115, -47, -22, 93, 32, 122, -53, -51, -20, 101, -108, 120, -51, 115, -24, -26, -111, 32, 3, 69, -90, 61, 19, 61, -116, -32, -15, 35, 85, -64, -55, -93, 53, -34, -97, -40, 38, -115, -23, -19, -107, -32, -89, 42, -104, 42, -98, 26, 19, -14, 48, 49, 112, 27, -66, -17, -121, 95, -70, 5, 85, -7, -103, -121, -41, 2, -10, 76, 70, 68, 91, 115, -18, -74, 63, 102, 73, -113, 43, -55, -28, -6, -122, -77, 57, 16, 53, 39, -18, -80, -3, 17, 109, -40, 53, 5, 13}, params);
        kp = new EncryptionKeyPair(priv, pub);
        plainText = "secret encrypted text".getBytes();
        encrypted = NtruEncrypt.encrypt(plainText, kp.pub, params);
        decrypted = NtruEncrypt.decrypt(encrypted, kp, params);
        assertArrayEquals(plainText, decrypted);
        
        params = EncryptionParameters.EES1499EP1;
        priv = new EncryptionPrivateKey(new byte[] {116, 7, 118, 121, 6, 77, -36, 60, 65, 108, 10, -106, 12, 9, -22, -113, 122, -31, -31, 18, 120, 81, -33, 5, 122, -76, 109, -30, -101, -45, 21, 13, -11, -49, -111, 46, 91, 4, -28, -109, 121, -119, -121, -58, -113, -9, -10, -25, -53, 40, -86, -22, -50, 42, 52, 107, 119, 17, 33, 125, -26, 33, 55, 25, -77, -65, -106, 116, -67, 91, 105, -7, 42, -107, -54, 101, 12, -12, 57, -116, 45, -107, -17, 110, 35, -64, 19, -38, -122, 115, -93, 53, 69, 66, -106, 17, 20, -71, 121, 23, -21, -45, 108, 97, 23, -98, -12, -41, -31, -53, 30, -42, 15, 85, -21, -89, 118, 42, -117, -39, 69, 0, -63, 83, 48, -80, -14, -123, -4, -116, -90, -107, -89, 119, 29, -30, 69, 22, -84, 47, 117, -123, 102, -116, 35, 93, -13, 84, -9, -122, 58, 101, 93, -106, -119, -35, -75, 76, 27, -125, -22, 68, 101, 49, 103, -13, -98, 93, -56, -110, -19, -12, 74, 104, 7, 6, -11, 47, 57, 90, 75, -30, 47, 66, -58, 14, 14, 70, 11, -119, -36, -118, -55, -53, 101, -73, -77, 33, -29, 96, -86, 38, 47, 103, 19, -37, -17, -50, -82, -87, -119, 37, -54, 77, -69, -16, -48, -52, 110, -26, 111, 35, 26, -53, -10, 9, -108, -34, 102, 7, -18, -72, -26, 24, -50, -43, 92, 56, -94, 23, -36, 60, 28, -121, 27, 127, -93, -79, -45, -60, 105, -6, -88, 72, -41, 47, -51, 3, 91, 116, 75, 122, -94, -113, 28, -96, -62, -29, -74, -85, -93, 51, 58, 72, 44, 9, 18, -48, -24, 73, 122, 60, -23, 83, -110, -7, -111, -69, 106, 51, 118, -83, -18, 109, -32, 40, 22}, params);
        pub = new EncryptionPublicKey(new byte[] {-62, 56, 59, -46, 30, -19, 22, -115, -20, 117, -14, 3, 2, -57, 85, -24, 27, 57, 49, -93, -52, 87, 49, 96, 15, 95, -95, -86, -61, 50, -18, 3, 109, -55, -110, -57, 82, 124, -5, -57, 68, -18, 126, 114, 6, -22, 8, 121, 125, 29, -16, 112, -81, 27, -7, 109, -44, -123, -15, -14, 74, -126, 95, -94, -91, 119, 80, -48, 41, 49, 6, 104, 93, -97, -108, -82, 93, 70, -127, -113, -22, -103, 35, -115, 20, -115, 63, 57, -84, -18, -107, 81, 44, -16, 83, 71, -27, -2, -125, 87, 26, 100, -116, 110, 94, -46, -56, -82, 119, -110, -127, -99, -8, -118, 90, 64, -29, 102, 99, 92, 86, -117, 26, -89, 32, 17, 55, -65, -10, -5, -74, 19, 13, 113, -15, -103, 17, 10, -127, -95, -79, 19, 11, -24, 59, 28, -70, -55, -69, -105, -20, -117, 66, 4, 77, 116, -124, -62, 19, 109, 49, -120, 10, -15, 108, 84, 126, 122, -46, -37, 114, -78, -72, 34, -12, 25, -104, -3, 114, -94, 16, 31, 31, -124, -109, -64, 57, -47, -113, -26, 97, -58, 112, -40, 49, 80, -54, -115, -98, -60, -123, 91, 14, 75, -86, 77, -93, 68, 112, 82, 79, 28, -25, 49, -27, -112, 103, 60, -128, 95, -63, 2, -51, 2, -107, 80, 113, 18, 123, 24, 70, 77, -56, -48, 33, 89, 88, 29, 112, -102, -15, 52, -96, 17, -9, 6, -11, -119, 29, -107, -84, -19, 84, 124, 19, 90, -60, -41, 123, -81, 96, -119, 17, -61, 62, 55, 95, -73, -13, -60, 56, 77, 24, -39, -107, -78, 47, -91, 88, 90, 34, 112, -80, 83, -58, 127, 76, -97, -40, 78, -20, -1, -62, 19, 6, -43, -46, -36, -53, -22, -28, -119, 8, 19, 79, -9, -54, -126, -3, -20, -110, -82, 51, 3, 1, -123, -41, -40, -11, 91, -52, 48, 104, -11, -2, 49, 45, 52, -33, 109, -44, -30, -44, -83, -108, -10, 77, 106, 82, 3, 14, -48, -18, -79, -64, -34, -63, -18, 122, 33, 25, 44, 82, -112, 111, 68, 97, -58, -38, 25, 62, 78, 97, -36, 57, -19, 122, -18, -74, 67, -127, -24, 32, -45, 67, -106, 90, 0, 1, 91, 30, -80, 95, 9, 78, -4, -14, 16, 111, -56, -102, -90, 52, -1, 116, 19, -127, -23, -87, 103, -94, -111, 118, 53, -69, 77, 17, -3, 31, -53, -21, -78, 124, -88, 52, 117, 34, -52, -77, -107, -38, -102, 23, 73, -76, -88, 95, 64, -85, 12, 36, -86, 86, -17, 77, 121, 90, 24, -49, -107, 33, -116, 65, 13, 91, 118, -107, -21, 65, -59, 18, 125, 61, -65, -68, -19, 23, 88, 60, -6, 78, -8, 69, -62, -118, -93, 97, -64, -67, 28, 28, -87, -97, 72, -125, -119, 4, -43, 7, 22, -15, 52, 52, -82, -5, -51, 99, 20, -59, -2, -54, -67, 40, -128, -20, -37, 50, 123, 32, 8, -39, -105, 93, 73, 77, 84, 43, 89, 88, -6, 7, -108, 81, 27, 1, 50, 16, -101, 67, 95, 119, 105, 70, 99, -127, 22, 127, -33, -19, -113, -55, -100, 122, -86, 98, 53, 27, -95, 4, -121, -96, 87, 67, -98, -37, -10, 92, 29, -3, -115, -23, 37, 8, -30, 99, -117, 62, 101, 83, 49, 60, -83, -47, -33, 41, -118, 76, -7, 111, -15, 123, -59, 53, 2, -20, -57, 24, 57, 62, 84, -26, -11, 93, -118, 54, -13, 56, 77, -66, 18, -62, -76, 80, 98, 26, 120, -93, 55, 103, -1, 78, -92, 120, -23, -60, -75, 11, 53, -62, -94, -80, 120, 113, 33, -24, -64, -5, 23, 120, -14, 61, 26, -1, 56, 79, 34, 116, -16, -95, -71, 40, -89, -50, 71, -117, -109, 2, -2, -34, 94, -78, -88, -27, 70, 94, -86, 123, -49, 107, -65, -67, 84, 90, 123, -61, -2, 43, -119, -93, 75, -4, -81, 98, -36, 125, -23, -37, 81, 104, 90, -63, -52, 88, -96, -44, 25, 3, -37, -123, -48, 113, -76, -94, -109, -115, 37, -39, 104, -124, 82, -73, 100, 48, -54, -40, -65, 81, 16, -85, -41, 60, 42, 117, 65, 77, 14, -8, -56, 52, -118, -109, 125, 13, 64, -20, 125, -37, -74, -28, 118, 112, -126, 18, -101, 11, 75, 30, -4, -121, -13, -65, -13, -122, -53, -52, 20, -2, 67, 18, -106, 67, 83, -111, 15, 106, 10, 113, 53, -112, -3, 118, 8, -56, 40, 53, 23, -123, 96, 87, -118, -97, -116, -47, 85, -73, -85, -82, 124, -55, 55, 61, 46, 12, -6, 34, 22, -22, 3, 115, -49, 102, 23, 46, 39, 0, 118, 3, -45, 48, -73, -38, 29, -36, 11, -127, -86, 30, 29, -2, -108, -114, 64, 110, 86, -46, -91, -64, 95, -40, -65, 49, -79, -126, -37, -103, -71, 53, -85, 45, -51, 33, -28, -126, 36, -77, -120, 55, -54, 72, -21, 58, -87, -73, 18, -12, 20, -100, 30, 118, -83, -22, -90, 71, -64, 108, 101, -46, 36, 105, -46, -91, 60, -113, 72, 100, 82, -90, 106, -127, 65, -94, 17, 77, -10, -112, 46, 118, 72, -84, 57, -86, -114, 88, 91, 79, 30, 107, -35, 61, 81, 71, 40, -29, -6, -107, 61, -62, -6, 65, -68, 118, 61, 110, -115, -119, -73, 104, 59, -66, -89, -127, -8, -67, 122, -38, 79, -13, 93, 1, -32, -47, -3, 62, 88, -112, 105, 73, 96, 73, -104, -126, -69, 21, -22, 16, -85, 116, 9, 82, 54, -15, -55, -67, 68, -23, 16, -89, 48, -17, -107, 60, -43, -34, 66, -114, 63, -3, -26, 68, 68, -86, 120, -111, 99, 61, 101, 27, 93, 31, 90, -33, -94, 29, -89, 41, -80, 26, -23, -80, 27, 107, 69, -45, -123, 62, 63, 80, 1, -28, 52, -8, 35, -86, -127, 76, 102, 83, -104, -79, -98, 77, -28, 118, 18, -15, -98, -39, 2, -58, 95, 64, 105, -82, -7, 96, 110, 104, 127, 126, -124, 26, 36, 33, -42, 59, 82, 127, 42, -24, -61, -50, -18, -87, 22, -32, -125, -70, 103, -121, -112, -94, 58, -95, -97, 53, 95, -61, -83, 42, 37, 80, 51, -118, 125, 15, 67, 41, -97, 41, -121, 29, -88, 100, -113, 39, 101, 47, 91, -36, 48, -56, -13, 12, 37, 0, 81, 3, -40, 8, 36, -65, -11, -32, 108, 62, 79, 70, 91, -83, 2, -47, 0, 91, 10, 87, -19, -40, 96, 106, 41, 120, -53, 40, -114, 90, 64, 59, -115, 39, 2, 53, -49, -72, -114, 94, 5, 49, 74, 13, 50, -14, 76, -123, -11, -81, 100, 120, 16, -41, -72, -118, 28, 41, 98, 122, 27, 18, -108, -43, 51, -71, 93, -13, -42, -64, -118, -106, 45, 108, 72, -128, 58, -123, -29, -114, 15, 52, -72, 108, -62, 75, -15, 105, -89, 25, 37, 13, -21, -109, 68, 5, -89, 69, 10, -46, 18, -57, 77, -103, -74, 57, -43, -110, 1, -80, 82, 5, -9, -49, -53, 83, 4, 44, 64, -117, -67, -11, 1, -65, -81, 34, -23, -71, 14, 105, -93, 2, -120, 90, 92, -6, -128, -16, -51, 27, 123, 71, -117, -72, -81, 26, 28, 5, -117, -30, 22, -72, -76, -32, -14, 82, 90, 69, 74, -94, -72, -30, -17, 12, -37, -3, -80, 72, 2, -40, 41, 0, -53, 48, -37, -117, -128, -120, -80, 28, 49, -52, 114, -119, 92, -42, -105, 125, -95, 78, 76, 123, -56, 32, -66, 69, -58, 57, -77, -100, -70, 125, 53, -115, 8, 116, 88, -34, 86, -75, 55, 64, 79, -113, -124, -91, 50, -82, -119, 50, 11, 87, -14, -25, 15, -1, -49, -127, -5, -50, 72, -29, -78, 101, -119, -21, -15, 97, -63, 57, -123, -94, -24, -8, 104, 86, 79, 49, 102, -8, -76, 8, 69, 99, -64, -108, 70, 36, 71, -127, 56, 39, 78, 109, 42, -42, -2, 126, 17, -88, -65, -23, -64, 78, 87, 7, 6, -82, -98, 41, -46, -10, -25, 90, -73, 24, 127, -27, 118, -9, 81, -3, 115, -4, 47, 86, -30, -9, -50, 32, 86, 114, 58, -5, 78, 74, 36, 29, -126, 116, 117, -114, -92, -121, -36, -86, -18, 55, 49, 112, 43, 111, -99, -116, 70, 60, -63, 87, -4, -35, 15, 28, -27, -65, 66, 115, -33, 112, 94, 74, -22, 104, -56, -27, 39, -8, -53, -120, 8, -109, 73, -68, 67, 40, -59, 59, 121, -76, -41, -80, -54, -88, -120, -121, -118, -58, 74, -120, 82, -88, -113, 30, -8, 54, -126, -106, 37, -43, -74, -56, 40, -76, 93, 91, 28, -59, -30, -2, 107, 6, -89, -69, -121, -125, -109, 5, -94, -7, -2, -5, -67, 54, -90, 39, 5, -80, 93, -99, 82, -100, -128, -8, -39, -109, 66, -11, 99, -41, 18, -32, -122, 69, 6, -95, -21, 9, 19, -117, -34, -42, 11, 20, 84, 89, 91, -61, -13, -7, 55, 90, -15, 62, 59, -4, 125, -127, -24, -124, -99, -63, -23, 52, 111, -52, -60, -113, -65, -26, 127, 57, 21, 102, 101, -77, 66, -116, 117, 80, 7, 1, -96, -29, -99, 75, -73, 44, -99, 61, -73, 15, -18, 89, 95, 104, -12, 94, 33, 13, -49, 118, -84, -122, -2, -121, 62, -32, -80, 11, -10, 102, -67, 20, -3, 25, -6, 51, -17, -123, -76, 103, 3, 127, -107, -5, 122, 65, 22, 113, 120, 6, -19, -110, 86, 55, -88, -124, 0, -54, 17, 112, 15, 105, -28, 111, -93, 85, -59, -88, 28, 123, 55, 117, 10, 76, 54, -98, 116, 40, -65, -53, -80, 46, 66, -8, -114, 102, 66, 67, -117, 46, 21, -116, -38, 58, -105, 101, 37, -16, 5, 55, -33, -87, 72, 122, -114, -91, 41, -114, 77, 50, 109, 35, -61, 9, -55, -118, 126, -35, -108, 5, 62, 125, -109, -115, -55, 32, -71, 69, 110, 87, -82, 119, 26, 103, -77, -38, -13, 113, 74, 69, 116, 94, -21, 5, 35, 73, -80, -87, 80, 13, 108, 1, 82, -56, -35, -21, -78, -98, 121, 112, -117, 72, 47, 76, -97, -84, -110, -35, -19, -120, -13, 127, 5, 56, 72, -22, 110, -8, -71, 0, -57, -125, -101, 60, -64, -32, 1, 126, -109, 9, 84, 117, 62, -68, -106, 28, -118, -52, -81, 112, 11, 55, 68, -86, -65, 123, 83, 55, -72, 110, 63, -90, 31, 11, 90, -60, 20, 14, -36, 5, -92, 11, -100, 64, -57, -72, -105, 7, 103, 125, 99, -88, 32, -5, 41, -115, -11, 89, 81, 77, -33, -7, -123, -17, 109, 59, 40, -12, -61, 98, -91, 19, -36, 108, 118, -124, -82, -40, -124, -66, 19, 127, -73, -39, 99, 43, -16, -44, -83, -77, -34, 68, -118, -71, -116, 114, 120, -34, -105, -32, -46, 102, 73, -79, 7, 42, 35, -66, 125, 34, 113, 66, 78, 71, 6, 44, -17, 4, -80, 38, -59, 12, -8, -78, 103, 8, 80, 18, -74, 20, 3, 56, -20, 106, -1, -12, 83, 4, 68, -119, 84, -87, 97, -53, 102, 119, 34, -85, 22, -26, 55, -107, 96, -70, 77, -68, -96, -15, -22, -77, -55, 5, 103, -42, -87, 122, -80, -103, -37, -120, -56, -16, -51, -7, -19, -104, 120, 9, 54, -85, 48, -76, -38, 58, -68, 116, -20, -44, 22, -32, 75, -46, -41, 13, -100, 16, -59, -93, -115, 54, 22, -110, -46, -119, 44, -98, -48, 4, -58, -115, -57, 103, -56, 36, -63, 104, -114, -125, 92, 65, 117, -21, -59, -31, 56, -98, -126, 56, 47, -116, 100, 122, -98, 4, 26, -29, -127, -113, 73, 48, 106, 125, -69, -127, 62, 56, -79, 76, 84, -46, -31, -17, 94, -98, 62, 63, 118, -24, 63, 123, -93, -46, 103, 117, -120, -35, 19, 25, 15, -110, -125, 12, -75, -50, 103, 49, 47, 98, 92, 10, -88, 54, -53, 19, 25, -90, 93, -49, 64, 126, -106, -30, -52, 58, 37, 68, -18, -60, 15, -27, 93, -124, 88, 110, -80, -106, 88, 55, 108, -58, -43, -70, 76, 85, 98, 27, -66, 18, 75, 69, 114, 90, -26, -10, -12, -126, 84, -109, 108, 15, -115, 90, 11, -127, 63, -7, 47, 92, -72, 38, -58, -35, 18, 25, 12, -103, 0}, params);
        kp = new EncryptionKeyPair(priv, pub);
        plainText = "secret encrypted text".getBytes();
        encrypted = NtruEncrypt.encrypt(plainText, kp.pub, params);
        decrypted = NtruEncrypt.decrypt(encrypted, kp, params);
        assertArrayEquals(plainText, decrypted);
    }
}