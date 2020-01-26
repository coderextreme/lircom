function displayWindow(winnum, frame) {
	var newwin = window.open("about:blank", "w"+winnum);
	var fileName = winnum+"_"+frame+".svg";
	var html ='<html><head><title>'+fileName+'</title><meta http-equiv="Refresh" content="0; url=http://localhost:8080/web/start.jsp?frame='+frame+'&window='+winnum+'"></meta></head><body><object onkeypress="return sendKeyPress(event,'+winnum+', '+frame+')" data="'+fileName+'" type="image/svg+xml" width="1024" height="768"><embed src="'+fileName+'" type="image/svg+xml" width="1024" height="768"></embed></object></body></html>';
	alert(html);
	newwin.document.write(html);
	newwin.document.close();
}


function sendKeyPress(e, winnum, frame) {
	var keynum
	var keychar

	if(window.event) // IE
	{
		keynum = e.keyCode
	}
	else if(e.which) // Netscape/Firefox/Opera
	{
		keynum = e.which
	}
	keychar = String.fromCharCode(keynum)
	displayWindow(winnum, frame+1);
}
