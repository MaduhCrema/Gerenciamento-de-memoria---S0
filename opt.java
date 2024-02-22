import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class opt {

    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("/home/isacoelho/Downloads/testopt.txt");

        Scanner scan = new Scanner(file);
        int tamanho_frame = 3;

        List<String> reference_string = new ArrayList<>();
        ArrayList<Integer> ocorrencias = new ArrayList<>();

        while (scan.hasNextLine()) {
            String linha = scan.nextLine();
            String[] partes = linha.split(",");
            if (partes.length >= 2) {
                reference_string.add(partes[0].trim());
                ocorrencias.add(Integer.parseInt(partes[1].trim()));
            }
        }
        
        int[] proxima_ocorrencia = ocorrencias.stream().mapToInt(Integer::intValue).toArray();
		scan.close();

        inicializa_frame(reference_string, proxima_ocorrencia, tamanho_frame);
    }

    private static void inicializa_frame(List<String> reference_string, int[] proxima_ocorrencia, int tamanho_frame) {
        int falta_pagina = 0;
        List<String> frame = new ArrayList<>(tamanho_frame);
        List<Integer> ocorrencias = new ArrayList<>(tamanho_frame);
        String acesso_atual;
        int frame_cheio = 0, temporaria, i = 0;

        while(frame_cheio != tamanho_frame){
            acesso_atual = reference_string.get(i);
            if (!frame.contains(acesso_atual)) {
                System.out.println(acesso_atual);
                falta_pagina++;
                frame.add(acesso_atual);
                ocorrencias.add(proxima_ocorrencia[i]);
                frame_cheio++;
            } else {
                temporaria = frame.indexOf(acesso_atual);
                ocorrencias.set(temporaria, proxima_ocorrencia[i]);
            }
            i++;
        }

        while(i < reference_string.size()){
            
            acesso_atual = reference_string.get(i);
            if (!frame.contains(acesso_atual)) {
                falta_pagina++;
                substituir_pagina(frame, ocorrencias, acesso_atual, proxima_ocorrencia[i]);
            } else {
                temporaria = frame.indexOf(acesso_atual);
                ocorrencias.set(temporaria, proxima_ocorrencia[i]);
            }
            i++;
        }

            System.out.println("falha de pagina:" + falta_pagina);  
    }
    
    private static void substituir_pagina(List<String> frame, List<Integer> ocorrencias, String acesso_atual, int substituto) {
        int index_substituir = -1;
        int maior_distancia = -1;
        
        for (int i = 0; i < frame.size(); i++) { 
            if (ocorrencias.get(i) > maior_distancia) { //encontra a maior distancia
                maior_distancia = ocorrencias.get(i); 
                index_substituir = i; //salva seu index
            }
        }

            frame.set(index_substituir, acesso_atual);
            ocorrencias.set(index_substituir, substituto);
    }

}

