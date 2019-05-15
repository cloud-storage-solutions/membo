package io.membo.web.client;

import io.membo.Account;
import io.membo.web.client.transaction.create.TransactionCreationException;
import io.membo.web.client.transaction.create.memo.MemoTransactionCreator;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class ProcessMemoTransactionCreator implements MemoTransactionCreator {
   @Override
   public String createTransaction(String memoContent) throws TransactionCreationException {
      String publicKey = "bitcoincash:" + Account.publicKey;
      String privateKey = Account.privateKey;
      Process exec = createPythonProcess(memoContent, publicKey, privateKey);

      if (exec.exitValue() != 0) {
         String errorOutput = new BufferedReader(new InputStreamReader(exec.getErrorStream()))
               .lines().collect(Collectors.joining("\n"));
         throw new TransactionCreationException(errorOutput);
      }

      return new BufferedReader(new InputStreamReader(exec.getInputStream()))
            .lines().collect(Collectors.joining("\n"));
   }

   private Process createPythonProcess(String memoContent, String publicKey, String privateKey) throws TransactionCreationException {
      String pythonPath = "D:\\Dropbox\\software\\windows\\programming-tools\\python-3\\python"; // TODO: put it in a config file
     //pythonPath = "/usr/bin/python";
      String memboPath = "D:/tools/memboo/membo";
     //memboPath = "/mnt/d/tools/memboo/membo";
      try {
         return Runtime.getRuntime().exec(pythonPath + " -c \"" +
               "import sys;" +
               "sys.path.insert(0, '" + memboPath + "');" +
               "from bchmemo import MemoUser;" +
               "user=MemoUser('" + publicKey + "');" +
               "user.private_key = '" + privateKey + "';" +
               "print(user.get_post_memo_signed_transaction('" + memoContent + "'))");
      } catch (IOException e) {
         throw new TransactionCreationException("Failed to create Memo transaction.", e);
      }
   }
}
