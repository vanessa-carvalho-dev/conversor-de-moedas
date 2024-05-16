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
    public static void main(String[] args) {
        String menu = """
                **********************************************
                Seja bem vindo/a ao Conversor de moedas ^^
                
                1) Dólar =>> Real brasileiro
                2) Real brasileiro =>> Dólar
                3) Euro =>> Real brasileiro
                4) Real brasileiro =>> Euro
                5) Iene japônes =>> Real brasileiro
                6) Real brasileiro =>> Iene japonês
                7) Sair
                Escolha uma opção válida:
                **********************************************
                """;

        System.out.println(menu);

        Scanner leituraDoTeclado = new Scanner(System.in);
        int opcao = leituraDoTeclado.nextInt();

        while(opcao != 7){
            System.out.println("Digite o valor que deseja converter: ");
            double valor = leituraDoTeclado.nextDouble();

            String codigoMoedaOrigem = "";
            String codigoMoedaDestino= "";

            switch (opcao){
                case 1:
                    codigoMoedaOrigem = "USD";
                    codigoMoedaDestino = "BRL";
                    break;
                case 2:
                    codigoMoedaOrigem = "BRL";
                    codigoMoedaDestino = "USD";
                    break;
                case 3:
                    codigoMoedaOrigem = "EUR";
                    codigoMoedaDestino = "BRL";
                    break;
                case 4:
                    codigoMoedaOrigem = "BRL";
                    codigoMoedaDestino = "EUR";
                    break;
                case 5:
                    codigoMoedaOrigem = "JPY";
                    codigoMoedaDestino = "BRL";
                    break;
                case 6:
                    codigoMoedaOrigem = "BRL";
                    codigoMoedaDestino = "JPY";
                    break;
            }

            try{
                ExchangeRateApi exchangeRate = requisicaoParaApiTaxaDeCambio(codigoMoedaOrigem, codigoMoedaDestino);
                double taxaDeConversao = exchangeRate.conversionRate();
                double valorConvertido = converterMoeda(valor, taxaDeConversao);

                System.out.printf("$ %.2f (%s) =  $ %.2f (%s)%n", valor, codigoMoedaOrigem, valorConvertido, codigoMoedaDestino);
            } catch (Exception e) {
                System.out.println("Algo deu errado");
            }

            System.out.println(menu);
            opcao = leituraDoTeclado.nextInt();

        }

    }

    private static ExchangeRateApi requisicaoParaApiTaxaDeCambio(String codigoMoedaOrigem, String codigoMoedaDestino) throws IOException, InterruptedException {
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

        return gson.fromJson(response.body(), ExchangeRateApi.class);
    }

    public static double converterMoeda(double valorOriginal, double taxaDeConversao) {
        return valorOriginal * taxaDeConversao;
    }
}