package lircom {
public interface SendCommandInterface {
	function msgSend(line:String, recipient:String):void ;
	function msgSend(line:String):void ;
	function getUser():String ;
}
}