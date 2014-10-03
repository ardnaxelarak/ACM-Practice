import java.util.*;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Long.bitCount;
public class YouWin
{
	static final long WEIGHT_MASK = 0xFFFFl;
	static final long POS_MASK = 0xFF0000l;
	static final long LETTER_MASK = 0xFFFFF000000l;
	static final long FIRST_MASK = 0xFF00000000000l;
	static class StateComparator implements Comparator<Long>
	{
		public int compare(Long o1, Long o2)
		{
			long l1 = o1.longValue(), l2 = o2.longValue();
			long val = (l1 & WEIGHT_MASK) - (l2 & WEIGHT_MASK);
			if (val > 0)
				return 1;
			if (val < 0)
				return -1;
			return 0;
		}
	}

	static long newItem(long curState, String word, int pos, long mask)
	{
		int curCost = (int)(curState & WEIGHT_MASK);
		int curPos = (int)((curState & POS_MASK) >>> 16);
		long curBits = (curState & LETTER_MASK);
		// long firstLetter = (curState & FIRST_MASK);
		// System.err.printf("%4d %2d %2d %20s\n", curCost, curPos, pos, Long.toBinaryString(curBits >>> 24));
		char newLet = word.charAt(pos);
		char curLet = word.charAt(curPos);
		if (curBits == 0)
		{
			curLet = 'A';
			// firstLetter = (long)pos << 44;
		}
		int dist = newLet - curLet;
		if (dist < 0)
			dist += 26;
		if (dist > 13)
			dist = 26 - dist;
		long moveMask = ((1l << abs(curPos - pos)) - 1) << (43 - max(pos, curPos));
		int move = bitCount(curState & moveMask);
		// System.err.printf("%2d %d\n", move, dist);
		int newCost = curCost + dist + move + 1;
		// return firstLetter | curBits | mask | (pos << 16) | newCost;
		return curBits | mask | (pos << 16) | newCost;
	}

	static String toString(long state, String word)
	{
		int cost = (int)(state & WEIGHT_MASK);
		int pos = (int)((state & POS_MASK) >> 16);
		long mask = 1l << 43;
		String res = "";
		for (int i = 0; i < word.length(); i++)
		{
			if ((state & mask) != 0)
				res += word.charAt(i);
			if (pos == i)
				res += "|";
			mask >>>= 1;
		}
		return String.format("%4d %s", cost, res);
	}

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		String word = sc.next();
		PriorityQueue<Long> q;
		int n;
		long cur;
		int solutionValue;
		long sol;
		StateComparator comp = new StateComparator();
		long checkmask;
		while (!word.equals("0"))
		{
			n = word.length();
			solutionValue = Integer.MAX_VALUE;
			sol = 0l;
			checkmask = ~(((1l << n) - 1) << (44 - n));
			q = new PriorityQueue<Long>(1 << n, comp);
			q.add(0l);
			while (!q.isEmpty())
			{
				cur = q.poll().longValue();
				// System.err.printf("<<< %s >>>\n", toString(cur, word));
				if ((cur & WEIGHT_MASK) > solutionValue)
					break;
				long mask = 1l << 43;
				for (int i = 0; i < n; i++)
				{
					if ((cur & mask) == 0)
					{
						long newitem = newItem(cur, word, i, mask);
						if ((newitem & WEIGHT_MASK) < solutionValue)
						{
							if ((newitem | checkmask) == -1l)
							{
								solutionValue = (int)(newitem & WEIGHT_MASK);
								// sol = newitem;
								// System.err.printf("Found solution %d\n", solutionValue);
							}
							else
							{
								q.add(newitem);
							}
							// System.err.printf("    %s\n", toString(newitem, word));
						}
					}
					
					mask >>>= 1;
				}
			}
			// System.err.println((sol & FIRST_MASK) >>> 44);
			System.out.println(solutionValue);
			word = sc.next();
		}
	}
}
