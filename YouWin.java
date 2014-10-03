import java.util.*;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.max;
import static java.lang.Integer.bitCount;
public class YouWin
{
	static final int PRUNE_WIDTH = 3;
	static class State implements Comparable<State>
	{
		static String word;
		static int len;
		static int finmask;
		short weight;
		short est;
		int letters;
		byte curPos;

		public State()
		{
			letters = 0;
			curPos = 0;
			weight = 0;
			est = (short)len;
		}

		private short calcEst()
		{
			if (letters == finmask)
				return (short)0;
			int remlets = len - bitCount(letters);
			int est = remlets;
			int check = letters ^ finmask;
			int firstzero = Integer.numberOfLeadingZeros(check) - 12;
			int lastzero = 19 - Integer.numberOfTrailingZeros(check);
			int mask;
			if (curPos < firstzero)
			{
				mask = (1 << (firstzero - curPos) - 1) << firstzero;
				est += bitCount(letters & mask);
			}
			else if (curPos > firstzero)
			{
				mask = (1 << (curPos - firstzero) - 1) << curPos;
				est += bitCount(letters & mask);
			}
			mask = (1 << (lastzero - firstzero) - 1) << lastzero;
			est += bitCount(letters & mask);

			int dist;
			char curLet, newLet;
			char[] lets = word.toCharArray();
			if (letters == 0)
				curLet = 'A';
			else
				curLet = lets[curPos];
			int[] letDisps = new int[remlets + 2];
			letDisps[0] = 0;
			letDisps[remlets + 1] = 26;
			mask = 1 << 19;
			int k = 1;
			for (int i = 0; i < len; i++)
			{
				if ((letters & mask) == 0)
				{
					newLet = lets[i];
					dist = newLet - curLet;
					if (dist < 0)
						dist += 26;
					letDisps[k++] = dist;
				}
				mask >>>= 1;
			}
			Arrays.sort(letDisps);
			int mindist = 25;
			for (int i = 1; i < remlets + 1; i++)
			{
				int d1 = letDisps[i];
				if (d1 > 13)
					dist = (26 - d1) * 2 + letDisps[i - 1];
				else
					dist = d1 * 2 + 26 - letDisps[i + 1];
				if (dist < mindist)
					mindist = dist;
			}
			mindist = min(mindist, min(letDisps[0], 26 - letDisps[remlets + 1]));
			est += mindist;

			return (short)est;
		}

		public State(int letters, byte curPos, short weight)
		{
			this.letters = letters;
			this.weight = weight;;
			this.curPos = curPos;
			this.est = calcEst();
		}

		public int compareTo(State o)
		{
			return (weight + est) - (o.weight + o.est);
		}

		public int getMin()
		{
			return weight + est;
		}
		
		public String toString()
		{
			int mask = 1 << 19;
			String res = "";
			for (int i = 0; i < len; i++)
			{
				if ((letters & mask) != 0)
					res += word.charAt(i);
				if (curPos == i)
					res += "|";
				mask >>>= 1;
			}
			return String.format("%4d (+%2d) %s", weight, est, res);
		}

		public boolean isFinal()
		{
			return letters == finmask;
		}

		public static void setWord(String word)
		{
			State.word = word;
			len = word.length();
			finmask = ((1 << len) - 1) << (20 - len);
		}

		public State next(byte pos)
		{
			return next(pos, 1 << (19 - pos));
		}

		public State next(byte pos, int mask)
		{
			char newLet = word.charAt(pos);
			char curLet;
			if (letters == 0)
				curLet = 'A';
			else
				curLet = word.charAt(curPos);
			int dist = newLet - curLet;
			if (dist < 0)
				dist += 26;
			if (dist > 13)
				dist = 26 - dist;
			int moveMask = ((1 << abs(curPos - pos)) - 1) << (19 - max(pos, curPos));
			int move = bitCount(letters & moveMask);
			// System.err.printf("%2d %d\n", move, dist);
			int newCost = weight + dist + move + 1;
			return new State(letters | mask, pos, (short)newCost);
		}
	}

	static class AlphSort implements Comparator<Byte>
	{
		private char[] word;
		public AlphSort(String word)
		{
			this.word = word.toCharArray();
		}

		public int compare(Byte b1, Byte b2)
		{
			return word[b1.byteValue()] - word[b2.byteValue()];
		}
	}

	static int naiveTry(String word)
	{
		int len = word.length();
		byte[] order = new byte[len];
		for (byte i = 0; i < len; i++)
			order[i] = i;
		int moves1 = orderCost(order);
		// System.err.printf("  In Order: %3d\n", moves1);

		Byte[] pOrder = new Byte[len];
		for (byte i = 0; i < len; i++)
			pOrder[i] = i;
		Arrays.sort(pOrder, new AlphSort(word));
		for (byte i = 0; i < len; i++)
			order[i] = pOrder[i].byteValue();
		int moves2 = orderCost(order);
		// System.err.printf("Alphabetic: %3d\n", moves2);
		int moves = min(moves1, moves2);
		return moves;
	}

	static int orderCost(byte[] order)
	{
		State cur = new State();
		for (byte pos : order)
			cur = cur.next(pos);
		return cur.weight;
	}

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		String word = sc.next();
		PriorityQueue<State> q, q2;
		int n;
		State cur;
		int solutionValue;
		State sol;
		while (!word.equals("0"))
		{
			n = word.length();
			State.setWord(word);
			solutionValue = naiveTry(word);
			q = new PriorityQueue<State>(1 << n);
			q2 = new PriorityQueue<State>(n);
			q.add(new State());
			while (!q.isEmpty())
			{
				cur = q.poll();
				// System.err.printf("<<< %s >>>\n", cur);
				if (cur.weight > solutionValue)
					break;
				int mask = 1 << 19;
				for (byte i = 0; i < n; i++)
				{
					if ((cur.letters & mask) == 0)
					{
						State newitem = cur.next(i, mask);
						if (newitem.weight < solutionValue)
						{
							if (newitem.isFinal())
							{
								solutionValue = newitem.weight;
								// sol = newitem;
								// System.err.printf("Found solution %d\n", solutionValue);
							}
							else
							{
								if (newitem.getMin() < solutionValue)
									q2.add(newitem);
							}
							// System.err.printf("    %s\n", newitem);
						}
					}
					
					mask >>>= 1;
				}

				for (int i = 0; i < PRUNE_WIDTH && !q2.isEmpty(); i++)
					q.add(q2.poll());
				q2.clear();
			}
			// System.err.println((sol & FIRST_MASK) >>> 44);
			System.out.println(solutionValue);
			word = sc.next();
		}
	}
}
