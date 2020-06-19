import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

class HttpClient {

    void saveHtml(String host, String resName) throws IOException
    {

        try {
            host = host.trim();
            Socket s = new Socket(InetAddress.getByName(host), 80);
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println("GET " + resName + " HTTP/1.1");
            pw.println("Host: riweb.tibeica.com");
            pw.println("User-Agent: CLIENT RIW");
            pw.println("Connection: closed");
            pw.println("");

            pw.flush();
            try {
                String replaced = resName.replace("/", "");

                FileWriter myWriter;

                if(replaced.equals("crawl")) {
                    myWriter = new FileWriter(replaced + ".html");
                }
                else {
                    myWriter = new FileWriter(replaced);
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String t;
                t = br.readLine();
                FileWriter text = new FileWriter("request.txt");
                if (t.contains("4") || t.contains("5")) //eroare client / eroare server
                {
                    text.write(t);

                }
                else {

                    myWriter.write(t);
                    myWriter.write("\n");
                    while ((t = br.readLine()) != null) {
                        myWriter.write(t);
                        myWriter.write("\n");
                    }
                    myWriter.close();
                    br.close();
                }
                text.close();
            }
            catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        catch (UnknownHostException ex)
        {
            System.out.println(ex);
        }
    }
}
