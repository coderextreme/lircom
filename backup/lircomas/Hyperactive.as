package lircom {
/*
    ICBM (Internet Chat by MUD)

    Copyright (C) 1997, 1998, 2000  John Carlson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
import javax.swing.event.*;

public class Hyperactive implements HyperlinkListener {
        var browser:String= "firefox";
        public function setBrowser(exe:String):void {
            browser = exe;
        }
        public function Hyperactive() {
            var os:String= System.getProperty("os.name");
            System.err.println("OS is "+os);
            if (os.startsWith("Windows")) {
                browser = "C:/Program Files/Internet Explorer/iexplore.exe";
            } else if (os.startsWith("Mac")) {
                browser = "/Applications/Safari.app/Contents/MacOS/Safari";
            } else {
                // default firefox
            }
        }
	public function hyperlinkUpdate(e:HyperlinkEvent):void {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			var url:String= e.getURL().toString();
                        try {
                              Runtime.getRuntime().exec(new String[] { browser, url});
                        } catch (var ex:Exception) {
                                ex.printStackTrace();
                        }
		}
	}
}
}