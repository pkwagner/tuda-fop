/**
 * Decrypts a encrypted hardcoded string using given prime numbers
 * and the public key. Then it display the decrypted part on the command line.
 * <p>
 * Optimized code for Schematic Inc written in 2016
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class Chaos {

	/**
	 * Finds the greatest common divisor (gcd) using extended Euklid algorithm
	 * 
	 * @param i First number
	 * @param j Second number
	 * @return GCD
	 */
	private static int[] gcd (int i, int j) {
		if (j == 0)
			return new int[] { i, 1, 0 };
		int[] k = gcd(j, i % j);
		return new int[] { k[0], k[2], k[1] - (i / j) * k[2] };
	}

	/**
	 * Decrypts an encrypted char using RSA
	 * 
	 * @param i Char that should be decrypted
	 * @param j PrivateKey as an integer
	 * @param k Shared n (also part of the private key)
	 * @return Decrypted char
	 */
	private static char decryptChar (int i, int j, int k) {
		return (char) (Math.pow(i, j) % k);
	}

	/**
	 * Generates a new RSA key out of given values and
	 * decrypts a predefined message with it.
	 * 
	 * @param l Default execution commands (ignored)
	 * @return A predefined plain text
	 */
	public static void main(String[] l) {
		int p = 3, q = 11, n = p * q, phi = (p - 1) * (q - 1);
		
		int publicKey = 7;
		int privateKey = gcd(publicKey, phi)[1];
		
		char[] message_encrypted = new char[] { 14, 20, 27, 24, 8, 20, 23, 23 }, message = message_encrypted.clone();
		for (int i = 0; i < message_encrypted.length; i++)
			message[i] = (char) (decryptChar(message_encrypted[i], privateKey, n) + 'A');// print it!
		System.out.println(message);
	}
}