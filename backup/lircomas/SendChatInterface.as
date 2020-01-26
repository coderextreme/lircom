package lircom {
import java.io.*;
import java.util.*;

public interface SendChatInterface {
	function username(username:String):void ;
	function password(password:String):void ;
	function nickname(nick:String):void ;
	function sendJoin(room:String):void ;
	function sendLeave(room:String):void ;
	function sendQuit():void ;
	function send(to:String, message:String):void ;
	function send(to:String, file:InputStream):void ;
	function sendAction(to:String, message:String):void ;
	function sendPing():void ;
	// people on system
	function requestPeople():void ;
	// rooms on system
	function requestRooms():void ;
	// people in room
	function requestPeople(room:String):void ;
	// rooms a person is in
	function requestRooms(person:String):void ;
}
}