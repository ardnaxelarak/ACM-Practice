import java.util.*;
import static java.lang.Math.*;

public class CoconutsRevisited
{
    static class PrimeList implements Iterable<Integer>
    {
        private LinkedList<Integer> primes;
        private int max;

        public PrimeList(int max)
        {
            if (max < 3)
                max = 3;
            if (max & 1 == 0)
                max |= 1;
            this.max = max;
            LinkedList<Integer> test = new LinkedList<Integer>();
            primes = new LinkedList<Integer>();
            ListIterator<Integer> it;
            int curprime;

            primes.add(2);
            for (int i = 3; i <= max; i += 2)
                test.add(i);

            while (!test.isEmpty() && test.getFirst() <= sqrt(max))
            {
                curprime = test.removeFirst();
                primes.add(curprime);
                it = test.listIterator(0);
                while (it.hasNext())
                {
                    if ((it.next() % curprime) == 0)
                        it.remove();
                }
            }

            for (int prime : test)
                primes.add(prime);
        }

        public Iterator<Integer> iterator()
        {
            return primes.iterator();
        }
    }

    static class PrimeFactorization
    {
        private int[] factors;
        private int[] counts;

        public PrimeFactorization(int value, PrimeList primes)
        {
            List<Integer> factorlist, countlist;

            factorlist = new LinkedList<Integer>();
            countlist = new LinkedList<Integer>();

            int current = value;

            for (int prime : primes)
            {
                int count = 0;
                while (current % prime == 0)
                {
                    count++;
                    current /= prime;
                }

                if (count > 0)
                {
                    factorlist.add(prime);
                    countlist.add(count);

                    if (current == 1)
                        break;
                }
            }

            if (current > 1)
            {
                factorlist.add(current);
                countlist.add(1);
            }

            factors = new int[factorlist.size()];
            counts = new int[countlist.size()];

            int k = 0;
            for (int factor : factorlist)
                factors[k++] = factor;

            k = 0;
            for (int count : countlist)
                counts[k++] = count;
        }

        private int getValue(int[] current)
        {
            int res = 1;
            int len = current.length;
            int num, prime;

            for (int i = 0; i < len; i++)
            {
                num = current[i];
                prime = factors[i];
                for (int j = 0; j < num; j++)
                    res *= prime;
            }

            return res;
        }

        private int nextFactor(int[] current)
        {
            int len = current.length;

            for (int i = 0; i < len; i++)
            {
                if (current[i] < counts[i])
                {
                    current[i]++;
                    return getValue(current);
                }
                else
                {
                    current[i] = 0;
                }
            }

            return -1;
        }

        public int[] getFactors()
        {
            int numFactors = 1;
            for (int count : counts)
                numFactors *= count + 1;

            int[] ret = new int[numFactors];
            int[] current = new int[factors.length];

            ret[0] = 1;
            for (int k = 1; k < numFactors; k++)
                ret[k] = nextFactor(current);

            Arrays.sort(ret);

            return ret;
        }

        public String toString()
        {
            StringBuilder sb = new StringBuilder();

            int len = factors.length;
            for (int i = 0; i < len; i++)
            {
                if (i > 0)
                    sb.append(" + ");

                sb.append(factors[i]);
                sb.append('^');
                sb.append(counts[i]);
            }

            return sb.toString();
        }
    }

    static boolean checkValue(int start, int num)
    {
        int cur = start;
        for (int i = 0; i < num; i++)
        {
            cur--;
            if ((cur % num) != 0)
            {
                return false;
            }
            cur -= cur / num;
        }

        return (cur % num) == 0;
    }

    static int getValue(int start, PrimeFactorization pf)
    {
        int[] list = pf.getFactors();

        for (int i = list.length - 2; i > 0; i--)
            if (checkValue(start, list[i]))
                return list[i];

        return -1;
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        int value, ans;

        PrimeList pl = new PrimeList(1000000);

        value = sc.nextInt();

        while (value >= 0)
        {
            PrimeFactorization pf = new PrimeFactorization(value - 1, pl);

            ans = getValue(value, pf);

            if (ans > 0)
            {
                System.out.printf("%d coconuts, %d persons and 1 monkey\n", value, ans);
            }
            else
            {
                System.out.printf("%d coconuts, no solution\n", value);
            }

            value = sc.nextInt();
        }
    }
}
