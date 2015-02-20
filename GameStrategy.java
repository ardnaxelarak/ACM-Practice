import java.util.*;
import static java.lang.Math.*;

public class GameStrategy
{
    /* parse string of option set into int array of options */
    public static int[] getOptions(String options, int pos)
    {
        int[] ret = new int[options.length()];
        int k = 0;
        int cur;
        for (char c : options.toCharArray())
        {
            cur = c - 'a';
            if (cur == pos)
                return null;
            else
                ret[k++] = c - 'a';
        }
        return ret;
    }

    public static void main(String[] args)
    {
        final int MAX_POS = 25;

        Scanner sc = new Scanner(System.in);
        int n, m, k;
        boolean good;
        int[][][] options;
        LinkedList<int[]> curset = new LinkedList<int[]>();
        int[] curopt;
        int[][] path = new int[MAX_POS][MAX_POS];

        while (sc.hasNextInt())
        {
            n = sc.nextInt();

            options = new int[n][][];
            for (int i = 0; i < n; i++)
            {
                /* initialize solution array */
                Arrays.fill(path[i], Integer.MAX_VALUE);
                path[i][i] = 0;

                /* read input data */
                m = sc.nextInt();
                curset.clear();
                for (int j = 0; j < m; j++)
                {
                    curopt = getOptions(sc.next(), i);
                    if (curopt == null)
                        continue;

                    if (curopt.length == 1)
                        path[i][curopt[0]] = 1;

                    curset.add(curopt);
                }
                options[i] = new int[curset.size()][];
                options[i] = curset.toArray(options[i]);
            }

            /* searching for start/end goals Alice can force in num moves */
            for (int num = 2; num < n; num++)
            {
                for (int i = 0; i < n; i++)
                {
                    for (int j = 0; j < n; j++)
                    {
                        /* ignore goals for which we've already 
                         * found a shorter path */
                        if (path[i][j] < Integer.MAX_VALUE)
                            continue;

                        for (int[] optset : options[i])
                        {
                            good = true;

                            /* look for an option set in which every choice
                             * Bob makes leads to a win for Alice in num - 1
                             * moves */
                            for (int opt : optset)
                            {
                                if (path[opt][j] > num - 1)
                                {
                                    /* Bob has an option that will take longer
                                     * than num - 1 additional moves for Alice
                                     * to win */
                                    good = false;
                                    break;
                                }
                            }

                            if (good)
                            {
                                /* Every option Bob takes leads to a win for
                                 * Alice in num - 1 additional moves, or num
                                 * total */
                                path[i][j] = num;
                                break;
                            }
                        }
                    }
                }
            }
            
            /* replace out default MAX_VALUE for not found with -1 */
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
}
