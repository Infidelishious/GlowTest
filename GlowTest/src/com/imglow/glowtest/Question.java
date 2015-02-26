package com.imglow.glowtest;

import java.util.Comparator;

import org.json.JSONObject;

public class Question {
	String question, answer, tag, type;
	int asked, correct;
	JSONObject json;

	public float getWinRate()
	{
		if(asked > 0)
			return correct/(float)asked;
		else
			return 0;
	}

	public boolean check(String answer)
	{
		return answer.equalsIgnoreCase(this.answer);
	}

	public boolean checkandupdate(String answer)
	{
		asked++;

		if(check(answer))
		{
			correct++;
			return true;
		}
		return false;
	}
	
	public void prep()
	{
		json.remove("asked");
		json.put("asked", asked);
		
		json.remove("correct");
		json.put("correct", correct);
		
//		json.remove("type");
//		json.put("type", type);
//		
//		json.remove("tag");
//		json.put("tag", tag);
//		
//		json.remove("answer");
//		json.put("answer", answer);
//		
//		json.remove("question");
//		json.put("question", question);
	}
	
	public void reset() {
		asked = 0;
		correct = 0;
	}
	
	public static Comparator<Question> LeastComparator = new Comparator<Question>() {

		public int compare( Question s1, Question s2) {
			if(s1.asked == s2.asked)
				return 0;
			if(s1.asked > s2.asked)
				return 1;
			return -1;
		}};

		/*Comparator for sorting the list by roll no*/
	public static Comparator<Question> Worst = new Comparator<Question>() {

		public int compare(Question s1, Question s2) {
			if(s1.getWinRate() == s2.getWinRate())
				return 0;
			if(s1.getWinRate() > s2.getWinRate())
				return 1;
			return -1;
		}};



}
