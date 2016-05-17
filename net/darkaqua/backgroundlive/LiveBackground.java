package net.darkaqua.backgroundlive;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;
/**
 * Modified again by pagoru on 17/05/2016.
 * Modified by cout970 on 14/05/2016.
 * Created by pagoru on 13/05/2016.
 * 
 */
public class LiveBackground {

    private static final String DEFAULT_CONFIG =
            "#url de la imagen\n" +
            "url = http://www2.lulea.se/web-camera/live_00001.jpg\n" +
            "#localizacion donde guardar la imagen\n" +
            "image = ./background.bmp\n" +
            "#segundos entre actualizaciones de imagen\n" +
            "time = 30\n";
    private static Properties config;

    public static void main(String[] args) throws Exception {

        loadConfig();

        while (true) {
        	
        	try{
        		URL url = new URL(config.getProperty("url"));
	            InputStream in = new BufferedInputStream(url.openStream());
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            byte[] buf = new byte[1024];
	            int n = 0;
	            while (-1 != (n = in.read(buf))) {
	                out.write(buf, 0, n);
	            }
	            out.close();
	            in.close();
	            byte[] response = out.toByteArray();

	            File file = new File(config.getProperty("image"));

	            FileOutputStream fos = new FileOutputStream(file);
	            fos.write(response);
	            fos.close();

	            BackgroundHelper.setBackground(file.getAbsolutePath());

	            System.out.println("Background reloaded.");
	            Thread.sleep(1000 * Integer.parseInt(config.getProperty("time")));
        	} catch(Exception e){
        		System.out.println("¿No connection?");
        	}

        }

    }

    private static void loadConfig() throws IOException {
        File f = new File("./config.txt");
        if (!f.exists())createDefaultConfigFile(f);
        Scanner s = new Scanner(new FileInputStream(f));

        String url = null;
        String image = null;
        int time = 60;
        while (s.hasNext()){
            String line = s.nextLine();
            if (line.startsWith("#"))continue;
            if (line.startsWith("url =")){
                url = line.replaceFirst("url =", "").trim();
            }else if(line.startsWith("image =")){
                image = line.replaceFirst("image =", "").trim();
            }else if(line.startsWith("time =")){
                String temp = line.replaceFirst("time =", "").trim();
                try{
                    time = Integer.parseInt(temp);
                }catch (Exception e){
                    System.err.println("Error parseando el valor de time: "+temp);
                    System.err.println("Se usara el valor por defecto 30 seg");
                    e.printStackTrace();
                    time = 30;
                }
                if (time < 1){
                    System.err.println("Valor de time invalido: "+time);
                    System.err.println("Se usara el valor por defecto 30 seg");
                    time = 30;
                }
            }else{
                System.err.println("Ignorando linea de la configuracion: "+line);
            }
        }

        config = new Properties();
        config.setProperty("url", url);
        config.setProperty("image", image);
        config.setProperty("time", String.valueOf(time));
        System.out.println("Configuracion: "+config);
        
        s.close();
    }

    private static void createDefaultConfigFile(File f) throws IOException {
        FileWriter writer = new FileWriter(f);
        writer.append(DEFAULT_CONFIG);
        writer.close();
    }
}
