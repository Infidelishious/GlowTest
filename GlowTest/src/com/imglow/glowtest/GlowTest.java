package com.imglow.glowtest;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class GlowTest extends JFrame{
	private static final long serialVersionUID = -2431572766861289179L;
	
	public GlowTest thiss;
	
	public GlowTest(){
		thiss = this;
		
		openTest();
	}
	
	private void openTest() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open");   
		 
		int userSelection = fileChooser.showOpenDialog(thiss);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    File fileToOpen = fileChooser.getSelectedFile();
		    
		    try {
				
				parse(fileToOpen);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
		    System.out.println("Opened  file: " + fileToOpen.getAbsolutePath());
		}
	}
	
	private void parse(File file)
	{
		
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

			String compName = doc.getDocumentElement().getElementsByTagName("Name").item(0).getTextContent();
			String low = doc.getDocumentElement().getElementsByTagName("YearLow").item(0).getTextContent();
			String high = doc.getDocumentElement().getElementsByTagName("YearHigh").item(0).getTextContent();
			String marketcap = doc.getDocumentElement().getElementsByTagName("MarketCapitalization").item(0).getTextContent();

			System.out.println(compName + " has a year low of: " + low
					+ " and a year high of " + high
					+ " Its current market capitalization is " + marketcap);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		new GlowTest();
	}
}
