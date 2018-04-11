package mapapp.cs4985.westga.edu.untitled;

import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class ThreadFetcher extends Thread {
        final String userAgent = "Android weather app";
        final String acceptString = "text/html,application/*,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        final String encodingString = "gzip";
        final String languageString = "en-US,en;q=0.5";
        final String request;

        private StringBuffer sb;
        private boolean finished;
        private boolean success;

        public ThreadFetcher(String request) {
            this.request = request;
            sb = new StringBuffer();
            finished = false;
            success = false;
        }

        public boolean isFinished() {
            return finished;
        }

        public boolean successful() {
            return success;
        }

        @Override
        public void run() {

            URL url = null;
            HttpsURLConnection connection = null;
            Scanner scan = null;
            try {
                url = new URL(request);
                connection = (HttpsURLConnection) url.openConnection();

                if (connection == null) {
                    return;
                }
                connection.setRequestProperty("User-Agent", userAgent);
                connection.setRequestProperty("Accept", acceptString);
                connection.setRequestProperty("Accept-Encoding", acceptString);
                connection.setRequestProperty("Accept-Language", languageString);

                int response = connection.getResponseCode();
                if (response == HttpsURLConnection.HTTP_OK) {
                    scan = new Scanner(connection.getInputStream());
                } else {
                    scan = new Scanner(connection.getErrorStream());
                    sb.append("connection not ok\n");
                }
                while (scan.hasNext()) {
                    String line = scan.nextLine();
                    sb.append(line);
                }
                scan.close();
                success = true;
            } catch (Exception err) {
                err.printStackTrace();
            }

            finished = true;
            return;
        }

        public String getResult() {
            return sb.toString();
        }
    }
