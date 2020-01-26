package lircom {
import java.io.*;

public interface ReceiveChatInterface {
	function receive(from:String, message:String, color:String):void ;
	function receive(from:String, message:String):void ;
	function processLine(message:String):Boolean throws Exception;
	function receive(from:String, file:InputStream):void ;
	function receiveAction(from:String, message:String):void ;
	function receiveJoin(network:String, room:String, person:String):void ;
	function receivePresence(network:String, room:String, person:String):void ;
	function receiveLeave(room:String, person:String):void ;
	function receiveQuit(party:String):void ;
	function setSendCommandInterface(send:SendCommandInterface):void throws Exception ;
	function receiveRoom(network:String, room:String, numUsers:int, topic:String):void ;
	function receiveNick(network:String, oldnick:String, newnick:String):void ;
        public function getLanguage():String ;
}
}