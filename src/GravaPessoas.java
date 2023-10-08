import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GravaPessoas {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, List<String>> siglaToLinhas = new HashMap<>();
        
        // Lê o arquivo UF.CSV e cria mapeamentos entre siglas e linhas completas
        try (BufferedReader reader = new BufferedReader(new FileReader("UF.CSV"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String sigla = parts[1];
                    
                    // Adiciona a linha completa ao mapeamento
                    if (siglaToLinhas.containsKey(sigla)) {
                        siglaToLinhas.get(sigla).add(line);
                    } else {
                        List<String> linhas = new ArrayList<>();
                        linhas.add(line);
                        siglaToLinhas.put(sigla, linhas);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream("pessoas.bin"))) {
            while (true) {
                System.out.print("Digite o nome (ou 'sair' para encerrar): ");
                String nome = scanner.nextLine();
                if (nome.equalsIgnoreCase("sair")) {
                    break;
                }

                System.out.print("Digite a idade: ");
                int idade = Integer.parseInt(scanner.nextLine());

                String sigla;
                String codigo;
                
                do {
                    System.out.print("Digite a sigla do estado: ");
                    sigla = scanner.nextLine().toUpperCase();
                    
                    // Verifica se a sigla tem múltiplas correspondências
                    if (siglaToLinhas.containsKey(sigla) && siglaToLinhas.get(sigla).size() > 1) {
                        System.out.println("A sigla possui múltiplas correspondências. Escolha uma opção:");
                        List<String> linhas = siglaToLinhas.get(sigla);
                        for (int i = 0; i < linhas.size(); i++) {
                            System.out.println((i + 1) + ": " + linhas.get(i));
                        }
                        System.out.print("Opção: ");
                        int opcao = Integer.parseInt(scanner.nextLine());
                        
                        if (opcao >= 1 && opcao <= linhas.size()) {
                            String linhaEscolhida = linhas.get(opcao - 1);
                            String[] parts = linhaEscolhida.split(",");
                            codigo = parts[0];
                        } else {
                            System.out.println("Opção inválida. Tente novamente.");
                            codigo = null;
                        }
                    } else {
                        String linhaUnica = siglaToLinhas.get(sigla).get(0);
                        String[] parts = linhaUnica.split(",");
                        codigo = parts[0];
                    }
                    
                    if (codigo == null) {
                        System.out.println("Sigla não encontrada. Digite o código do país: ");
                        codigo = scanner.nextLine();
                    }
                } while (codigo == null);

                System.out.print("Digite o salário: ");
                double salario = Double.parseDouble(scanner.nextLine());

                // Gravar os dados no arquivo binário no formato "nome, idade, código, estado, salario"
                String dados = nome + ", " + idade + ", " + codigo + ", " + sigla + ", " + salario + "\n";
                outputStream.writeUTF(dados);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Exibir a mensagem "Registros em pessoas.bin"
        System.out.println("Registros em pessoas.bin");

        // Fechar o scanner após a conclusão do programa
        scanner.close();
    }
}
