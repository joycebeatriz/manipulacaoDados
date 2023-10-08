import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsultaEstado {
    public static void main(String[] args) {
        // Mapeamento de regiões
        Map<String, String> regioes = new HashMap<>();
        regioes.put("1", "NORTE");
        regioes.put("2", "NORDESTE");
        regioes.put("3", "SUDESTE");
        regioes.put("4", "SUL");
        regioes.put("5", "CENTRO OESTE");

        // Solicitar a sigla do estado ao usuário
        System.out.print("Digite a sigla do estado: ");
        try (BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(System.in))) {
            String sigla = reader.readLine().toUpperCase();

            // Pesquisar a sigla no arquivo UF.csv
            List<String[]> resultados = pesquisarEstado(sigla);

            // Exibir os resultados
            if (resultados.isEmpty()) {
                System.out.println("Estado não encontrado.");
            } else {
                for (String[] estado : resultados) {
                    System.out.println(estado[2] + ", " + regioes.get(estado[3]) + ", " + estado[4]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Função para pesquisar a sigla no arquivo UF.csv
    private static List<String[]> pesquisarEstado(String sigla) {
        List<String[]> encontrados = new ArrayList<>();

        try (BufferedReader csvReader = new BufferedReader(new FileReader("UF.csv"))) {
            String linha;
            while ((linha = csvReader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 5 && dados[1].equalsIgnoreCase(sigla)) {
                    encontrados.add(dados);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encontrados;
    }
}
