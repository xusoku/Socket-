package com.cooge.media.proxy;



public class NumberUtil {

	
	private static String letters = "0123456789abcdef";
	
	
	public static void main(String[] args) {
		String num = "a10y4b";
		Long dd= StringTOLong(num);
		System.out.println(dd);
		System.out.println(LongTOString(dd));
	}
	
	
	
	public static int  StringtoInt(char letter){
		return letters.indexOf(letter);
	}
	public static char  StringtoInt(int index){
		return letters.charAt(index);
	}

	public static Long StringTOLong(String num){
		char [] chs = num.toCharArray();
		Long chnum = 0L;
		for(int i =0 ;i<chs.length;i++){
			chnum = (long) (chnum+StringtoInt(chs[i])*Math.pow(letters.length(),chs.length-i-1));
		}
		return chnum;
	}
	public static String LongTOString(long num){
		String str="";
		while(true){
			int in = (int) (num%letters.length());
			num = (long) (num/letters.length());
			str = StringtoInt(in)+str;
			if(num==0)break;
		}
		return str;
	}
}
