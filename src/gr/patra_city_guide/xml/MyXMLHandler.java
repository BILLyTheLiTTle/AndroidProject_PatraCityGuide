package gr.patra_city_guide.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class MyXMLHandler extends DefaultHandler{
	
	private ArrayList<String> names,streets,tels,mobs,faxes,sites,emails,maps;
	private boolean currentElement=false;
	private String currentValue=null;

	public MyXMLHandler(InputStream xmlStream){
		try{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();//SaxException
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(this);
			if(xmlStream.equals(null)){
				//show an error message
			}
			else{
				xr.parse(new InputSource(xmlStream));
			}
		}
		catch(SAXException se){
			//handle later
		}
		catch(ParserConfigurationException pce){
			//handler later
		} 
		catch (IOException ioe) {
			//handler later
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
			}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		currentElement = false;
		if(localName.equals("name")){
			names.add(currentValue);
		}
		else if(localName.equals("street")){
			streets.add(currentValue);
		}
		else if(localName.equals("tel")){
			tels.add(currentValue);
		}
		else if(localName.equals("mob")){
			mobs.add(currentValue);
		}
		else if(localName.equals("fax")){
			faxes.add(currentValue);
		}
		else if(localName.equals("site")){
			sites.add(currentValue);
		}
		else if(localName.equals("email")){
			emails.add(currentValue);
		}
		else if(localName.equals("map")){
			maps.add(currentValue);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		currentElement = true;
		if (localName.equals("shops")){
			names=new ArrayList<String>();
			streets=new ArrayList<String>();
			tels=new ArrayList<String>();
			mobs=new ArrayList<String>();
			faxes=new ArrayList<String>();
			sites=new ArrayList<String>();
			emails=new ArrayList<String>();
			maps=new ArrayList<String>();
		}
	}
	
	public String[] getNames(){
		String[] arr=new String[names.size()];
		names.toArray(arr);
		return arr;
	}
	
	public String[] getStreets(){
		String[] arr=new String[streets.size()];
		streets.toArray(arr);
		return arr;
	}
	
	public String[] getTels(){
		String[] arr=new String[tels.size()];
		tels.toArray(arr);
		return arr;
	}
	
	public String[] getMobs(){
		String[] arr=new String[mobs.size()];
		mobs.toArray(arr);
		return arr;
	}
	
	public String[] getFaxes(){
		String[] arr=new String[faxes.size()];
		faxes.toArray(arr);
		return arr;
	}
	
	public String[] getSites(){
		String[] arr=new String[sites.size()];
		sites.toArray(arr);
		return arr;
	}
	
	public String[] getEmails(){
		String[] arr=new String[emails.size()];
		emails.toArray(arr);
		return arr;
	}
	
	public String[] getMaps(){
		String[] arr=new String[maps.size()];
		maps.toArray(arr);
		return arr;
	}
}
