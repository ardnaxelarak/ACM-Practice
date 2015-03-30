import java.util.*;
import static java.lang.Math.*;

public class ZigZag
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		int k = sc.nextInt();
		int len = (k - 1) / 25 + 2;
		if (len == 2)
			System.out.printf("a%c\n", (char)('a' + k));
		else
		{
			int delta = k - 25 * len + 75;
			System.out.printf("a%c", (char)('a' + (delta + 1) / 2));
			for (int i = 3; i < len; i++)
			{
				if (i % 2 == 0)
					System.out.print("z");
				else
					System.out.print("a");
			}
			if (len % 2 == 0)
				System.out.printf("%c", (char)('z' - (delta % 2)));
			else
				System.out.printf("%c", (char)('a' + (delta % 2)));
			System.out.println();
		}
	}
}
