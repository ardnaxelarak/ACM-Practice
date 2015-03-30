import java.util.*;
import static java.lang.Math.*;

public class CoconutsRevisited2
{
    static boolean checkValue(int start, int num)
    {
        int cur = start;
        for (int i = 0; i < num; i++)
        {
            cur--;
            if ((cur % num) != 0)
            {
                return false;
            }
            cur -= cur / num;
        }

        return (cur % num) == 0;
    }

    static int getValue(int start)
    {
        for (int i = 20; i > 1; i--)
            if (checkValue(start, i))
                return i;

        return -1;
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        int value, ans;

        value = sc.nextInt();

        while (value >= 0)
        {
            ans = getValue(value);

            if (ans > 0)
            {
                System.out.printf("%d coconuts, %d persons and 1 monkey\n", value, ans);
            }
            else
            {
                System.out.printf("%d coconuts, no solution\n", value);
            }

            value = sc.nextInt();
        }
    }
}
