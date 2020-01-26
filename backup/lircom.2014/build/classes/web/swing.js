function displayWindow(winnum, frame, e) {
	var keynum
	//var keychar

	if(window.event) // IE
	{
		keynum = e.keyCode
	}
	else if(e.which) // Netscape/Firefox/Opera
	{
		keynum = e.which
	}
	//keychar = String.fromCharCode(keynum)
	var newwin = window.open("http://localhost:8180/web/start.jsp?frame="+frame+"&window="+winnum+"&keynum="+keynum+"&x="+e.x+"&y="+e.y, "w"+winnum, "height=768,width=1024", true);
	return false;
}

function sendKeyPress(e, winnum, frame) {
	displayWindow(winnum, frame+1, e);
}
