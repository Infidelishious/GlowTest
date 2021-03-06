package com.imglow.glowtest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Test {
	
	static final int INORDER = 1,
			RANDOM = 2,
			LEAST = 3,
			WORST = 4;
	
	static final int TEXT_WIDTH = 100;
			
	public ArrayList<Question> qs, wrong;
	public Map<String, ArrayList<Question>> map;
	public Scanner in;
	public GlowTest main;
	
	int asked, correct;
	long start, end;
	
	Random rand;
	
	public Test(GlowTest main, ArrayList<Question> qs, Map<String, ArrayList<Question>> map, Scanner in, int testType){
		this.qs = qs;
		this.map = map;
		this.main = main;
		
		wrong = new ArrayList<Question>();
		
		asked = 0;
		correct = 0;
		rand = new Random(System.nanoTime());
		
		if(testType == RANDOM)
			Collections.shuffle(qs, rand);
		else if(testType == LEAST)
			Collections.sort(qs, Question.LeastComparator);
		else if(testType == WORST)
			Collections.sort(qs, Question.Worst);
		
		start = System.currentTimeMillis();
		for(int i = 0; i < qs.size(); i++)
		{
			System.out.print("\n" + (i + 1) + ") ");
			printWrapped(qs.get(i).question, TEXT_WIDTH);
			
			if(qs.get(i).type.equalsIgnoreCase("MC"))
			{
				ArrayList<String> answers = getAnswers(qs.get(i));
				
				Collections.shuffle(answers, rand);
				
				for(int j = 0; j < 4; j++)
				{
					char c = (char) ('A' + j);
					System.out.println(c + ": " + answers.get(j));
				}
				
				String st = in.nextLine();
				
				if(st.equals("QUIT"))
					break;
				
				asked++;
				
				if((4 >= st.charAt(0)-'A' && st.charAt(0)-'A' >= 0) 
					&& (qs.get(i).checkandupdate(answers.get(st.charAt(0)-'A'))))
				{
					System.out.println("Correct!");
					correct++;
				}
				else
				{
					System.out.println("Wrong!");
					System.out.println("Answer: " + qs.get(i).answer);
					wrong.add(qs.get(i));
				}
			}
			else if(qs.get(i).type.equalsIgnoreCase("SP"))
			{
				String st = in.nextLine();
				
				if(st.equals("QUIT"))
					break;
				
				asked++;
				
				if(qs.get(i).checkandupdate(st))
				{
					System.out.println("Correct!");
					correct++;
				}
				else
				{
					System.out.println("Wrong!");
					System.out.println("Answer: " + qs.get(i).answer);
					wrong.add(qs.get(i));
				}
			}
			else
			{
				System.out.println("(press enter to see other side)");
				
				String st = in.nextLine();
				
				if(st.equals("QUIT"))
					break;
				
				asked++;
				
				System.out.println("Answer: " + qs.get(i).answer);
				
				
				int tt = main.getInt("\nWere you correct?\n1) Yes\n2) No", 1, 2, true);
				
				if(tt == 1)
				{
					System.out.println("Correct!");
					qs.get(i).checkandupdate(qs.get(i).answer);
					correct++;
				}
				else
				{
					System.out.println("Wrong!");
					qs.get(i).checkandupdate("");
					wrong.add(qs.get(i));
				}
			}
		}
		
		end = System.currentTimeMillis();
		
		System.out.println("\nScore: " + correct + "/" + asked + "(" + String.format("%,.1f", ((correct/(float)asked)) * 100) + "%)");
		System.out.println("Time: " + String.format("%,.2f", ((end - start) / 1000f)) + "s");
	}

	private void printWrapped(String question, int i) {
		if(question.length() > i)
		{
			System.out.println(question.substring(0,i));
			printWrapped(question.substring(i), i);
		}
		else
		System.out.println(question);
	}

	private ArrayList<String> getAnswers(Question question) {
		ArrayList<String> array = new ArrayList<String>();
		ArrayList<Question> tagArray = map.get(question.tag);
		
		array.add(question.answer);
		
		for(int i = 0; i < 3; i++)
		{
			array.add(tagArray.get((int)(Math.random() * tagArray.size())).answer);
		}
		return array;
	}

}
