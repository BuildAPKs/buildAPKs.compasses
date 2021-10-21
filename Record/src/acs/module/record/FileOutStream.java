package acs.module.record;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class FileOutStream implements Iterator {
	String actual;
	File file;
	BufferedReader br;
	
	public FileOutStream(File file){
		this.file=file;
		try {
			this.br=new BufferedReader(new FileReader(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasNext() {
		try {
			return ((actual=br.readLine())!=null);
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public String next() {
		return actual;
	}
	

	@Override
	public void remove() {
		System.err.println("Unimplemented method remove!!!");
		
	}
}
