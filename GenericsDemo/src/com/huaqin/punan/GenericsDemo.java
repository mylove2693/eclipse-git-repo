package com.huaqin.punan;

public class GenericsDemo {
	
	class Point<T> {
		private T var;
		
		public T getVar(){
			return var;
		}
		
		public void setVar(T var){
			this.var = var;
		}
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Point<String> p = null;
		
		//p = new Point<String>();
		
		p.setVar("it");
		System.out.println(p.getVar().length());
	}


	
	
	
}
