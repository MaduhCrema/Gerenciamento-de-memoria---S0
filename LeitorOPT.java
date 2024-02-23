import java.io.*;
import java.util.*;

public class LeitorOPT {

    public static void main(String[] args) {
        try {
            File file = new File("C:\\Users\\eduar\\Desktop\\Unioeste\\Unioeste\\4-Ano-CC\\SO\\Trabalhos\\Trabalho-02\\trace1.txt");

            Scanner scan = new Scanner(file); // Leitor de arquivo
            String acesso_atual, proximo_acesso; // Variáveis para armazenar os traces da vez e poder compará-los

            List<String> reference_string = new ArrayList<>(); // Lista que vai gerar string de referência

            acesso_atual = scan.nextLine(); // Armazena a primeira linha
            acesso_atual = completa_string(acesso_atual);
            acesso_atual = acesso_atual.substring(0, acesso_atual.length() - 3); // Remove deslocamento
            reference_string.add(acesso_atual); // Adiciona a string de referência

            int linhas_arquivo = 1;

            // Criar um novo arquivo para armazenar a reference_string
            File outputFile = new File("C:\\Users\\eduar\\Desktop\\Unioeste\\Unioeste\\4-Ano-CC\\SO\\Trabalhos\\Trabalho-02\\reference_string.txt");
            if (!outputFile.exists()) {
                outputFile.createNewFile(); // Cria o arquivo se não existir
            }

            // Inicializar o BufferedWriter
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            while (scan.hasNextLine()) {

                proximo_acesso = scan.nextLine(); // Lê a próxima linha
                proximo_acesso = completa_string(proximo_acesso);
                proximo_acesso = proximo_acesso.substring(0, proximo_acesso.length() - 3);

                if (!acesso_atual.equals(proximo_acesso)) { // Verifica se não acessa a mesma página do anterior
                    reference_string.add(proximo_acesso); // Se não for igual, entra para reference string
                }

                acesso_atual = proximo_acesso; // Reinicia loop
                linhas_arquivo++; //conta linhas do arquivo
            }
            scan.close();

            // Escrever os elementos da reference_string no arquivo
            for (String str : reference_string) {
                writer.write(str);
                writer.newLine(); // Adicionar uma nova linha após cada elemento
            }

            // Fechar o BufferedWriter após terminar de escrever
            writer.close();
            compara_log(reference_string.size(), linhas_arquivo);

            // Realizar a leitura do arquivo de referência e adicionar as próximas ocorrências
            addNextOccurrencesToReferenceFile(outputFile, reference_string);


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
    private static void addNextOccurrencesToReferenceFile(File inputFile, List<String> referenceString) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile)); // Leitor de arquivo
           
            // Inicializar o BufferedWriter
            File outputFile = new File("C:\\Users\\eduar\\Desktop\\Unioeste\\Unioeste\\4-Ano-CC\\SO\\Trabalhos\\Trabalho-02\\reference_string_with_occurrences-OPT.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            int lineNumber = 1; // Número da linha atual

            // Criar um novo arquivo para armazenar as referências com as próximas ocorrências            
            if (!outputFile.exists()) {
                outputFile.createNewFile(); // Cria o arquivo se não existir
            }        

            String line;
            while ((line = reader.readLine()) != null) {
                //System.out.println(lineNumber);
                line = line.trim(); // Remove espaços em branco
                int nextOccurrence = fileNextOcc(inputFile.getPath(), line, lineNumber);
                //System.out.println("i " + nextOccurrence);
                //System.out.println("line " + line);                    
                
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

    private static int fileNextOcc(String filePath, String page, int currentLine) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            List<String> entracys = new ArrayList<>();
            //System.out.println("current line " + currentLine);
            //System.out.println("page " + page);
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                entracys.add(line);
                
            }
            //System.out.println(entracys);
            reader.close();

            int entracyslen = entracys.size();
            for(int i = currentLine; i < entracyslen; i++){
                //System.out.println("entracys " + entracys.get(i));
                if(entracys.get(i).equals(page)){
                    int indice = i + 1;
                    //System.out.println("teve ocorrencia: " + indice );
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
