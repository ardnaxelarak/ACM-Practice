import java.util.*;
import static java.lang.Math.*;

public class AntiBruteForceLock
{
    private static final int[][] digdist = calcDists();
    private static final int[][] digs = calcDigs();

    private static int[][] calcDigs()
    {
        int[][] digs = new int[10000][4];
        int cur;
        for (int i = 0; i < 10000; i++)
        {
            cur = i;
            for (int k = 0; k < 4; k++)
            {
                digs[i][k] = cur % 10;
                cur /= 10;
            }
        }
        return digs;
    }

    private static int[][] calcDists()
    {
        int[][] digdist = new int[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                digdist[i][j] = min(abs(i - j), 10 - abs(i - j));

        return digdist;
    }

    private static int getDist(int v1, int v2)
    {
        int dist = 0;
        for (int i = 0; i < 4; i++)
            dist += digdist[digs[v1][i]][digs[v2][i]];
        return dist;
    }

    public static void main(String[] args)
    {
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
                total = min(total, getDist(0, values[j]));
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
                    shortDist[k] = min(shortDist[k], getDist(values[cur], values[k]));
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
