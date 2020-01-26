<html>
<head>
<title>LirCom Chat</title>
<%! static String [] args = null; %>
<%
	String framestr = request.getParameter("frame");
	int frame = 0;
	if (framestr != null) {
		frame = Integer.parseInt(framestr);
	}
	String windowstr = request.getParameter("window");
	int window = -1;
	if (windowstr != null) {
		window = Integer.parseInt(windowstr);
	}
	String keynum = request.getParameter("keynum");
	String xstr = request.getParameter("x");
	int x;
	if (xstr != null) {
		x = Integer.parseInt(xstr);
	}
	String ystr = request.getParameter("y");
	int y;
	if (ystr != null) {
		y = Integer.parseInt(ystr);
	}
%>
<script type="text/javascript" src="swing.js">
<%
	if (args == null) {
		lircom.MainWindow.main(args = new String[] {"lircom.Chat", "yottzumm"});
	}
	while (lircom.SVGFE.getWindows().size() == 0);
	System.err.println("got here");
	for (int i = 0;  i < lircom.SVGFE.getWindows().size(); i++) {
		java.awt.Window w = lircom.SVGFE.getWindows().get(i);
		int winnum = w.hashCode();
		if (window == winnum && keynum != null) {
			System.err.println(keynum);
		}
		String fileName = winnum+"_"+frame+".svg";
		java.io.File file = new java.io.File("/Users/johncarlson/Applications/apache-tomcat-6.0.14/webapps/web/"+fileName);
		while (!file.exists()); // wait for the file to show up
		System.err.println("file exists");
%>
<!--
<script type="text/javascript">
		displayWindow(<%=winnum%>, <%=frame+1%>);
</script>
-->
<%
	}
%>
<body>
<%
	for (int i = 0;  i < lircom.SVGFE.getWindows().size(); i++) {
		java.awt.Window w = lircom.SVGFE.getWindows().get(i);
		int winnum = w.hashCode();
		String fileName = winnum+"_"+frame+".svg";
%>
	<object onclick="displayWindow(<%=winnum%>, <%=frame+1%>);" onkeypress="return sendKeyPress(event,<%=winnum%>, <%=frame%>)" data="<%=fileName%>" type="image/svg+xml" width="1024" height="768"><embed src="<%=fileName%>" type="image/svg+xml" width="1024" height="768"></embed></object>
<%
	}
%>
</body>
</html>
