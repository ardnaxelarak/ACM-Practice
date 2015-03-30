import java.util.*;
import static java.lang.Math.*;

public class PrimalPartitions
{
	static final int MAX_NUM = 1000000;
	static int[][] D, F;
	static int n, k;
	static int[] hp;
	static int[] list;

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

	public static void calcPrimes()
	{
		hp = new int[MAX_NUM + 1];
		int cur;

		hp[1] = 0;

		for (int i = 2; i <= MAX_NUM; i++)
			hp[i] = i;

		for (int i = 2; i <= MAX_NUM; i++)
		{
			if (hp[i] == i)
			{
				for (int j = 2 * i; j <= MAX_NUM; j += i)
				{
					cur = hp[j];
					while (cur > i && cur % i == 0)
						cur /= i;
					hp[j] = cur;
				}
			}
		}
	}

	public static int getGCD(int start, int end)
	{
		int res;
		assert end >= start;
		if (start < 0)
			return 1;

		if (D[end][start] != -1)
			return D[end][start];

		res = gcd(list[start], getGCD(start + 1, end));
		D[end][start] = res;

		// System.err.printf("gcd(%3d, %3d) = %d\n", start, end, res);

		return res;
	}

	public static int getScore(int part, int len)
	{
		// System.err.printf("computing F(%d, %d)\n", part, len);
		assert part > 0;
		assert len > 0;

		if (part > len)
			part = len;

		if (F[part][len] != -1)
			return F[part][len];

		int cur, next;
		int score;
		int best = 0;

		next = list[len - 1];

		for (int i = len - 1; i >= 0; i--)
		{
			// System.err.printf("i = %d\n", i);
			cur = next;
			// System.err.printf("cur = %d\n", cur);
			if (cur == 1)
				break;

			next = getGCD(i - 1, len - 1);
			// System.err.printf("next = %d\n", next);
			if (hp[cur] == hp[next])
				continue;

			score = min(hp[cur], getScore(part - 1, i));
			best = max(best, score);
		}

		F[part][len] = best;

		// System.err.printf("  F(%3d, %3d) = %d\n", part, len, best);

		return best;
	}

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);

		n = sc.nextInt();
		k = sc.nextInt();

		calcPrimes();

		D = new int[n][]; // gcd(l_j, ..., l_i)
		F = new int[k + 1][n + 1];

		for (int i = 2; i <= k; i++)
			Arrays.fill(F[i], -1);

		list = new int[n];
		for (int i = 0; i < n; i++)
			list[i] = sc.nextInt();

		D[0] = new int[] {list[0]};
		F[1][1] = hp[list[0]];

		for (int i = 1; i < n; i++)
		{
			D[i] = new int[i + 1];
			D[i][i] = list[i];
			Arrays.fill(D[i], 0, i, -1);
		}

		int cur, last;
		cur = list[0];
		for (int i = 1; i < n; i++)
		{
			last = cur;
			cur = gcd(list[i], last);
			D[i][0] = cur;
			F[1][i + 1] = hp[cur];
		}

		System.out.println(getScore(k, n));
	}
}
