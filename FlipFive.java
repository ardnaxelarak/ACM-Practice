import java.util.*;
public class FlipFive
{
	public static void main(String[] args)
	{
		short[] masks = new short[] {0x1a0, 0x1d0, 0x0c8,
									 0x134, 0x0ba, 0x059,
									 0x026, 0x017, 0x00b};
		int[] values = new int[512];
		short[] results = new short[512];
		int[] dist = new int[512];
		short cur = 1;
		for (int j = 0; j < 512; j++)
		{
			results[j] = 0;
			dist[j] = 0;
			values[j] = Integer.MAX_VALUE;
		}

		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 512; j++)
			{
				if ((j & cur) == 0)
					continue;
				results[j] ^= masks[i];
				dist[j]++;
			}
			cur <<= 1;
		}

		for (int j = 0; j < 512; j++)
		{
			short k = results[j];
			if (dist[j] < values[k])
				values[k] = dist[j];
		}

		for (int j = 0; j < 512; j++)
		{
			if (dist[j] > 9)
				System.err.println(j);
		}


		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		for (int i = 0; i < n; i++)
		{
			String casestr = sc.next() + sc.next() + sc.next();
			casestr = casestr.replace('*', '1').replace('.', '0');
			short casenum = Short.parseShort(casestr, 2);
			System.out.println(values[casenum]);
		}
	}
}
