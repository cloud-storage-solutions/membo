package io.membo.web.client;

import com.rometools.rome.io.FeedException;
import io.membo.web.client.convert.ConverterImpl;
import io.membo.web.client.rss.Post;
import io.membo.web.client.rss.RedditRssFetcher;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Entity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Submitter {
   private final static Post FAQ = new Post("", "https://www.reddit.com/r/btc/comments/5wwznc/please_read_our_frequently_asked_questions_faq/", "");

   private Set<Post> posted = new HashSet<>();
   private Set<Post> filtered = new HashSet<>(Collections.singletonList(FAQ));

   public void submit() throws IOException, FeedException, InterruptedException, URISyntaxException {
//      while (true) {
         RedditRssFetcher redditRssFetcher = new RedditRssFetcher(new ConverterImpl());
         List<Post> posts = redditRssFetcher.fetch();

         for (Post post : posts) {
            if (!filtered.contains(post) && !posted.contains(post)) {
               posted.add(post);
               broadcastTx(createTx(String.valueOf(post)));
            }
         }

//         Thread.sleep(1000 * 60 * 10); // 10 minutes
//      }
   }

   private void broadcastTx(String txHex) throws URISyntaxException {
      // POST to BitBox
//      new RequestEntity<String>(null, HttpMethod.POST,
//            ResponseEntity < String > response = restTemplate.postForEntity(url, request, String.class);

      RestTemplate restTemplate = new RestTemplate();
      RequestEntity<Void> build = RequestEntity.post(new URI("https://rest.bitbox.earth/v1/rawtransactions/sendRawTransaction/" + txHex))
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
      ResponseEntity<String> exchange = restTemplate.exchange(build, String.class);
      exchange.toString(); // verify it was successful
   }

   public String createTx(String memoContent) throws IOException {
      String memboPath = "X:\\Dropbox\\source-code\\java\\projects\\membo";
//      memoContent = "testing someting";

      // lubokkanev's
      String publicKey = "bitcoincash:qp4ek7dm8g8k84j96j5ye0wdrrxc7pcm5qhnmwpy29";
      String privateKey = "KyPu4vkM96pTnYgQuwmXAzwpLhX2haYuJrx9zwsXi5LZz3PWBdZS";

      Process exec = Runtime.getRuntime().exec("X:\\Dropbox\\software\\windows\\programming-tools\\python-3\\python -c \"" +
            "import sys;" +
            "sys.path.insert(0, '" + memboPath + "');" +
            "from bchmemo import MemoUser;" +
            "user=MemoUser('" + publicKey + "');" +
            "user.private_key = '" + privateKey + "';" +
            "print(user.get_post_memo_signed_transaction('" + memoContent + "'))");

      String errorOutput = new BufferedReader(new InputStreamReader(exec.getErrorStream()))
            .lines().collect(Collectors.joining("\n"));
      if (!errorOutput.equals("")) {
         System.out.println("Error output:");
         System.out.println(errorOutput);
      }

      return new BufferedReader(new InputStreamReader(exec.getInputStream()))
            .lines().collect(Collectors.joining("\n"));
   }

   public String createTx_jython() {
      PythonInterpreter interpreter = new PythonInterpreter();
      interpreter.exec("" +
            "import sys\n" +
            "sys.path.insert(0, 'X:\\Dropbox\\source-code\\java\\projects\\membo')\n" +
            "print(sys.path)\n" +
            "from bchmemo import MemoUser\n" +
            "def get_memo_tx():" +
            "    #lubokkanev\n" +
            "    user=MemoUser('bitcoincash:qp4ek7dm8g8k84j96j5ye0wdrrxc7pcm5qhnmwpy29')\n" +
            "    user.private_key = 'KyPu4vkM96pTnYgQuwmXAzwpLhX2haYuJrx9zwsXi5LZz3PWBdZS'\n" +
            "    #user.post_memo('Testing something')\n" +
            "    return user.get_post_memo_signed_transaction(\"testing someting\")" +
            "");

      PyObject someFunc = interpreter.get("get_memo_tx");
//      PyObject result = someFunc.__call__(new PyString("Test!"));
//      PyObject result = someFunc.__call__();
//      String realResult = (String) result.__tojava__(String.class);

//      System.out.println("The result from the python funciton is " + realResult);

      return null;
   }
}
