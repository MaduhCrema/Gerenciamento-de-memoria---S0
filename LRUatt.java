import java.io.*;
import java.util.*;

public class LRUatt {

    public static void main(String[] args) {
        try {
            // Lendo o arquivo de referência LRU
            File inputFile = new File("C:\\Users\\gabxl\\OneDrive\\Área de Trabalho\\Desktop\\Conteúdos\\SO\\Trab 2\\LRU\\referencia2-LRU.txt");
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
        Map<String, Integer> pageAccessTime = new HashMap<>(); // Mapa para armazenar o tempo de acesso de cada página
        int pageFaults = 0;
    
        String line;
        LinkedList<String> cache = new LinkedList<>(); // Cache para armazenar os frames
        LinkedList<Integer> cacheIndices = new LinkedList<>(); // Cache para armazenar os índices dos frames
    
        int time = 0; // Contador de tempo de acesso
    
        while ((line = reader.readLine()) != null) {
            String page = line.trim(); // Remover espaços em branco no início e no final da linha
    
            // Atualiza o tempo de acesso da página
            pageAccessTime.put(page, time++);
    
            if (!cache.contains(page)) { // Página não está na cache
                pageFaults++;
                if (cache.size() == numFrames) { // Cache está cheia, remove a página menos recentemente usada
                    String leastRecentPage = findLRUPage(cache, pageAccessTime);
                    int leastRecentIndex = cacheIndices.remove(cache.indexOf(leastRecentPage)); // Remove o índice da página menos recente
                    cache.remove(leastRecentPage);
    
                    // Adiciona a nova página na posição do frame que foi removido
                    cache.add(leastRecentIndex, page);
                    cacheIndices.add(leastRecentIndex, leastRecentIndex); // Atualiza os índices dos frames
                } else { // Se a cache não está cheia, apenas adicione a nova página
                    cache.addLast(page);
                    cacheIndices.addLast(cache.size() - 1); // Adiciona o índice do frame
                }
            } else { // Se a página já está na cache, remove-a para reordená-la
                int pageIndex = cache.indexOf(page);
                cache.remove(page);
                cacheIndices.remove(pageIndex);
    
                // Adiciona a página de volta na mesma posição
                cache.add(pageIndex, page);
                cacheIndices.add(pageIndex, pageIndex); // Atualiza os índices dos frames
            }
    
            // Imprime os frames após cada iteração
           // System.out.println("Frames após a iteração: " + cache);
        }
    
        // Retornando o número total de falhas de página
        return pageFaults;
    }
    
    // Método para encontrar a página menos recentemente usada
    private static String findLRUPage(LinkedList<String> cache, Map<String, Integer> pageAccessTime) {
        int minTime = Integer.MAX_VALUE;
        String lruPage = null;
        for (String page : cache) {
            int time = pageAccessTime.get(page);
            if (time < minTime) {
                minTime = time;
                lruPage = page;
            }
        }
        return lruPage;
    }
}
