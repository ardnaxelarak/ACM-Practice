import java.util.*;
import static java.lang.Math.*;

public class VendingMachine
{
    public static void main(String[] args)
    {
		Scanner sc = new Scanner(System.in);

		int n = sc.nextInt();

		int[] f = new int[n];
		int[] p = new int[n];
		int[] m = new int[n];
		int[] s = new int[n];
		LinkedList<Integer>[] in = new LinkedList[n];

		for (int i = 0; i < n; i++)
		{
			in[i] = new LinkedList<Integer>();
		}

		for (int i = 0; i < n; i++)
		{
			f[i] = sc.nextInt() - 1;
			in[f[i]].add(i);
			p[i] = sc.nextInt();
			m[i] = sc.nextInt();
			s[i] = sc.nextInt();
		}

		int[] profit = new int[n];

		for (int i = 0; i < n; i++)
		{
			profit[i] = m[f[i]] - p[i];
		}

		int total = 0;
		int best;
		for (int i = 0; i < n; i++)
		{
			best = 0;
			for (int old : in[i])
			{
				best = max(best, profit[old]);
			}
			total += best * (s[i] - 1);
		}

		LinkedList<Integer> Q = new LinkedList<Integer>();
		for (int i = 0; i < n; i++)
		{
			if (f[i] == i)
				Q.add(i);
			else if (profit[i] <= 0)
				Q.add(i);
			else if (in[i].isEmpty())
				Q.add(i);
		}

		boolean[] care = new boolean[n];
		Arrays.fill(care, true);

		int cur, index;
		while (!Q.isEmpty())
		{
			index = Q.poll();
			// System.err.println(index);
			care[index] = false;
			in[f[index]].remove(Integer.valueOf(index));
			if (care[f[index]] && in[f[index]].isEmpty())
				Q.add(f[index]);
			cur = profit[index];
			if (cur > 0)
			{
				total += cur;
				// System.err.printf("%d -> %d: %d\n", index + 1, f[index] + 1, cur);
				for (int val : in[f[index]])
				{
					profit[val] -= cur;
					if (profit[val] <= 0 && care[val])
						Q.add(val);
				}
			}
		}

		for (int i = 0; i < n; i++)
		{
			if (!care[i])
				continue;
			
			best = profit[i];
			// System.err.printf("%d -> %d: %d\n", i + 1, f[i] + 1, profit[i]);
			total += best;
			cur = f[i];
			while (cur != i)
			{
				if (profit[cur] > 0)
				{
					// System.err.printf("%d -> %d: %d\n", cur + 1, f[cur] + 1, profit[cur]);
					total += profit[cur];
					best = min(best, profit[cur]);
				}
				else
				{
					best = 0;
				}
				care[cur] = false;
				cur = f[cur];
			}
			total -= best;
		}

		System.out.println(total);
    }
}
