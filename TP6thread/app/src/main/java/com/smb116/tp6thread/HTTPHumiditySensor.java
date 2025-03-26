package com.smb116.tp6thread;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

// Classe de lecture d'un capteur sur le Web
public class HTTPHumiditySensor extends HumiditySensorAbstract {
    //public final static String DEFAULT_HTTP_SENSOR = "http://localhost:8999/ds2438/";
    public final static String DEFAULT_HTTP_SENSOR = "http://10.0.2.2:8999/ds2438/";
    public static final long ONE_MINUTE = 60L * 1000L;


    // L'URL associÃ©e au capteur
    private String urlSensor;


    //Constructeur d'une connexion avec un Capteur, valeur par dÃ©faut * @param  urlSensor l'URL du capteur sur le Web en protocole HTTP
    public HTTPHumiditySensor() {
        this(DEFAULT_HTTP_SENSOR);
    }

    /**
     * Constructeur d'une connexion avec un Capteur, syntaxe habituelle
     * http://site:port/
     * @param urlSensor l'URL du capteur sur le Web en protocole HTTP
     */
    public HTTPHumiditySensor(String urlSensor) {
        this.urlSensor = urlSensor;
    }

    //Lecture de la valeur de humiditÃ© relative
    public float value() throws Exception {
        String sr = request();
        String[] words = sr.split("\"");
        float f = Float.parseFloat(words[9]) * 10F;
        return ((int) f) / 10F;
    }

    public long minimalPeriod() {
        if (urlSensor.startsWith("http://localhost") || urlSensor.startsWith("http://10.0.2.2"))
            return 200L; // Ã  valider
        else
            return 2000L; //ONE_MINUTE;
    }

    /**
     * lecture de l'URL
     * @return l'url associÃ©e Ã  ce capteur
     */
    public String getUrl() {
        return this.urlSensor;
    }

    /**
     * Lecture des informations issues de ce capteur
     * @return la totalitÃ© de la page lue
     * @throws Exception en cas d'erreur
     */
    private String request() throws Exception {
        URL url = new URL(urlSensor);
        URLConnection connection = url.openConnection();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()), 128); //78 caractÃ¨res
        String result = new String("");
        String inputLine = in.readLine();
        while (inputLine != null) {
            result = result + inputLine;
            inputLine = in.readLine();
        }
        in.close();
        return result;
    }
}
