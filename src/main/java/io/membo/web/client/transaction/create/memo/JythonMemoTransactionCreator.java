package io.membo.web.client.transaction.create.memo;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class JythonMemoTransactionCreator implements MemoTransactionCreator {
    public String createTransaction(String memoContent) {
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
        PyObject result = someFunc.__call__();
        String realResult = (String) result.__tojava__(String.class);

        return realResult;
    }
}
