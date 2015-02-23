import java.util.Scanner;
import java.math.BigInteger;

public class Factorial500
{
    public static void main(String[] args)
    {
        final int MAX_INPUT = 1000;

        Scanner sc = new Scanner(System.in);
        int n;

        BigInteger[] factorials = new BigInteger[MAX_INPUT + 1];
        factorials[0] = BigInteger.ONE;

        for (int i = 1; i <= MAX_INPUT; i++)
            factorials[i] = factorials[i - 1].multiply(BigInteger.valueOf(i));

        while (sc.hasNextInt())
        {
            n = sc.nextInt();
            System.out.printf("%d!\n%d\n", n, factorials[n]);
        }

        /* slower but still fast enough
        while (sc.hasNextInt())
        {
            n = sc.nextInt();
            BigInteger fact = BigInteger.ONE;
            for (int i = 2; i <= n; i++)
                fact = fact.multiply(BigInteger.valueOf(i));

            System.out.printf("%d!\n%d\n", n, fact);
        }
        */
    }
}
