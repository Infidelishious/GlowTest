package com.imglow.glowtest;

import org.json.JSONObject;

public class Question {
	String question, answer, tag, type;
	int asked, correct;
	JSONObject json;
	
	public float getWinRate()
	{
		return correct/(float)asked;
	}
	
	public boolean check(String answer)
	{
		return answer.equalsIgnoreCase(this.answer);
	}
	
	public boolean checkandupdate(String answer)
	{
		asked++;
		json.remove("asked");
		json.put("asked", asked);
		
		
		if(check(answer))
		{
			correct++;
			json.remove("correct");
			json.put("correct", asked);
			return true;
		}
		return false;
	}
}
