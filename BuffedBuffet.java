import java.util.*;
import static java.lang.Math.*;

public class BuffedBuffet
{
    interface Addable
    {
        public int getSize();
        public double getCost(int current);
    }

    static class DiscreteDish implements Addable
    {
        int pieceWeight, initial, decay;
        public DiscreteDish(int pieceWeight, int initial, int decay)
        {
            this.pieceWeight = pieceWeight;
            this.initial = initial;
            this.decay = decay;
        }

        public double getTastiness(int pieces)
        {
            return pieces * initial - decay * pieces * (pieces - 1) / 2;
        }

        public double getCost(int current)
        {
            return initial - current * decay;
        }

        public int getSize()
        {
            return pieceWeight;
        }
    }

    static class ContinuousDish implements Comparable<ContinuousDish>
    {
        double initial, decay;
        public ContinuousDish(double initial, double decay)
        {
            this.initial = initial;
            this.decay = decay;
        }

        public double getTastiness(double weight)
        {
            return weight * initial - weight * weight * decay / 2;
        }

        public int compareTo(ContinuousDish o)
        {
            if (initial < o.initial)
                return -1;
            else if (initial > o.initial)
                return 1;
            else
                return 0;
        }
    }

    static class MixedDish extends ContinuousDish
    {
        double priorTaste, priorWeight;
        public MixedDish(double initial, double decay,
                         double priorTaste, double priorWeight)
        {
            super(initial, decay);
            this.priorTaste = priorTaste;
            this.priorWeight = priorWeight;
        }

        @Override
        public double getTastiness(double weight)
        {
            double newWeight = weight - priorWeight;
            return priorTaste + super.getTastiness(newWeight);
        }

        public String toString()
        {
            return String.format("MixedDish:\n\tinitial: %.2f\n\tdecay:   %.2f\n\ttaste:   %.2f\n\tweight:   %.2f", initial, decay, priorTaste, priorWeight);
        }
    }

    static class MashedContinuous implements Addable
    {
        private TreeMap<Double, MixedDish> mixes;

        public MashedContinuous(List<? extends ContinuousDish> dishes)
        {
            Stack<ContinuousDish> list = new Stack<ContinuousDish>();
            for (ContinuousDish d : dishes)
                list.push(d);
            Collections.sort(list);
            mixes = new TreeMap<Double, MixedDish>();

            double taste = 0;
            double weight = 0;
            double initial;
            double sumdecay = 0;

            ContinuousDish cur;
            MixedDish curmix;
            while (!list.isEmpty())
            {
                cur = list.pop();
                initial = cur.initial;
                sumdecay += 1.0 / cur.decay;
                while (!list.isEmpty() && list.peek().initial == initial)
                    sumdecay += 1.0 / list.pop().decay;

                curmix = new MixedDish(initial, 1 / sumdecay, taste, weight);
                mixes.put(weight, curmix);
                // System.err.println(curmix);
                if (!list.isEmpty())
                {
                    cur = list.peek();
                    weight += (initial - cur.initial) * sumdecay;
                    taste = curmix.getTastiness(weight);
                }
            }
        }

        public double getTastiness(double weight)
        {
            if (weight == 0)
                return 0;
            Map.Entry<Double, MixedDish> entry = mixes.lowerEntry(weight);
            if (entry != null)
            {
                MixedDish mix = entry.getValue();
                return mix.getTastiness(weight);
            }
            else
                return Double.NEGATIVE_INFINITY;
        }

        public int getSize()
        {
            return 1;
        }

        public double getCost(int current)
        {
            return getTastiness(current + 1) - getTastiness(current);
        }
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextInt())
        {

        int d = sc.nextInt();
        int w = sc.nextInt();

        ArrayList<DiscreteDish> dd = new ArrayList<DiscreteDish>(d);
        ArrayList<ContinuousDish> cd = new ArrayList<ContinuousDish>(d);

        for (int i = 0; i < d; i++)
        {
            if (sc.next().equals("D"))
                dd.add(new DiscreteDish(sc.nextInt(), sc.nextInt(), sc.nextInt()));
            else
                cd.add(new ContinuousDish(sc.nextInt(), sc.nextInt()));
        }

        MashedContinuous mc = new MashedContinuous(cd);

        int nd = dd.size();

        Addable[] types = new Addable[nd + 1];
        int[] sizes = new int[nd + 1];
        types[0] = mc;
        sizes[0] = 1;
        int dishnum = 1;
        for (DiscreteDish dish : dd)
        {
            types[dishnum] = dish;
            sizes[dishnum] = dish.getSize();
            dishnum++;
        }

        double[] values = new double[w + 1];
        int[][] numused = new int[w + 1][nd + 1];

        Arrays.fill(numused[0], 0);
        Arrays.fill(values, Double.NEGATIVE_INFINITY);
        values[0] = 0;

        Addable cur;
        int bestind;
        int prev;
        double bestval;
        double curval;

        for (int i = 1; i <= w; i++)
        {
            bestval = Double.NEGATIVE_INFINITY;
            bestind = 0;
            for (int j = 0; j <= nd; j++)
            {
                prev = i - sizes[j];
                if (prev < 0)
                    continue;

                cur = types[j];
                curval = values[prev] + cur.getCost(numused[prev][j]);
                // System.err.printf("i = %d, j = %d\n\tcurval = %.2f + %.2f = %.2f\n", i, j, values[prev], cur.getCost(numused[i][j]), curval);

                if (curval > bestval)
                {
                    bestval = curval;
                    bestind = j;
                }
            }
            values[i] = bestval;
            // System.err.printf("values[%d] = %.2f\n", i, values[i]);
            prev = i - sizes[bestind];
            for (int j = 0; j <= nd; j++)
                numused[i][j] = numused[prev][j];
            numused[i][bestind]++;
            // System.err.printf("numused[%d][%d] = %d\n", i, bestind, numused[i][bestind]);
        }

        double ans = values[w];
        if (ans == Double.NEGATIVE_INFINITY)
            System.out.println("impossible");
        else
            System.out.printf("%.9f\n", ans);

        /*
        for (j = 0; j <= dd.size(); j++)
        {
            for (int i = 0; i <= w; i++)
            {
                System.err.printf("%7.2f ", values[i][j]);
            }
            System.err.println();
        }
        */
        }
    }
}
