import java.io.*;
import java.util.*;

public class LRU {

    public static void main(String[] args) {
        try {
            // Lendo o arquivo de referência LRU
            File inputFile = new File("C:\\Users\\gabxl\\OneDrive\\Área de Trabalho\\Desktop\\Conteúdos\\SO\\Trab 2\\LRU\\teste.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));

            // Definindo o número de frames livres para cada caso
            int[] frames = {4, 8, 16, 32};

            // Executando o algoritmo LRU para cada número de frames
            for (int numFrames : frames) {
                int pageFaults = executeLRU(reader, numFrames);
                System.out.println("Número de falhas com " + numFrames + " frames: " + pageFaults);
                // Reiniciando o leitor para ler novamente o arquivo para o próximo teste
                reader.close();
                reader = new BufferedReader(new FileReader(inputFile));
            }

            // Fechando o leitor após a conclusão de todos os testes
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int executeLRU(BufferedReader reader, int numFrames) throws IOException {
        Map<String, Integer> pageIndices = new HashMap<>(); // Mapa para armazenar o índice de cada página na cache
        int pageFaults = 0;

        String line;
        LinkedList<String> cache = new LinkedList<>(); // Cache para armazenar os frames

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(", ");
            String page = parts[0];
            int pageIndex = Integer.parseInt(parts[1]);

            if (!cache.contains(page)) { // Página não está na cache
                pageFaults++;
                if (cache.size() == numFrames) { // Cache está cheia, remove a página menos recentemente usada
                    String leastRecentPage = cache.remove(); // Remove o primeiro elemento da lista, que é o menos recentemente usado
                    pageIndices.remove(leastRecentPage);
                }
            } else { // Se a página já está na cache, remove-a para reordená-la
                cache.remove(page);
            }
            cache.addLast(page); // Adiciona a página ao final da lista, indicando que ela foi a mais recentemente usada

            pageIndices.put(page, pageIndex);
        }

        // Retornando o número total de falhas de página
        return pageFaults;
    }
}
