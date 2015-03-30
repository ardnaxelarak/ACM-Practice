import java.util.*;
import static java.lang.Math.*;

public class PrimalPartitions
{
	public static int gcd(int a, int b)
	{
		if (b > a)
			return gcd(b, a);
		int rem = a % b;
		while (rem > 0)
		{
			a = b;
			b = rem;
			rem = a % b;
		}
		return b;
	}

    public static void main(String[] args)
    {
		Scanner sc = new Scanner(System.in);

		int n = sc.nextInt();
		int k = sc.nextInt();

		final int MAX_NUM = 1000000;
		int[] hp = new int[MAX_NUM + 1];
		LinkedList<Integer> tries = new LinkedList<Integer>();
		int cur, factor;
		ListIterator<Integer> li;

		hp[1] = 0;
		hp[2] = 2;

		for (int i = 4; i <= MAX_NUM; i++)
		{
			hp[i] = 2;
			tries.add(i + 1);
		}

		for (int i = 3; i <= MAX_NUM; i++)
		{
			if (hp[i] > 0)
			{
				factor = hp[i];
				hp[i] = hp[i / factor];
			}
			else
			{
				tries.poll();
				li = tries.listIterator();
				while (li.hasNext())
				{
					cur = li.next();
					if (cur % i == 0)
					{
						li.remove();
						hp[cur] = i;
					}
				}
				hp[i] = i;
			}
		}

		int[][] D = new int[n][]; // gcd(l_j, ..., l_i)
		int[][] F = new int[k + 1][n];

		int[] list = new int[n];
		for (int i = 0; i < n; i++)
			list[i] = sc.nextInt();

		D[0] = new int[] {list[0]};

		for (int i = 1; i < n; i++)
		{
			D[i] = new int[i + 1];
			D[i][i] = list[i];
			D[i][i - 1] = gcd(list[i], list[i - 1]);
			for (int j = i - 2; j >= 0; j--)
			{
				D[i][j] = gcd(D[i][j + 1], D[i - 1][j]);
			}
		}

		for (int i = 1; i < n; i++)
		{
			for (int j = 0; j <= i; j++)
			{
				D[i][j] = hp[D[i][j]];
			}
			// System.err.printf("D[%3d]: %s\n", i, Arrays.toString(D[i]));
		}

		for (int i = 0; i < n; i++)
			F[1][i] = D[i][0];

		// System.err.printf("F[%3d]: %s\n", 1, Arrays.toString(F[1]));
		int max;

		for (int i = 2; i <= k; i++)
		{
			for (int j = i - 1; j < n; j++)
			{
				max = 0;
				for (int l = i - 1; l < j; l++)
				{
					max = max(max, min(F[i - 1][l], D[j][l + 1]));
				}
				F[i][j] = max;
			}
			// System.err.printf("F[%3d]: %s\n", i, Arrays.toString(F[i]));
		}

		System.out.println(F[k][n - 1]);
    }
}
