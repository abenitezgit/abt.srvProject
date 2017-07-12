package eco.ftp;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.PrintCommandListener;
import static org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


public class FTPClientMp3 {
	String host;
	String user;
	String pass;
	String folder;
	
	public FTPClientMp3(String host, String user, String pass, String folder) {
		this.host = host;
		this.user = user;
		this.pass = pass;
		this.folder = folder;
	}
	
    @SuppressWarnings("resource")
	public void getWAV(String grabacion, OutputStream outStream) {
        FTPClient ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        File tempFile = null;
        File tempDecodedFile = null;
        InputStream is = null;
        FileOutputStream out;

        try {
            tempFile = File.createTempFile("stream", ".wav",new File(folder));
            tempDecodedFile = File.createTempFile("stream-decoded", ".mp3",new File(folder));
            out = new FileOutputStream(tempFile);
            
            ftp.connect(host);

            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new Exception("Ocurrió un problema al intentar conectar con el servidor FTP");
            }
            ftp.login(user, pass);
            ftp.setFileType(BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            is = ftp.retrieveFileStream(grabacion);
            System.out.println("File received!");
            IOUtils.copy(is, out);
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(is);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
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
        System.out.println("Prepare to encode!");
        try {
            FileInputStream in = new FileInputStream(tempDecodedFile);
            transformToMp3(tempFile, tempDecodedFile);
            IOUtils.copy(in, outStream);
            IOUtils.closeQuietly(in);
        } catch(Exception ex) {
            //ex.printStackTrace();
        } finally {
            
        }

        tempFile.deleteOnExit();
        tempDecodedFile.deleteOnExit();
    }
	
    public void transformToMp3(File in, File out) {

    	
    	//Next example takes an audio WAV file and generates a 128 kbit/s, stereo, 44100 Hz MP3 file:

    		File source = new File("/Users/admin/Documents/mp3/source.wav");
    		File target = new File("/Users/admin/Documents/mp3/target.mp3");
//    		AudioAttributes audio = new AudioAttributes();
//    		audio.setCodec("libmp3lame");
//    		audio.setBitRate(new Integer(16000));
//    		audio.setChannels(new Integer(1));
//    		audio.setSamplingRate(new Integer(22050));
//    		EncodingAttributes attrs = new EncodingAttributes();
//    		attrs.setFormat("mp3");
//    		attrs.setAudioAttributes(audio);
//    		Encoder encoder = new Encoder();
//    		
//    		try {
//				String[] decs = encoder.getSupportedEncodingFormats();
//				
//				int l = decs.length;
//				
//				for (int i=0; i<l; i++) {
//					System.out.println("enc: "+decs[i]);
//				}
//				
//			} catch (EncoderException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//    		
//    		try {
//				encoder.encode(source, target, attrs);
//			} catch (IllegalArgumentException | EncoderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    	
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(new Integer(16000));
        audio.setChannels(new Integer(1));
        audio.setSamplingRate(new Integer(22050));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);
        String location = "/usr/local/srvProject/ffmpeg";
        Encoder encoder = new Encoder(new FFMPEGCustomLocator(location));
        System.out.println("Encoding...");
        
        try {
			String[] encs = encoder.getAudioEncoders();
			
			int nn = encs.length;
			System.out.println(nn);
			
		} catch (EncoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
            encoder.encode(source, target, attrs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Encoded");
    }
	

	
}
