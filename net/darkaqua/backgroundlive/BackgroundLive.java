package net.darkaqua.backgroundlive;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;
import com.sun.jna.win32.*;

public class BackgroundLive {
   public static void main(String[] args) throws Exception {
	   
	   URL url = new URL("http://www2.lulea.se/web-camera/live_00001.jpg");
	   InputStream in = new BufferedInputStream(url.openStream());
	   ByteArrayOutputStream out = new ByteArrayOutputStream();
	   byte[] buf = new byte[1024];
	   int n = 0;
	   while (-1!=(n=in.read(buf)))
	   {
	      out.write(buf, 0, n);
	   }
	   out.close();
	   in.close();
	   byte[] response = out.toByteArray();
	   
	   String path = "C:\\Users\\Pablo\\Pictures\\background.jpg";
	   
	   FileOutputStream fos = new FileOutputStream(path);
	   fos.write(response);
	   fos.close();
	   
      //supply your own path instead of using this one
      

      SPI.INSTANCE.SystemParametersInfo(
          new UINT_PTR(SPI.SPI_SETDESKWALLPAPER), 
          new UINT_PTR(0), 
          path, 
          new UINT_PTR(SPI.SPIF_UPDATEINIFILE | SPI.SPIF_SENDWININICHANGE));
   }

   public interface SPI extends StdCallLibrary {

      //from MSDN article
      long SPI_SETDESKWALLPAPER = 20;
      long SPIF_UPDATEINIFILE = 0x01;
      long SPIF_SENDWININICHANGE = 0x02;

      @SuppressWarnings("serial")
      SPI INSTANCE = (SPI) Native.loadLibrary("user32", SPI.class, new HashMap<Object, Object>() {
         {
            put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
            put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
         }
      });

      boolean SystemParametersInfo(
          UINT_PTR uiAction,
          UINT_PTR uiParam,
          String pvParam,
          UINT_PTR fWinIni
        );
  }
}