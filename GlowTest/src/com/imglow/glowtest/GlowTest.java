package com.imglow.glowtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.json.JSONArray;	
import org.json.JSONObject;

public class GlowTest extends JFrame{
	private static final long serialVersionUID = -2431572766861289179L;
	
	public GlowTest thiss;
	public String testTitle;
	public Scanner in;
	public File file;
	
	public JSONObject doc, prefsDoc;
	
	public ArrayList<Question> qs;
	public Map<String, ArrayList<Question>> map;

	private String lastTest = "", 
			lastTestPath = "",
			testPath = "";
	
	public GlowTest(){
		thiss = this;
		qs = new ArrayList<Question>();
		map = new HashMap<String,ArrayList<Question>>();
		in = new Scanner(System.in);
		
		System.out.println("GLOW TEST\n-------");
		
		openPrefs();
		int lt = 0;
		if(!lastTest.equals(""))
		{
			System.out.println();
			lt = getInt("Load Test " + lastTest + "?\n1) Yes\n2) No\n", 1, 2, true);
		}
		
		if(lt == 1)
			parse(lastTestPath);
		else
			openTest();
		
		makeMap();
		
		System.out.println("Test: " + testTitle +"\n");
		int tt = 5;
				
		while(tt == 5)
		{
			//System.out.println("Woot");
			tt = getInt("1) In Order Test\n2) Random Test\n3) Least Asked First\n4) Worst First\n5) Reset Asked/Correct Count\n", 1, 5, true);
			if(tt == 5)
			{
				for(Question i : qs)
				{
					i.reset();
				}
				save();
			}
		}
		
		Test test = new Test(this, qs, map, in, tt);
		
		save();
		savePrefs();
	}

	private void savePrefs() {
		prefsDoc = new JSONObject();
		prefsDoc.put("lastTest", testTitle);
		prefsDoc.put("lastTestPath", testPath);
		
		try {
			PrintWriter out = new PrintWriter("prefs");
			out.println(prefsDoc.toString());
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void openPrefs() {
		try {
			prefsDoc = new JSONObject(readFile("prefs", StandardCharsets.UTF_8));
		
			lastTest = prefsDoc.getString("lastTest");
			lastTestPath = prefsDoc.getString("lastTestPath");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("No Prefs File Found");
		}
	}
	
	public void save() {
		for(Question i : qs)
			i.prep();
		
		try {
			PrintWriter out = new PrintWriter(testPath);
			out.println(doc.toString());
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void makeMap() {
		for(Question q:qs)
		{
			try
			{
				ArrayList<Question> array = map.get(q.tag);
				array.add(q);
			}
			catch(Exception e)
			{
				ArrayList<Question> array = new ArrayList<Question>();
				array.add(q);
				map.put(q.tag, array);
			}
		}
	}

	private void openTest() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open");   
		 
		int userSelection = fileChooser.showOpenDialog(thiss);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    File fileToOpen = fileChooser.getSelectedFile();
		    
		    try {
				
				parse(fileToOpen.getAbsolutePath());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
		    System.out.println("Opened  file: " + fileToOpen.getAbsolutePath());
		}
	}
	
	private void parse(String file)
	{
		try {
			doc = new JSONObject(readFile(file, StandardCharsets.UTF_8));
			//this.file = file;
			testPath = file;
			testTitle = doc.getString("test");
			
			JSONArray arr = doc.getJSONArray("questions");
			
			for(int i = 0; i < arr.length(); i++)
			{
				Question q = new Question();
				q.question = arr.getJSONObject(i).getString("question");
				q.answer = arr.getJSONObject(i).getString("answer");
				q.asked = arr.getJSONObject(i).getInt("asked");
				q.correct = arr.getJSONObject(i).getInt("correct");
				q.tag = arr.getJSONObject(i).getString("tag");
				q.type = arr.getJSONObject(i).getString("type");
				q.json = arr.getJSONObject(i);
				
				qs.add(q);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static String readFile(String path, Charset encoding) throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
	
	int getInt(String prompt, int min, int max, boolean endline)
	{
		System.out.print(prompt);
		int temp;
		while(true)
		{
			try
			{
				temp = in.nextInt();
				if(temp <= 0)
				{
					System.out.println("That is not an option. Please try again");
					continue;
				}
				else if(temp > max || temp < min)
				{
					System.out.println("That is not an option. Please try again");
					continue;
				}
				
				in.nextLine();
				
				if(endline)
					System.out.println();
				break;
			}
			catch(Exception c)
			{
				in.nextLine();
				System.out.println("Error, not a number. Please try again");
			}
		}

		return temp;
	}
	
	public static void main(String[] args){
		new GlowTest();
	}
}
