import java.util.*;
public class Targets
{
	static interface Target
	{
		public boolean isHit(int x, int y);
	}
	static class Rectangle implements Target
	{
		int x1, y1, x2, y2;
		public Rectangle(int x1, int y1, int x2, int y2)
		{
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
		public boolean isHit(int x, int y)
		{
			return x >= x1 && y >= y1 && x <= x2 && y <= y2;
		}
	}
	static class Circle implements Target
	{
		int x1, y1, r2;
		public Circle(int x1, int y1, int r)
		{
			this.x1 = x1;
			this.y1 = y1;
			this.r2 = r * r;
		}
		public boolean isHit(int x, int y)
		{
			int xd = x - x1, yd = y - y1;
			return xd * xd + yd * yd <= r2;
		}
	}

	public static void main(String[] args)
	{
		Target[] t;
		int m, n;
		Scanner sc = new Scanner(System.in);
		m = sc.nextInt();
		t = new Target[m];
		for (int i = 0; i < m; i++)
		{
			if (sc.next().equals("circle"))
				t[i] = new Circle(sc.nextInt(), sc.nextInt(), sc.nextInt());
			else
				t[i] = new Rectangle(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
		}
		n = sc.nextInt();
		int x, y;
		int count;
		for (int i = 0; i < n; i++)
		{
			x = sc.nextInt();
			y = sc.nextInt();
			count = 0;
			for (int j = 0; j < m; j++)
			{
				if (t[j].isHit(x, y))
					count++;
			}
			System.out.println(count);
		}
	}
}
