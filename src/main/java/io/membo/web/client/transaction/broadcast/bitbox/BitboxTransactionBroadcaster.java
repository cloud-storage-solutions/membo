package io.membo.web.client.transaction.broadcast.bitbox;

import io.membo.web.client.transaction.broadcast.InvalidTransactionException;
import io.membo.web.client.transaction.broadcast.ServiceDownException;
import io.membo.web.client.transaction.broadcast.TransactionBroadcaster;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class BitboxTransactionBroadcaster implements TransactionBroadcaster {
    private final String BITBOX_ENDPOINT = "https://rest.bitcoin.com/v2/rawtransactions/sendRawTransaction/";

    @Override
    public void broadcastTransaction(String txHex) throws ServiceDownException, InvalidTransactionException {
        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec("curl -X GET \"" + BITBOX_ENDPOINT + txHex + "\" -H \"accept: */*\"");
            p.waitFor();
            BufferedReader reader =
                  new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine())!= null) {
                output.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!isResponseATxId(output.toString())) {
            throw new InvalidTransactionException(
                  "Transaction broadcasting failed. Probably the transaction is invalid. Transaction hex: " + txHex
                        + ". \nBroadcast error: " + output.toString());
        }
    }

    private boolean isBroadcastSuccessful(ResponseEntity<String> exchange) {
        return exchange.getStatusCode().is2xxSuccessful() && isResponseATxId(exchange.getBody());
    }

    private boolean isResponseATxId(String response) {
        final int QUOTES_LENGTH = 2;
        final int HEX_LENGTH = 64;
        final int NEW_LINE_LENGTH = 1;
        final int TX_ID_LENGTH = HEX_LENGTH + QUOTES_LENGTH + NEW_LINE_LENGTH;

        return response.length() == TX_ID_LENGTH && !response.contains(" ");
    }
}
