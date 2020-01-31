<html>
<head>
<title>LirCom Chat</title>
<%! static String [] args = null; %>
<%
	String framestr = request.getParameter("frame");
	String window = request.getParameter("window");
	int frame = 0;
	if (framestr != null) {
		frame = Integer.parseInt(framestr);
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
		String fileName = winnum+"_"+frame+".svg";
		java.io.File file = new java.io.File("/Users/johncarlson/Applications/apache-tomcat-6.0.14/webapps/web/"+fileName);
		while (!file.exists()); // wait for the file to show up
		System.err.println("file exists");
%>
<script type="text/javascript">
		displayWindow(<%=winnum%>, <%=frame%>+1);
</script>
<%
	}
%>
</head>
<body>
</body>
</html>
