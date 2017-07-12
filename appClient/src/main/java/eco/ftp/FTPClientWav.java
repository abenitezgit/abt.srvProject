package eco.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import static org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import eco.hbase.model.FtpWavModel;

public class FTPClientWav {
	FtpWavModel fm = new FtpWavModel();
	OutputStream outStream;
	InputStream isResponse;
	int exitStatus=0;
	
	public void setConfig(FtpWavModel fm) {
		this.fm = fm;
	}
	
	public boolean validaParams() {
		return true;
	}
	
	public int exitStatus() {
		return exitStatus;
	}
	
	public InputStream getInputStream() throws Exception {
		return isResponse;
	}
	
	public OutputStream getOutputStream() throws Exception {
		return outStream;
	}
	
    @SuppressWarnings("resource")
	public void getWAVtoMP3() {
        FTPClient ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;

        try {
        	ftp.setConnectTimeout(2000);
        	ftp.connect(fm.getHost());
        	
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new Exception("Ocurrió un problema al intentar conectar con el servidor FTP");
            }

            ftp.login(fm.getUser(), fm.getPass());
            ftp.setFileType(BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();

            File tempRemoteFile = File.createTempFile("stream", ".wav",new File(fm.getWorkFolder()));
            File tempDecodedFile = File.createTempFile("stream-decoded", ".mp3",new File(fm.getWorkFolder()));
            
            FileOutputStream outRemoteFile = new FileOutputStream(tempRemoteFile);   			//Puntero para escribir el archivo
            
            String remotePathFile = fm.getRemotePath()+fm.getRemoteFile();						//Remote Path y Name del WAV
            
            OutputStream outputRemoteStream = new BufferedOutputStream(outRemoteFile);  		//Buffer de Escritura hacia el puntero del archivo
            InputStream inputRemoteStream = ftp.retrieveFileStream(remotePathFile);     		//Buffer de Lectura desde un puntero de archivo
            
            //Leyendo el buffer de lectura y escribiendo el buffer de escritura - copia en memoria de buffers
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputRemoteStream.read(bytesArray)) != -1) {
            	outputRemoteStream.write(bytesArray, 0, bytesRead);
            }
 
            boolean success = ftp.completePendingCommand();
            if (success) {
                System.out.println("File #2 has been downloaded successfully.");
            }
            
            //Rutina para conversion del audio
            //Se entrega un buffer de lectura de un archivo : inputRemoteStream
            //Se espera un buffer de escritura de retorno para devolver
            
            ConvertMP3 convert = new ConvertMP3();
            convert.setConfig(fm.getWorkFolder(), tempRemoteFile.getName(), tempDecodedFile.getName());
            if (convert.isValidaParams()) {
            	int status = convert.convert();
            	if (status==0) {
            		FileInputStream inpStreamDecodedFile = new FileInputStream(new File(tempDecodedFile.getPath())); 
            		InputStream isDecodeFile = new BufferedInputStream(inpStreamDecodedFile);
            		isResponse = isDecodeFile;
            		
            		exitStatus = 0;
            	} else {
            		exitStatus = 1;
            	}
            }
            
            //Borrando archivos temporales
//            tempRemoteFile.deleteOnExit();
//            tempDecodedFile.deleteOnExit();

        } catch (Exception ex) {
        	exitStatus=1;
            ex.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.logout();
                    ftp.disconnect();
                } catch (Exception f) {
                    f.printStackTrace();
                }
            }
        }
    }
}
