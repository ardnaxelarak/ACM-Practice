import java.util.*;

public class BooleanSatisfiability
{
    public static void main(String[] args)
    {
        LinkedList<LinkedList<Integer>> expression = new LinkedList<LinkedList<Integer>>();
        LinkedList<Integer> clause;

        Scanner sc = new Scanner(System.in);
        HashMap<String, Integer> table = new HashMap<String, Integer>();
        int n = sc.nextInt();
        sc.nextLine();
        int mult, cur;
        String line;
        for (int i = 0; i < n; i++)
        {
            table.clear();
            expression.clear();

            line = sc.nextLine();
            mult = 1;
            cur = 1;
            clause = new LinkedList<Integer>();

            for (String piece : line.split(" "))
            {
                if (piece.equals("NOT"))
                    mult = -1;
                else if (piece.equals("AND"))
                {
                    expression.add(clause);
                    clause = new LinkedList<Integer>();
                    mult = 1;
                }
                else if (piece.equals("OR"))
                {
                }
                else
                {
                    if (table.containsKey(piece))
                    {
                        clause.add(table.get(piece) * mult);
                        mult = 1;
                    }
                    else
                    {
                        clause.add(cur * mult);
                        table.put(piece, cur++);
                        mult = 1;
                    }
                }
            }
            expression.add(clause);

            for (LinkedList<Integer> list : expression)
            {
                for (int k : list)
                    System.out.printf("%d ", k);
                System.out.println();
            }
            System.out.println();
        }
    }
}
