import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.cdimascio.dotenv.Dotenv;

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

        Scanner leituraDoTeclado = new Scanner(System.in);
        int opcao;

        do {
            System.out.println(menu);
            opcao = leituraDoTeclado.nextInt();

            System.out.println("Digite o valor que deseja converter: ");
            double valor = leituraDoTeclado.nextDouble();

            String codigoMoedaOrigem = "";
            String codigoMoedaDestino = switch (opcao) {
                case 1 -> {
                    codigoMoedaOrigem = "USD";
                    yield "BRL";
                }
                case 2 -> {
                    codigoMoedaOrigem = "BRL";
                    yield "USD";
                }
                case 3 -> {
                    codigoMoedaOrigem = "EUR";
                    yield "BRL";
                }
                case 4 -> {
                    codigoMoedaOrigem = "BRL";
                    yield "EUR";
                }
                case 5 -> {
                    codigoMoedaOrigem = "JPY";
                    yield "BRL";
                }
                case 6 -> {
                    codigoMoedaOrigem = "BRL";
                    yield "JPY";
                }
                default -> "";
            };

            try{
                ExchangeRateApi exchangeRate = requisicaoParaApiTaxaDeCambio(codigoMoedaOrigem, codigoMoedaDestino);
                double taxaDeConversao = exchangeRate.conversionRate();
                double valorConvertido = converterMoeda(valor, taxaDeConversao);

                System.out.printf("$ %.2f (%s) =  $ %.2f (%s)%n", valor, codigoMoedaOrigem, valorConvertido, codigoMoedaDestino);
            } catch (Exception e) {
                System.out.println("Algo deu errado");
            }

        } while (opcao != 7);

    }

    private static ExchangeRateApi requisicaoParaApiTaxaDeCambio(String codigoMoedaOrigem, String codigoMoedaDestino) throws IOException, InterruptedException {
        Dotenv dotenv = Dotenv.load();
        String API_KEY = dotenv.get("API_KEY");
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