import java.io.*;
import java.util.*;

public class Main {

	BufferedReader br;
	PrintWriter out;
	StringTokenizer st;
	boolean eof;
	
	Random rnd = new Random(228239);
	
	class Node {
		int value;
		int prior;
		boolean rev;
		int size;
		
		Node l, r;
		
		public Node(int value) {
			this.value = value;
			prior = rnd.nextInt();
			rev = false;
			size = 1;
			l = r = null;
		}
	}
	
	Node merge(Node left, Node right) {
		if (left == null)
			return right;
		if (right == null)
			return left;
		
		Node res = left.prior < right.prior ? left : right;
		pushDown(res);
		
		if (left.prior < right.prior)
			left.r = merge(left.r, right);
		else
			right.l = merge(left, right.l);
		
		res.size = getSize(res.l) + getSize(res.r)+ 1;
		return res;
	}
	
	Node[] split(Node v, int leftSize) {
		if (v == null)
			return new Node[2];
		Node[] res;
		pushDown(v);
		int curLeft = getSize(v.l) + 1;
		if (curLeft <= leftSize) {
			res = split(v.r, leftSize - curLeft);
			v.r = res[0];
			res[0] = v;
		} else {
			res = split(v.l, leftSize);
			v.l = res[1];
			res[1] = v;
		}
		v.size = getSize(v.l) + getSize(v.r) + 1;
		
		return res;
	}
	
	void modifyRev(Node v) {
		if (v != null)
			v.rev = !v.rev;
	}
	
	void pushDown(Node v) {
		if (v.rev) {
			modifyRev(v.l);
			modifyRev(v.r);
			Node tmp = v.l;
			v.l = v.r;
			v.r = tmp;
			v.rev = false;
		}
	}
	
	int getSize(Node v) {
		return v == null ? 0 : v.size;
	}
	
	void shuffle(Node v, int a, int b, int c) {
		Node[] t = split(v, a);
		Node[] t2 = split(t[1], b);
		Node curDeck = merge(t[0], t2[1]);
		t = split(curDeck, c);
		modifyRev(t2[0]);
		v = merge(t[0], merge(t2[0], t[1]));
	}
	
	void print(Node v) {
		if (v == null)
			return;
		pushDown(v);
		print(v.l);
		out.print(v.value + " ");
		print(v.r);
	}

	void solve() throws IOException {
		int n = nextInt();
		Node root = null;
		
		for (int i = 1; i <= n; i++) {
			root = merge(root, new Node(i));
		}
		
		int q = nextInt();
		while (q-- > 0)
			shuffle(root, nextInt(), nextInt(), nextInt());
		
		print(root);
		out.println();
	}

	void inp() throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
		solve();
		out.close();
	}

	public static void main(String[] args) throws IOException {
		new Main().inp();
	}

	String nextToken() {
		while (st == null || !st.hasMoreTokens()) {
			try {
				st = new StringTokenizer(br.readLine());
			} catch (Exception e) {
				eof = true;
				return null;
			}
		}
		return st.nextToken();
	}

	String nextString() {
		try {
			return br.readLine();
		} catch (IOException e) {
			eof = true;
			return null;
		}
	}

	int nextInt() throws IOException {
		return Integer.parseInt(nextToken());
	}

	long nextLong() throws IOException {
		return Long.parseLong(nextToken());
	}

	double nextDouble() throws IOException {
		return Double.parseDouble(nextToken());
	}
}
