package bc.utils.llama;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public interface ConnectionFactory {
    HttpURLConnection create(URL url) throws IOException;
}
