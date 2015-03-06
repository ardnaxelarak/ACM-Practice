import java.util.*;
import java.awt.Point;

public class SelfAssembly
{
    public static int getConnector(char a, char b)
    {
        if (a == '0')
            return -1;
        else
            return ((a - 'A') << 1) + (b == '+' ? 1 : 0);
    }

    public static int[][] getTiles(int count, char[] line)
    {
        int[][] tiles = new int[count][];
        int[] cur = new int[4];

        int k;
        int pos = 0;
        int curConn;
        for (int i = 0; i < count; i++)
        {
            k = 0;
            for (int j = 0; j < 4; j++)
            {
                curConn = getConnector(line[pos++], line[pos++]);
                if (curConn >= 0)
                    cur[k++] = curConn;
            }

            tiles[i] = new int[k];
            for (int j = 0; j < k; j++)
                tiles[i][j] = cur[j];

            pos++;
        }

        return tiles;
    }

    static String connectorString(int i)
    {
        return (char)((i >> 1) + 'A') + (((i & 1) == 1) ? "+" : "-");
    }

    static boolean update(boolean[][] conn, LinkedList<Point> list, int i1, int i2)
    {
        if (i1 == i2)
            return true;

        if (conn[i1][i2])
            return false;

        // System.err.printf("Connecting %s to %s\n", connectorString(i1), connectorString(i2));

        conn[i1][i2] = true;
        list.add(new Point(i1, i2));

        return false;
    }

    public static boolean isUnbounded(int[][] tiles)
    {
        boolean[][] conn = new boolean[52][52];
        LinkedList<Point> list = new LinkedList<Point>();

        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                for (int k = j + 1; k < tiles[i].length; k++)
                {
                    if (update(conn, list, tiles[i][j], tiles[i][k] ^ 1))
                        return true;
                    if (update(conn, list, tiles[i][k], tiles[i][j] ^ 1))
                        return true;
                }
            }
        }

        Point p;
        int i1, i2;

        while (!list.isEmpty())
        {
            p = list.removeFirst();
            i1 = p.x;
            i2 = p.y;

            for (int k = 0; k < 52; k++)
            {
                if (conn[k][i1] && update(conn, list, k, i2))
                    return true;
                if (conn[i2][k] && update(conn, list, i1, k))
                    return true;
            }
        }

        return false;
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int n;

        while (sc.hasNextInt())
        {
            n = sc.nextInt();
            sc.nextLine();
            if (isUnbounded(getTiles(n, sc.nextLine().toCharArray())))
                System.out.println("unbounded");
            else
                System.out.println("bounded");
        }
    }
}
