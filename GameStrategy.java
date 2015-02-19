import java.util.*;
import static java.lang.Math.*;

public class GameStrategy
{
    public static int[] getOptions(String options)
    {
        int[] ret = new int[options.length()];
        int k = 0;
        for (char c : options.toCharArray())
            ret[k++] = c - 'a';
        return ret;
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m;
        boolean good;

        int[][][] options = new int[n][][];
        int[][] path = new int[n][n];
        for (int i = 0; i < n; i++)
        {
            Arrays.fill(path[i], Integer.MAX_VALUE);
            path[i][i] = 0;
            m = sc.nextInt();
            options[i] = new int[m][];
            for (int j = 0; j < m; j++)
            {
                options[i][j] = getOptions(sc.next());
            }
        }

        for (int num = 1; num <= 25; num++)
        {
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    if (path[i][j] < Integer.MAX_VALUE)
                        continue;

                    for (int[] optset : options[i])
                    {
                        good = true;

                        for (int opt : optset)
                        {
                            if (path[opt][j] > num - 1)
                            {
                                good = false;
                                break;
                            }
                        }
                        if (good)
                        {
                            path[i][j] = num;
                            break;
                        }
                    }
                }
            }
        }
        
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (path[i][j] == Integer.MAX_VALUE)
                    path[i][j] = -1;

        /* print out solution */
        for (int i = 0; i < n; i++)
        {
            System.out.print(path[i][0]);
            for (int j = 1; j < n; j++)
            {
                System.out.printf(" %d", path[i][j]);
            }
            System.out.println();
        }
    }
}
