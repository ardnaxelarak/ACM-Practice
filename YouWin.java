import java.util.*;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.max;
import static java.lang.Integer.bitCount;
public class YouWin
{
	static final int LEVELS = 5;
	static class StateGenerator
	{
		private String word;
		private int len, finmask;
		private byte[] letDisps;
		public class State implements Comparable<State>
		{
			short weight;
			short est;
			int letters;
			byte curPos;

			public State()
			{
				letters = 0;
				curPos = 0;
				weight = 0;
				est = calcEst();
			}

			private short calcEst()
			{
				if (letters == finmask)
					return (short)0;
				int remlets = len - bitCount(letters);
				int est = remlets;
				int mask;
				if (letters != 0)
				{
					int check = letters ^ finmask;
					int firstzero = Integer.numberOfLeadingZeros(check) - 12;
					int lastzero = 19 - Integer.numberOfTrailingZeros(check);
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
				}

				char[] lets = word.toCharArray();
				byte dist, mindist;
				char curLet, newLet;
				if (letters == 0)
					curLet = 'A';
				else
					curLet = lets[curPos];
				mask = 1 << 19;

				letDisps[0] = 0;
				letDisps[1] = 26;
				int k = 2;
				for (int i = 0; i < len; i++)
				{
					if ((letters & mask) == 0)
					{
						newLet = lets[i];
						dist = (byte)(newLet - curLet);
						if (dist < 0)
							dist += 26;
						letDisps[k++] = dist;
					}
					mask >>>= 1;
				}
				Arrays.sort(letDisps, 0, k);
				mindist = (byte)25;
				for (int i = 1; i < k - 1; i++)
				{
					int d1 = letDisps[i];
					if (d1 > 13)
						dist = (byte)((26 - d1) * 2 + letDisps[i - 1]);
					else
						dist = (byte)(d1 * 2 + 26 - letDisps[i + 1]);
					if (dist < mindist)
						mindist = dist;
				}
				mindist = (byte)min(mindist, min(26 - letDisps[1], letDisps[k - 1]));
				est += mindist;

				return (short)est;
			}

			public State(int letters, byte curPos, short weight)
			{
				this.letters = letters;
				this.weight = weight;
				this.curPos = curPos;
				this.est = calcEst();
			}

			public int compareTo(State o)
			{
				return (weight + est) - (o.weight + o.est);
			}

			public boolean equivalent(State o)
			{
				return (letters == o.letters) && (curPos == o.curPos);
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

		public StateGenerator(String word)
		{
			this.word = word;
			len = word.length();
			finmask = ((1 << len) - 1) << (20 - len);
			letDisps = new byte[len + 2];
		}

		PriorityQueue<State> getStates(int letters, int numkeep)
		{
			State cur, next;
			PriorityQueue<State> nextQueue;
			if (numkeep < 0)
			{
				numkeep = Integer.MAX_VALUE;
				nextQueue = new PriorityQueue<State>(1 << len);
			}
			else
			{
				nextQueue = new PriorityQueue<State>(numkeep * len);
			}
			LinkedList<State> curQueue = new LinkedList<State>();
			nextQueue.add(new State());
			int mask;
			boolean contained;
			for (byte i = 0; i < letters; i++)
			{
				while (curQueue.size() < numkeep && !nextQueue.isEmpty())
				{
					contained = false;
					next = nextQueue.poll();
					for (State curst : curQueue)
					{
						if (curst.equivalent(next))
						{
							contained = true;
							break;
						}
					}
					if (!contained)
						curQueue.add(next);
				}
				nextQueue.clear();

				while (!curQueue.isEmpty())
				{
					cur = curQueue.poll();
					mask = 1 << 19;
					for (byte j = 0; j < len; j++)
					{
						if ((cur.letters & mask) == 0)
						{
							next = cur.next(j, mask);
							nextQueue.add(next);
						}
						mask >>>= 1;
					}
				}
			}
			return nextQueue;
		}

		int greedyTry(int numkeep)
		{
			PriorityQueue<State> q = getStates(len, numkeep);
			return q.poll().weight;
		}

		public int orderCost(byte[] order)
		{
			State cur = new State();
			for (byte pos : order)
				cur = cur.next(pos);
			return cur.weight;
		}

		public int quickTries()
		{
			byte[] order = new byte[len];
			for (byte i = 0; i < len; i++)
				order[i] = i;
			int moves1 = orderCost(order);
			System.err.printf("   In Order: %3d\n", moves1);

			Byte[] pOrder = new Byte[len];
			for (byte i = 0; i < len; i++)
				pOrder[i] = i;
			Arrays.sort(pOrder, new AlphSort(word));
			for (byte i = 0; i < len; i++)
				order[i] = pOrder[i].byteValue();
			int moves2 = orderCost(order);
			System.err.printf(" Alphabetic: %3d\n", moves2);
			int moves3;
			moves3 = greedyTry(1000);
			System.err.printf(" Big Greedy: %3d\n", moves3);
			int moves = min(min(moves1, moves2), moves3);
			return moves;
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

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		String word = sc.next();
		PriorityQueue<StateGenerator.State> q, q2;
		StateGenerator sg;
		StateGenerator.State newitem, cur;
		int n;
		int mask;
		int solutionValue;
		while (!word.equals("0"))
		{
			n = word.length();
			sg = new StateGenerator(word);
			solutionValue = sg.quickTries();
			q = new PriorityQueue<StateGenerator.State>(1 << n);
			q = sg.getStates(min(LEVELS, n), -1);
			System.err.println("States received");
			// q.add(sg.new State());
			while (!q.isEmpty())
			{
				cur = q.poll();
				// System.err.printf("<<< %s >>>\n", cur);
				if (cur.weight > solutionValue)
					break;
				mask = 1 << 19;
				for (byte i = 0; i < n; i++)
				{
					if ((cur.letters & mask) == 0)
					{
						newitem = cur.next(i, mask);
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
							// System.err.printf("    %s\n", newitem);
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
