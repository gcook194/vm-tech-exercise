package com.virginmoney.transactionlog.cucumberglue;

import com.virginmoney.transactionlog.CucumberUtils;
import com.virginmoney.transactionlog.config.propertysource.BasicAuthProperties;
import com.virginmoney.transactionlog.dto.GetTransactionListDTO;
import com.virginmoney.transactionlog.dto.TotalOutgoingListDTO;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class TransactionSteps {

    private final BasicAuthProperties authProperties;

    @LocalServerPort
    private String port;

    private ResponseEntity<GetTransactionListDTO> transactionListResponse;
    private ResponseEntity<TotalOutgoingListDTO> totalOutgoingResponse;

    @When("the client calls endpoint {string}")
    public void whenClientCalls(String url) {
        final String token = CucumberUtils.getJwtForTesting(port, authProperties.username(), authProperties.password());
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization" , "Bearer " + token);

        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(headers);

        try {
            transactionListResponse = new RestTemplate()
                    .exchange("http://localhost:" + port + url,
                            HttpMethod.GET,
                            entity,
                            GetTransactionListDTO.class);

        } catch (HttpClientErrorException httpClientErrorException) {
            httpClientErrorException.printStackTrace();
        }
    }

    @Then("response status code is {int}")
    public void thenStatusCode(int expected) {
        assertThat(transactionListResponse).isNotNull();
        assertThat(transactionListResponse.getStatusCode().value()).isEqualTo(expected);
    }

    @Then("response body should contain all transactions")
    public void thenEmptyResponseBody() {
        assertThat(transactionListResponse).isNotNull();

        final GetTransactionListDTO responseBody = transactionListResponse.getBody();

        assertThat(responseBody.transactions()).isNotEmpty();
        assertThat(responseBody.transactions().size()).isEqualTo(7); // would obviously write more thorough tests here
    }

    @Then("response body should contain only transactions with a category of {string}")
    public void thenResponseContainsOnlyTransactionsForCategory(String category) {
        assertThat(transactionListResponse).isNotNull();

        final GetTransactionListDTO responseBody = transactionListResponse.getBody();

        assertThat(responseBody.transactions()).isNotEmpty();
        responseBody.transactions().forEach(transaction -> assertThat(transaction.category()).isEqualTo(category));
    }
}
