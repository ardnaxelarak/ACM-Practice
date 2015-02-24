import java.util.*;
import java.util.regex.*;
import static java.lang.Math.*;

public class Compression
{
    public static void main(String[] args)
    {
        /* pre-defined keywords */
        final String[] keywords = new String[] {"var", "end", "for", "then", "else", "case", "goto", "const", "label", "while", "begin", "until", "repeat", "downto", "function", "procedure" };

        Scanner sc = new Scanner(System.in);
        String line;

        /* text to compress */
        StringBuilder programText;

        /* object to match program text to regex to find identifiers */
        Matcher m;

        /* index of code to store for next new identifier */
        int k;

        /* current position in scanning text for identifiers */
        int curPos;

        /* table of identifiers -- map String to integer code */
        HashMap<String, Integer> identifiers = new HashMap<String, Integer>();

        /* pattern to match identifiers */
        Pattern idPat = Pattern.compile("[a-zA-Z0-9]+");

        /* read number of test cases */
        int N = sc.nextInt();

        /* read end of first line (that contained N) and the blank line */
        sc.nextLine();
        sc.nextLine();

        for (int i = 0; i < N; i++)
        {
            k = 0;

            /* reset list of identifiers */
            identifiers.clear();
            /* add pre-defined keywords */
            for (String keyword : keywords)
                identifiers.put(keyword, k++);

            /* read in first line of test case
             * this will be a blank line for all cases after the first
             * this will give us the needed blank line to separate
             * outputs between test cases */
            line = sc.nextLine();

            /* initialize our text */
            programText = new StringBuilder();

            /* read in lines until end of test case */
            while (!line.equals("end."))
            {
                /* add current line to text */
                programText.append(line + "\n");

                /* read next line */
                line = sc.nextLine();
            }
            /* add line containing "end." */
            programText.append(line);

            /* note that we do not read the blank like between test cases
             * this will be added to the next test case, and will be
             * printed unmodified, giving the required blank line between
             * test cases */

            /* create Matcher object to match identifiers */
            m = idPat.matcher(programText);
            curPos = 0;

            /* repeat until no more identifiers exist
             * m.group() will contain the matched text */
            while (m.find())
            {
                /* print the non-alphanumeric characters we skipped */
                System.out.print(programText.substring(curPos, m.start()));

                /* check if we've seen the identifier before */
                if (identifiers.containsKey(m.group()))
                {
                    /* we have -- print out the code */
                    System.out.printf("&%d", identifiers.get(m.group()));
                }
                else if (m.group().length() < 3)
                {
                    /* we haven't, and it's too short and should be
                     * printed */
                    System.out.print(m.group());
                }
                else
                {
                    /* we haven't, and it's long enough to compress, so
                     * store its code */
                    identifiers.put(m.group(), k++);

                    /* but this is the first time we've seen it, so we
                     * print it normally */
                    System.out.print(m.group());
                }

                /* update current position */
                curPos = m.end();
            }

            /* print out any non-alphanumeric characters at end
             * should be just a '.' */
            System.out.print(programText.substring(curPos));
            /* end the line */
            System.out.println();
        }
    }
}
