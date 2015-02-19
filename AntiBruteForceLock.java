import java.util.*;
import static java.lang.Math.*;

public class AntiBruteForceLock
{
    private static int getDist(int[][] digdist, int v1, int v2)
    {
        int dist = 0;
        int cur1, cur2;
        cur1 = v1;
        cur2 = v2;

        for (int k = 0; k < 4; k++)
        {
            dist += digdist[cur1 % 10][cur2 % 10];
            cur1 /= 10;
            cur2 /= 10;
        }

        return dist;
    }

    public static void main(String[] args)
    {
        final int MAX_KEYS = 500;

        /* precompute distance between each pair of digits
         * not strictly necessary, but should improve
         * performance somewhat in files with many or large
         * test cases */
        int[][] digdist = new int[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                digdist[i][j] = min(abs(i - j), 10 - abs(i - j));

        Scanner sc = new Scanner(System.in);
        int[] values = new int[MAX_KEYS];
        /* track closest path to each key */
        int[] shortDist = new int[MAX_KEYS];
        /* track whether we've already inputted each key */
        boolean[] conn = new boolean[MAX_KEYS];
        int N, total;
        int cur, next, weight;
        int T = sc.nextInt();

        for (int i = 0; i < T; i++)
        {
            N = sc.nextInt();
            total = 1000;

            /* read in values */
            for (int j = 0; j < N; j++)
            {
                values[j] = sc.nextInt();

                /* check for shortest key from 0000 */
                total = min(total, getDist(digdist, 0, values[j]));
            }

            Arrays.fill(conn, 0, N, false);
            Arrays.fill(shortDist, 0, N, Integer.MAX_VALUE);

            /* use Prim's algorithm to find minimum-cost spanning tree */

            /* start with key 0 (arbitrarily) */
            cur = 0;
            for (int j = 1; j < N; j++)
            {
                /* mark current key as visited */
                conn[cur] = true;
                next = 0;

                weight = Integer.MAX_VALUE;

                /* look for closest new key */
                for (int k = 0; k < N; k++)
                {
                    /* ignore previously visited keys */
                    if (conn[k])
                        continue;

                    /* update shortDist[k] if shorter to visit key k
                     * from current key than previous keys */
                    shortDist[k] = min(shortDist[k], getDist(digdist, values[cur], values[k]));

                    /* remember key k if best we've found */
                    if (shortDist[k] < weight)
                    {
                        weight = shortDist[k];
                        next = k;
                    }
                }

                /* add weight to go to next key and set current key */
                total += weight;
                cur = next;
            }

            /* print out total rolls */
            System.out.println(total);
        }
    }
}
