import java.util.*;
import java.util.regex.*;
import static java.lang.Math.*;

public class Compression
{
    public static void main(String[] args)
    {
        final String[] keywords = new String[] {"var", "end", "for", "then", "else", "case", "goto", "const", "label", "while", "begin", "until", "repeat", "downto", "function", "procedure" };
        Scanner sc = new Scanner(System.in);
        String line;
        StringBuilder sb;
        Matcher m;
        int k;
        int curPos;
        HashMap<String, Integer> identifiers = new HashMap<String, Integer>();

        Pattern idPat = Pattern.compile("\\w+");
        int N = sc.nextInt();
        sc.nextLine();
        sc.nextLine();

        for (int i = 0; i < N; i++)
        {
            k = 0;
            identifiers.clear();
            for (String keyword : keywords)
                identifiers.put(keyword, k++);

            line = sc.nextLine();
            sb = new StringBuilder();

            while (!line.equals("end."))
            {
                sb.append(line + "\n");

                line = sc.nextLine();
            }
            sb.append(line);

            m = idPat.matcher(sb);
            curPos = 0;

            while (m.find())
            {
                System.out.print(sb.substring(curPos, m.start()));
                if (identifiers.containsKey(m.group()))
                    System.out.printf("&%d", identifiers.get(m.group()));
                else
                {
                    System.out.print(m.group());
                    if (m.group().length() >= 3)
                        identifiers.put(m.group(), k++);
                }
                curPos = m.end();
            }
            System.out.print(sb.substring(curPos));
            System.out.println();
        }
    }
}
