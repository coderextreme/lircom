package lircom;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpResponse.*;
import java.net.http.HttpClient.*;
import java.time.Duration;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class BabelFish {
    static public String translate(String message, String from, String to) throws Exception {


	   HttpClient client = HttpClient.newBuilder()
		.version(Version.HTTP_1_1)
		.followRedirects(Redirect.NORMAL)
		.connectTimeout(Duration.ofSeconds(20))
		.build();

	   HttpRequest request = HttpRequest.newBuilder()
		 .uri(URI.create("https://www.babelfish.com/success/?uText="+message.replaceAll(" ","+")+"&slang="+from+"&dlang="+to+"&btnSubmit=Translate&recaptcha_response=03AGdBq27H9gOCW2mtH10Jj7SIvBAHIucn8xxjVSvAvRuPbkfMhdHYxo8cVq-bk1_N-bMLHovpe9Az-6C9P7qQthadDI0DPYL-rOXLzkpY6yfSTtV7R_hw83YxPZ1g21_UGts78V6dWv468mVLosgN96oQgKHraKXFoo_HUK273Altv_9aOl5EC2kemrzBijBf0pZ0DjrWz6KD6i9IQ8YcITE46l6seOU7ofSikARjFipNX0d588v_k-V2iENBrBefZiG7Pm4w-IxDU0NxUmdglwgcSc4IpPGmQouKmAw7DeFHJHGjobOe4SX_giP1KkuigKSLUByvMXekpe453dRiCqIulASOmPRk0KHCHZsfJYm3hEXWdKewjdL8kD5ClJfkX9KNZNrfH_IEa1ZRi7IzJbB88OBTgxRNKz9xZvJ6HmJupZa0tKw51Ret5BqTKHtQBz9to_o1XJqo4Px1K8HD7YnOyZ4c1nV1OA&SubmitKeyHidden=replacethiskey"))
		 .build();

	   HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
	   System.out.println(response.statusCode());
	   // fix up the HTML
	   String buffer = response.body()
		   .replaceAll("async([ />])", "async='true'$1")
		   .replaceAll("defer([ />])", "defer='true'$1")
		   .replaceAll("(<meta[^<>]*)/>", "$1>")
		   .replaceAll("(<meta[^>]*>)", "$1</meta>")
		   .replaceAll("(<link[^<>]*)/>", "$1>")
		   .replaceAll("(<link[^>]*>)", "$1</link>")
		   .replaceAll("(<img[^<>]*)/>", "$1>")
		   .replaceAll("(<img[^>]*>)", "$1</img>")
		   .replaceAll("&(amp;){0}", "&amp;$1")
		   .replaceAll("&amp;amp;", "&amp;")
		   .replaceAll("<hr>", "<hr/>")
		   .replaceAll("</body>", "</div></body>")
		   ;
	   // System.out.println(buffer);
	   System.out.println("message="+message+" from="+from+" to="+to);
	   String out = null;
	   try {
		   DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		   DocumentBuilder builder = builderFactory.newDocumentBuilder();
		   Document xmlDocument = builder.parse(new InputSource(new StringReader(buffer)));
		   XPath xPath = XPathFactory.newInstance().newXPath();
		   String expression = "/html/body/div[2]/div[2]/div[1]/div[1]/div/div/div/text()";
		   out = xPath.compile(expression).evaluate(xmlDocument, XPathConstants.STRING).toString().trim();
	   } catch (Exception e) {	
		   out = "Untranslated "+message;
	   }
	   System.out.println(out);
	return out;
    }
    static public void main(String args[]) throws Exception {
	BabelFish.translate("thank you", "en", "de");
    }
}
