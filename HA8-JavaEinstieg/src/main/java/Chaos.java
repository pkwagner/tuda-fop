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
     * Calculates the gcd (greatest common divisor) using the extended euclid algorithm.
     * <p>
     * It takes two integers and recursively invokes itself until it found the gcd of those two numbers.
     * Then it returns it in an array together with two real numbers s and t for the formula:
     * <p>
     * gcd(a, b) = s * a + t * b
     *
     * @param a the first number that should be dividable by the result
     * @param b the second number that should be dividable by the result
     * @return int array of three components where i=0 for the gcd, i=1->s and i=2->t
     */
    private static int[] extendedEuclid(int a, int b) {
        if (b == 0) {
            return new int[]{a, 1, 0};
        }

        int[] result = extendedEuclid(b, a % b);
        return new int[]{result[0], result[2], result[1] - (a / b) * result[2]};
    }

    /**
     * Decrypts the given base input using Fermat's algorithm with the formula:
     * <p>
     * message = base^exp % n
     *
     * @param base the encrypted input
     * @param exp  secret/private key
     * @param mod  the common modulus
     * @return the decrypted part of the input
     */
    private static char decryptChar(int base, int exp, int mod) {
        return (char) (Math.pow(base, exp) % mod);
    }

    public static void main(String[] args) {
        int prime1 = 3;
        int prime2 = 11;
        int n = prime1 * prime2;
        int phi = (prime1 - 1) * (prime2 - 1);

        int publicKey = 7;
        int privateKey = extendedEuclid(publicKey, phi)[1];

        char[] encrypted = new char[]{14, 20, 27, 24, 8, 20, 23, 23};
        char[] decrypted = encrypted.clone();

        for (int i = 0; i < encrypted.length; i++) {
            decrypted[i] = (char) (decryptChar(encrypted[i], privateKey, n) + 'A');
        }

        //print it!
        System.out.println(decrypted);
    }
}