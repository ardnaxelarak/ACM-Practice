import java.util.*;

public class FlexibleSpaces
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		int P, W;
		boolean[] widths;
		int[] parts;
		W = sc.nextInt();
		P = sc.nextInt();
		parts = new int[P + 2];
		widths = new boolean[W + 1];
		parts[0] = 0;
		parts[P + 1] = W;
		for (int i = 0; i < P; i++)
			parts[i + 1] = sc.nextInt();
		for (int i = 0; i < P + 1; i++)
			for (int j = i + 1; j < P + 2; j++)
				widths[parts[j] - parts[i]] = true;
		for (int i = 1; i < W; i++)
			if (widths[i])
				System.out.printf("%d ", i);
		System.out.println(W);
	}
}
