public class Test {
	public static void main(String args[]) throws Exception {
		String adurl = "http://schizophrenics.net/yottzumm/lircom/advertise.pl?port=5891&nick=yottzumm4";
		System.err.println("url is "+adurl);
		java.net.URL u = new java.net.URL(adurl);
		java.net.URLConnection conn = u.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.connect();
		// conn.getOutputStream().close();
		java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}
}
