import java.util.*;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Integer.bitCount;
public class YouWin
{
	static class StateList
	{
		class Node
		{
			State state;
			Node prev, next;

			public Node(State s)
			{
				state = s;
				next = null;
				prev = null;
			}

			public Node addAfter(State s)
			{
				Node n = new Node(s);
				n.prev = this;
				if (next == null)
					last = n;
				else
					next.prev = n;

				n.next = next;
				next = n;
				size++;
				return n;
			}

			public Node addBefore(State s)
			{
				Node n = new Node(s);
				n.next = this;
				if (prev == null)
					first = n;
				else
					prev.next = n;

				n.prev = prev;
				prev = n;
				size++;
				return n;
			}

			public void remove()
			{
				if (prev == null && next == null)
				{
					last = null;
					first = null;
				}
				else if (next == null)
				{
					prev.next = null;
					last = prev;
				}
				else if (prev == null)
				{
					next.prev = null;
					first = next;
				}
				else
				{
					next.prev = prev;
					prev.next = next;
				}
				size--;
			}
		}

		Node first, last;
		int size;
		public StateList()
		{
			first = null;
			size = 0;
		}

		public boolean isEmpty()
		{
			return size == 0;
		}

		public void addState(State state)
		{
			if (size == 0)
			{
				Node n = new Node(state);
				first = n;
				last = n;
				size = 1;
			}
			else
			{
				Node newnode = null;
				Node cur = last;
				while (true)
				{
					if (state.compareTo(cur.state) > 0)
					{
						newnode = cur.addAfter(state);
						// System.err.printf(" -- added");
						break;
					}
					else if (state.samePos(cur.state))
					{
						cur.remove();
						// System.err.printf("Removing %s -- same as %s\n", cur.state, state);
					}
					cur = cur.prev;
					if (cur == null)
						break;
				}
				if (cur == null)
				{
					// System.err.printf(" -- added");
					first.addBefore(state);
				}
				else
				{
					cur = newnode;
					while (cur.prev != null)
					{
						cur = cur.prev;
						if (cur.state.samePos(state))
						{
							newnode.remove();
							// System.err.printf("Removing %s -- same as %s\n", newnode.state, cur.state);
						}
					}
				}
			}
		}

		public State pop()
		{
			State s = first.state;
			first.remove();
			return s;
		}
	}

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
			this.weight = weight;
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

		public boolean samePos(State o)
		{
			return (letters == o.letters) && (curPos == o.curPos);
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

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		String word = sc.next();
		StateList q;
		int n;
		State cur;
		int solutionValue;
		State sol;
		while (!word.equals("0"))
		{
			n = word.length();
			State.setWord(word);
			solutionValue = Integer.MAX_VALUE;
			q = new StateList();
			q.addState(new State());
			while (!q.isEmpty())
			{
				cur = q.pop();
				// System.err.println(q.size);
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
							// System.err.printf("    %s", newitem);
							if (newitem.isFinal())
							{
								solutionValue = newitem.weight;
								// sol = newitem;
								// System.err.printf("Found solution %d\n", solutionValue);
							}
							else
							{
								q.addState(newitem);
								// System.err.printf(" -- q.size = %d", q.size);
							}
							// System.err.println();
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
