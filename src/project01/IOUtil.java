package project01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class IOUtil {
	
	private static FileInputStream fis;
	private static InputStreamReader isr;
	private static FileOutputStream fos;
	private static OutputStreamWriter osw;
	private static BufferedReader br;
	private static BufferedWriter bw;
	
	public static BufferedReader getBufferedReader(String file, String charsetName) {

		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, charsetName);
			br = new BufferedReader(isr);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return br;
	}
	
	public static BufferedWriter getBufferedWriter(String file, String charsetName) {
		
		try {
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos, charsetName); 
			bw = new BufferedWriter(osw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bw;
	}
	
	public static void closeResource() throws IOException {
		if (br != null) {
			br.close();
		}
		if (bw != null) {
			bw.close();
		}

	}
}
