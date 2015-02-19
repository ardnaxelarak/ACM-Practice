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
        /* precompute distance between each pair of digits
         * not strictly necessary, but should improve performance slighty */
        int[][] digdist = new int[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                digdist[i][j] = min(abs(i - j), 10 - abs(i - j));

        Scanner sc = new Scanner(System.in);
        int[] values = new int[500];
        int[] shortDist = new int[500];
        boolean[] conn = new boolean[500];
        int N, total;
        int cur, next, weight;
        int T = sc.nextInt();

        for (int i = 0; i < T; i++)
        {
            N = sc.nextInt();
            total = 1000;
            for (int j = 0; j < N; j++)
            {
                values[j] = sc.nextInt();
                total = min(total, getDist(digdist, 0, values[j]));
            }

            Arrays.fill(conn, 0, N, false);
            Arrays.fill(shortDist, 0, N, Integer.MAX_VALUE);

            cur = 0;
            for (int j = 1; j < N; j++)
            {
                conn[cur] = true;
                next = 0;
                weight = Integer.MAX_VALUE;
                for (int k = 0; k < N; k++)
                {
                    if (conn[k])
                        continue;
                    shortDist[k] = min(shortDist[k], getDist(digdist, values[cur], values[k]));
                    if (weight > shortDist[k])
                    {
                        weight = shortDist[k];
                        next = k;
                    }
                }
                total += weight;
                cur = next;
            }

            System.out.println(total);
        }
    }
}
