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
		int letters;
		short curPos;
		public State(int letters, short curPos, short weight)
		{
			this.letters = letters;
			this.weight = weight;;
			this.curPos = curPos;
		}

		public int compareTo(State o)
		{
			return weight - o.weight;
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

		public State next(int pos, int mask)
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
			return new State(letters | mask, (short)pos, (short)newCost);
		}
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
			solutionValue = Integer.MAX_VALUE;
			q = new PriorityQueue<State>(1 << n);
			q.add(new State(0, (short)0, (short)0));
			while (!q.isEmpty())
			{
				cur = q.poll();
				// System.err.printf("<<< %s >>>\n", toString(cur, word));
				if (cur.weight > solutionValue)
					break;
				int mask = 1 << 19;
				for (int i = 0; i < n; i++)
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
