import java.util.*;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Integer.bitCount;
public class YouWin
{
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

		public State(int letters, byte curPos, short weight)
		{
			this.letters = letters;
			this.weight = weight;;
			this.curPos = curPos;
			if (letters == finmask)
				this.est = 0;
			else
			{
				int est = len - bitCount(letters);
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
				this.est = (short)est;
			}
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
			return String.format("%4d %s", weight, res);
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

	static int naiveTry(String word)
	{
		char[] lets = word.toCharArray();
		char curLet = 'A';
		int moves = word.length();
		for (char c : lets)
		{
			int dist = c - curLet;
			if (dist < 0)
				dist += 26;
			if (dist > 13)
				dist = 26 - dist;
			moves += dist;
			curLet = c;
		}
		return moves;
	}

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		String word = sc.next();
		PriorityQueue<State> q;
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
			q.add(new State());
			while (!q.isEmpty())
			{
				cur = q.poll();
				// System.err.printf("<<< %s >>>\n", toString(cur, word));
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
