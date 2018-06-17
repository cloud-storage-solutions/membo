package io.membo.web.client.transaction.create.memo;

import io.membo.web.client.transaction.create.TransactionCreationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ProcessMemoTransactionCreator implements MemoTransactionCreator {
    @Override
    public String createTransaction(String memoContent) throws TransactionCreationException {
        String publicKey = "bitcoincash:qp4ek7dm8g8k84j96j5ye0wdrrxc7pcm5qhnmwpy29";
        String privateKey = "KyPu4vkM96pTnYgQuwmXAzwpLhX2haYuJrx9zwsXi5LZz3PWBdZS";

        Process exec = createPythonProcess(memoContent, publicKey, privateKey);
        String errorOutput = new BufferedReader(new InputStreamReader(exec.getErrorStream()))
                .lines().collect(Collectors.joining("\n"));
        System.out.println("Error output:\n" + errorOutput);

        return new BufferedReader(new InputStreamReader(exec.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
    }

    private Process createPythonProcess(String memoContent, String publicKey, String privateKey) throws TransactionCreationException {
        String pythonPath = "X:\\Dropbox\\software\\windows\\programming-tools\\python-3\\python";
        String memboPath = "X:\\Dropbox\\source-code\\java\\projects\\membo";

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
