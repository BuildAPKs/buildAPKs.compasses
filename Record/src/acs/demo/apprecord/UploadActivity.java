package acs.demo.apprecord;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
  
public class UploadActivity extends Activity {
     
    TextView messageText;
    Button uploadButton;
    Button cancelButton;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    Activity present=this;   
    String upLoadServerUri = null;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
         
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
          
        uploadButton = (Button)findViewById(R.id.cancel);
        messageText  = (TextView)findViewById(R.id.textViewupload);
        cancelButton=(Button)findViewById(R.id.upload);
        final String dir=Environment.getExternalStorageDirectory().getAbsolutePath();
        final String[] fileArray={dir+"/click.txt",dir+"/time.txt",dir+"/gps.txt",dir+"/sensor.txt"};
        upLoadServerUri = "http://www.replaylog.bugs3.com/logserver/UploadToServer.php";
         
        uploadButton.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                 
                dialog = ProgressDialog.show(UploadActivity.this, "", "Uploading file...", true);
                 
                new Thread(new Runnable() {
                    public void run() {
                         runOnUiThread(new Runnable() {
                                public void run() {
                                    messageText.setText("uploading "+fileArray[0]+"\n");
                                }
                            });                      
                       
                         uploadFile(fileArray[0]);
                                                  
                    }
                  }).start();   
                
                new Thread(new Runnable() {
                    public void run() {
                         runOnUiThread(new Runnable() {
                                public void run() {
                                    messageText.setText("uploading "+fileArray[1]+"\n");
                                }
                            });                      
                       
                         uploadFile(fileArray[1]);
                                                  
                    }
                  }).start();   
                
                new Thread(new Runnable() {
                    public void run() {
                         runOnUiThread(new Runnable() {
                                public void run() {
                                    messageText.setText("uploading "+fileArray[2]+"\n");
                                }
                            });                      
                       
                         uploadFile(fileArray[2]);
                                                  
                    }
                  }).start();   
                
                new Thread(new Runnable() {
                    public void run() {
                         runOnUiThread(new Runnable() {
                                public void run() {
                                    messageText.setText("uploading "+fileArray[3]+"\n");
                                }
                            });                      
                       
                         uploadFile(fileArray[3]);
                                                  
                    }
                  }).start();   
                }
            });
        
        cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				present.onBackPressed();
			}
		});
    }
      
    public int uploadFile(String sourceFileUri) {
           
           
          String fileName = sourceFileUri;
  
          HttpURLConnection conn = null;
          DataOutputStream dos = null;  
          String lineEnd = "\r\n";
          String twoHyphens = "--";
          String boundary = "*****";
          int bytesRead, bytesAvailable, bufferSize;
          byte[] buffer;
          int maxBufferSize = 1 * 1024 * 1024; 
          File sourceFile = new File(sourceFileUri); 
           
          if (!sourceFile.isFile()) {
               
               dialog.dismiss(); 
               return 0;
            
          }
          else
          {
               try { 
                    
                     // open a URL connection to the Servlet
                   FileInputStream fileInputStream = new FileInputStream(sourceFile);
                   URL url = new URL(upLoadServerUri);
                    
                   // Open a HTTP  connection to  the URL
                   conn = (HttpURLConnection) url.openConnection(); 
                   conn.setDoInput(true); // Allow Inputs
                   conn.setDoOutput(true); // Allow Outputs
                   conn.setUseCaches(false); // Don't use a Cached Copy
                   conn.setRequestMethod("POST");
                   conn.setRequestProperty("Connection", "Keep-Alive");
                   conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                   conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                   conn.setRequestProperty("uploaded_file", fileName); 
                    
                   dos = new DataOutputStream(conn.getOutputStream());
          
                   dos.writeBytes(twoHyphens + boundary + lineEnd); 
                   dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                           + fileName + "\"" + lineEnd);
                    
                   dos.writeBytes(lineEnd);
          
                   // create a buffer of  maximum size
                   bytesAvailable = fileInputStream.available(); 
          
                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   buffer = new byte[bufferSize];
          
                   // read file and write it into form...
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                      
                   while (bytesRead > 0) {
                        
                     dos.write(buffer, 0, bufferSize);
                     bytesAvailable = fileInputStream.available();
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                      
                    }
          
                   // send multipart form data necesssary after file data...
                   dos.writeBytes(lineEnd);
                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
          
                   // Responses from the server (code and message)
                   serverResponseCode = conn.getResponseCode();
                   String serverResponseMessage = conn.getResponseMessage();
                     
                   Log.i("uploadFile", "HTTP Response is : "
                           + serverResponseMessage + ": " + serverResponseCode);
                    
                   if(serverResponseCode == 200){
                        
                       runOnUiThread(new Runnable() {
                            public void run() {
                                 
                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                              +" http://replaylog.bugs3.com/UploadToServer.php";
                                 
                                messageText.setText(msg);
                                Toast.makeText(UploadActivity.this, "File Upload Complete.", 
                                             Toast.LENGTH_SHORT).show();
                            }
                        });                
                   }    
                    
                   //close the streams //
                   fileInputStream.close();
                   dos.flush();
                   dos.close();
                     
              } catch (MalformedURLException ex) {
                   
                  dialog.dismiss();  
                  ex.printStackTrace();
                   
                  runOnUiThread(new Runnable() {
                      public void run() {
                          messageText.setText("MalformedURLException Exception : check script url.");
                          Toast.makeText(UploadActivity.this, "MalformedURLException", 
                                                              Toast.LENGTH_SHORT).show();
                      }
                  });
                   
                  Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
              } catch (Exception e) {
                   
                  dialog.dismiss();  
                  e.printStackTrace();
                   
                  runOnUiThread(new Runnable() {
                      public void run() {
                          messageText.setText("Got Exception : see logcat ");
                          Toast.makeText(UploadActivity.this, "Got Exception : see logcat ", 
                                  Toast.LENGTH_SHORT).show();
                      }
                  });
                  Log.e("Upload file to server Exception", "Exception : "
                                                   + e.getMessage(), e);  
              }
              dialog.dismiss();       
              return serverResponseCode; 
               
           } // End else block 
         } 
}
