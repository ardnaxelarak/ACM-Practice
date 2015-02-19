import java.util.*;
import static java.lang.Math.*;

public class Baggage
{
    public static void printBins(int[] bins)
    {
        for (int i = 0; i < bins.length; i++)
            System.err.printf(" %c ", 'A' + bins[i] - 1);
        System.err.println();
        for (int i = 0; i < bins.length; i++)
            System.err.printf("%2d ", i - 1);
        System.err.println();
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int[] bins = new int[2 * n + 2];

        bins[0] = 0;
        bins[1] = 0;
        for (int i = 0; i < n; i++)
        {
            bins[2 * i + 2] = 2;
            bins[2 * i + 3] = 1;
        }

        printBins(bins);

        int cur = 0;
        int next;

        while (sc.hasNextInt())
        {
            next = sc.nextInt() + 1;
            bins[cur] = bins[next];
            bins[cur + 1] = bins[next + 1];
            bins[next] = 0;
            bins[next + 1] = 0;
            printBins(bins);
            cur = next;
        }
    }
}
