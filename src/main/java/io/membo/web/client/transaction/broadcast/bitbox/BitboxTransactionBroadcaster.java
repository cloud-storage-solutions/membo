package io.membo.web.client.transaction.broadcast.bitbox;

import io.membo.web.client.transaction.broadcast.InvalidTransactionException;
import io.membo.web.client.transaction.broadcast.ServiceDownException;
import io.membo.web.client.transaction.broadcast.TransactionBroadcaster;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class BitboxTransactionBroadcaster implements TransactionBroadcaster {
    private final String BITBOX_ENDPOINT = "https://rest.bitbox.earth/v1/rawtransactions/sendRawTransaction/";

    @Override
    public void broadcastTransaction(String txHex) throws ServiceDownException, InvalidTransactionException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            RequestEntity<Void> build = RequestEntity.post(
                    new URI(BITBOX_ENDPOINT + txHex))
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
            ResponseEntity<String> exchange = restTemplate.exchange(build, String.class);

            if (!isBroadcastSuccessful(exchange)) {
                throw new InvalidTransactionException(
                        "Transaction broadcasting failed. Probably the transaction is invalid: " + txHex);
            }
        } catch (URISyntaxException e) {
            throw new ServiceDownException("The bitbox endpoint is down at the moment. Try again later.", e);
        }
    }

    private boolean isBroadcastSuccessful(ResponseEntity<String> exchange) {
        return exchange.getStatusCode().is2xxSuccessful() && isResponseATxId(exchange.getBody());
    }

    private boolean isResponseATxId(String response) {
        final int QUOTES_LENGTH = 2;
        final int HEX_LENGTH = 64;
        final int TX_ID_LENGTH = HEX_LENGTH + QUOTES_LENGTH;

        return response.length() == TX_ID_LENGTH &&
                !response.contains(" ");
    }
}
