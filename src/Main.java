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
        int menu = leituraDoTeclado.nextInt();

        HttpClient client = HttpClient.newHttpClient();

        String API_KEY = "e8c2a59f795108b991f65119";
        String moeda = "USD";
        String apiConversao = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + moeda;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiConversao))
                .build();

        HttpResponse <String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}