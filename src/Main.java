import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("**********************************************");
        System.out.println("Seja bem vindo/a ao Conversor de moedas ^^");

        Scanner leituraDoTeclado = new Scanner(System.in);
        System.out.println("Digite o valor que deseja converter: ");
        float valor = leituraDoTeclado.nextInt();

        String codigoMoedaOrigem = "USD";
        String codigoMoedaDestino = "BRL";
        String API_KEY = "e8c2a59f795108b991f65119";
        String API_URL = "https://v6.exchangerate-api.com/v6/";
        String apiConversao = API_URL + API_KEY + "/pair/" + codigoMoedaOrigem +'/' + codigoMoedaDestino;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiConversao))
                .build();

        HttpResponse <String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        
        ExchangeRateApi exchangeRate = gson.fromJson(response.body(), ExchangeRateApi.class);

        float taxaDeConversao = exchangeRate.conversionRate();
        float valorConvertido = converterMoeda(valor, taxaDeConversao);

        System.out.printf("O valor  $ %.2f (%s) =  $ %.2f (%s)%n", valor, codigoMoedaOrigem, valorConvertido, codigoMoedaDestino);
    }

    public static float converterMoeda(float valorOriginal, float taxaDeConversao) {
        return valorOriginal * taxaDeConversao;
    }
}