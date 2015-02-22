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
	
	public JSONObject doc;
	
	public ArrayList<Question> qs;
	public Map<String, ArrayList<Question>> map;
	
	public GlowTest(){
		thiss = this;
		qs = new ArrayList<Question>();
		map = new HashMap<String,ArrayList<Question>>();
		in = new Scanner(System.in);
		
		openTest();
		makeMap();
		Test test = new Test(qs, map, in);
		
		try {
			PrintWriter out = new PrintWriter(file.getAbsolutePath());
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
			doc = new JSONObject(readFile(file.getAbsolutePath(), StandardCharsets.UTF_8));
			this.file = file;
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
	
	public static void main(String[] args){
		new GlowTest();
	}
}
