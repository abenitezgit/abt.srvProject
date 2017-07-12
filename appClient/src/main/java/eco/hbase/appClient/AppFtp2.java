package eco.hbase.appClient;

import java.util.Calendar;
import java.util.Date;

import eco.ftp.ConvertMP3;
import eco.ftp.FTPClientMp3;
import eco.ftp.FTPClientWav;
import eco.hbase.model.FtpWavModel;

public class AppFtp2 {

	public static void main(String[] args) {
		
		FtpWavModel fm = new FtpWavModel();
		
		fm.setHost("192.168.223.61");
		fm.setUser("ftp_ttr2");
		fm.setPass("ftp2012");
		fm.setRemotePath("/u10/ftp_ttr2/2017/22/Plataforma_0008/SKILL_103EPH/");
		fm.setRemoteFile("20170603_160834_Skill_103EPH_024202A1EC020A21_56143_226047211.WAV");
		fm.setLocalFile("temp2.WAV");
		fm.setWorkFolder("/usr/local/hadoop/utils/");
		
		FTPClientWav ftpWav = new FTPClientWav();
		ftpWav.setConfig(fm);
		
		if (ftpWav.validaParams()) {
			
			ftpWav.getWAVtoMP3();
			
			if (ftpWav.exitStatus()==0) {
				System.out.println("FTP Exitoso!");
			} else {
				System.out.println("Error FTP");
			}
		}
		
		System.exit(0);
		
		

		String host = "192.168.223.61";
		String user = "ftp_ttr2";
		String pass = "ftp2012";
		String fname = "20170601_091858_Skill_103EPH_024202A1EBFFF51B_5227_322154700.WAV";
		String ftpFolder = "/u10/ftp_ttr2/2017/22/Plataforma_0008/SKILL_103EPH/";
		
		FTPClientMp3 ftpClient = new FTPClientMp3(host,user,pass,ftpFolder);
		
		String folder = "/usr/local/hadoop/utils";
		String source = "QA_01_20161201_181547_94410993323272__1480626947.13876.wav";
		String output = "paso2.mp3";

		
		
		ConvertMP3 convert = new ConvertMP3();
		
		
		convert.setConfig(folder, source, output);
		
		if (convert.isValidaParams()) {
			
			System.out.println("Validado!!");
			
			try {
				convert.convert();
				
				System.out.println(convert.getStdInput().toString());
				System.out.println(convert.getStdError().toString());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			System.out.println("Terminado!! "+ Calendar.getInstance().getTime());
			
		} else {
			System.out.println("Error de validacion de parametros");
		}
		
	}
	
}
