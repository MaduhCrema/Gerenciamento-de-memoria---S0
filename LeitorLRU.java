import java.io.*;
import java.util.*;

public class LeitorLRU {
    //20:28hrs
    public static void main(String[] args) {
        try {
            File file = new File("C:\\Users\\eduar\\Desktop\\Unioeste\\Unioeste\\4-Ano-CC\\SO\\Trabalhos\\Trabalho-02\\trace2.txt");

            Scanner scan = new Scanner(file); // Leitor de arquivo
            String acesso_atual, proximo_acesso; // Variáveis para armazenar os traces da vez e poder compará-los

            // Determinar o tamanho do arquivo
            int tamanho_arquivo = 0;
            while (scan.hasNextLine()) {
                scan.nextLine();
                tamanho_arquivo++;
            }
            scan.close();

            // Inicializar o array com o tamanho do arquivo
            String[] reference_string = new String[tamanho_arquivo];
            int referenciaIndex = 0;

            // Reabrir o arquivo para leitura
            scan = new Scanner(file);
            acesso_atual = scan.nextLine(); // Armazena a primeira linha
            acesso_atual = completa_string(acesso_atual);
            acesso_atual = acesso_atual.substring(0, acesso_atual.length() - 3); // Remove deslocamento
            reference_string[referenciaIndex++] = acesso_atual; // Adiciona a string de referência

            int linhas_arquivo = 1;

            while (scan.hasNextLine()) {

                proximo_acesso = scan.nextLine(); // Lê a próxima linha
                proximo_acesso = completa_string(proximo_acesso);
                proximo_acesso = proximo_acesso.substring(0, proximo_acesso.length() - 3);

                if (!acesso_atual.equals(proximo_acesso)) { // Verifica se não acessa a mesma página do anterior
                    reference_string[referenciaIndex++] = proximo_acesso; // Se não for igual, entra para reference string
                }

                acesso_atual = proximo_acesso; // Reinicia loop
                linhas_arquivo++; //conta linhas do arquivo
            }
            scan.close();

            // Escrever os elementos da reference_string no arquivo
            File outputFile = new File("C:\\Users\\eduar\\Desktop\\Unioeste\\Unioeste\\4-Ano-CC\\SO\\Trabalhos\\Trabalho-02\\reference_string.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            for (int i = 0; i < referenciaIndex; i++) {
                writer.write(reference_string[i]);
                writer.newLine(); // Adicionar uma nova linha após cada elemento
            }
            writer.close();

            compara_log(referenciaIndex, linhas_arquivo);

            // Realizar a leitura do arquivo de referência e adicionar as próximas ocorrências
            addNextOccurrencesToReferenceFile(outputFile, reference_string, referenciaIndex);


        } catch (IOException e) {
            e.printStackTrace(); // Se ocorrer uma IOException, imprime a stack trace
        }
    }

    private static void compara_log(int tamanho_referenceString, int linhas_arquivo) {
        System.out.println("Número de entradas na reference string: " + tamanho_referenceString);
        System.out.println("Tamanho original do log: " + linhas_arquivo);

        System.out.println("O tamanho da reference string é " + ((double) tamanho_referenceString / linhas_arquivo) * 100 + " % do log original.");
    }

    private static String completa_string(String acesso_atual) {

        if (!(acesso_atual.length() == 8)) { //verifica se a string já tem o tamanho necessario

            int zerosParaAdicionar = 8 - acesso_atual.length(); //conta quantos zeros são necessários
            StringBuilder zeros = new StringBuilder();
            for (int i = 0; i < zerosParaAdicionar; i++) {
                zeros.append("0"); //armazena o número de zeros necessarios
            }

            acesso_atual = zeros + acesso_atual; //junta a string original aos zeros
        }

        return acesso_atual;
    }

    private static void addNextOccurrencesToReferenceFile(File inputFile, String[] referenceString, int size) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile)); // Leitor de arquivo
            // Inicializar o BufferedWriter
            File outputFile = new File("C:\\Users\\eduar\\Desktop\\Unioeste\\Unioeste\\4-Ano-CC\\SO\\Trabalhos\\Trabalho-02\\reference_string_with_occurrences-LRU.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            int lineNumber = 1; // Número da linha atual

            // Criar um novo arquivo para armazenar as referências com as próximas ocorrências            
            if (!outputFile.exists()) {
                outputFile.createNewFile(); // Cria o arquivo se não existir
            }

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove espaços em branco
                int nextOccurrence = fileNextOcc(inputFile.getPath(), line, lineNumber, referenceString, size);
                writer.write(line + ", " + nextOccurrence); // Escreve a página seguida pelo próximo número de linha
                writer.newLine(); // Adicionar uma nova linha após cada elemento
                lineNumber++;
            }
            reader.close();

            // Fechar o BufferedWriter após terminar de escrever
            writer.close();

        } catch (IOException e) {
            e.printStackTrace(); // Se ocorrer uma IOException, imprime a stack trace
        }
    }

    private static int fileNextOcc(String filePath, String page, int currentLine, String[] referenceString, int size) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String[] entracys = new String[size]; // Array para armazenar as entradas
            String line;
            int index = 0; // Índice para o array
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                entracys[index++] = line; // Adiciona a linha ao array e incrementa o índice
            }
            reader.close();

            // Iterar de currentLine - 1 até 0
            for (int i = currentLine - 2; i >= 0; i--) {
                if (i < size && entracys[i].equals(page)) {
                    int indice = i + 1; // Incrementar o índice para ajustar a indexação do usuário
                    return indice;
                }
            }
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
