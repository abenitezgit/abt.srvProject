package eco.hbase.appClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import eco.ftp.FTPClientMp3;

public class AppFtp {

	public static void main(String[] args) throws IOException {

		
		String peticion = " /Users/admin/Documents/paso/ffmpeg -y -i /Users/admin/Documents/paso/source.wav -ar 22050 -ac 1 /Users/admin/Documents/paso/output.mp3";
		
		Process p= Runtime.getRuntime().exec(peticion);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		String line="";
		
		
		
		while ((line = br.readLine() ) != null) {
			
			System.out.println("la linea es: "+line);
		}
		
		System.out.println(p.exitValue());
		
		
		
		ProcessBuilder pb = new ProcessBuilder("./ffmpeg", "-y", "-i","QA_20161203_120551_00022968458790_103_TTR42-1480777551.43530.WAV","-ac","1","-ar","22050","output.mp3");

	    Map<String, String> env = pb.environment();
	    // If you want clean environment, call env.clear() first
	    // env.clear()
	    env.put("VAR1", "myValue");
	    env.remove("OTHERVAR");
	    env.put("VAR2", env.get("VAR1") + "suffix");

	    File workingFolder = new File("/usr/local/hadoop/utils");
	    pb.directory(workingFolder);

	    Process proc = pb.start();

	    BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

	    BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

	    // read the output from the command
	    System.out.println("Here is the standard output of the command:\n");
	    String s = null;
	    while ((s = stdInput.readLine()) != null)
	    {
	        System.out.println(s);
	    }

	    // read any errors from the attempted command
	    System.out.println("Here is the standard error of the command (if any):\n");
	    while ((s = stdError.readLine()) != null)
	    {
	        System.out.println(s);
	    }
		
	}

}
