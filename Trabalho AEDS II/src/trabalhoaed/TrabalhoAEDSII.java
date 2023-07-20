package trabalhoaed;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * @author Arthur
 */
public class TrabalhoAEDSII {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String pastaArquivos = null;
        boolean caminhoValido = false;

        // Aqui é solicitado ao usuário o caminho da pasta que ele quer realizar a busca
        //Exemplo utilizado: C:\Users\arthu\OneDrive\Área de Trabalho\TESTE AEDS
        while (!caminhoValido) {
            System.out.print("Informe o caminho da pasta dos arquivos: ");
            pastaArquivos = scanner.nextLine();

            // Aqui é feita a verificação se o caminho informado pelo usuário é válido
            File pasta = new File(pastaArquivos);
            if (pasta.exists() && pasta.isDirectory()) {
                caminhoValido = true;
            } else {
                System.out.println("Caminho inválido. Por favor, insira um caminho válido.");
            }
        }

        // Criar uma instância da classe HashTable
        Hash hashTable = new Hash(100); // Aqui é definido o tamanho adequado para a tabela Hash, utilizei o tamanho 100. 

        // Aqui são indexados os arquivos na pasta 
        indexarArquivos(hashTable, pastaArquivos);

        // Aqui é solicitado ao usuário a palavra chave que ele deseja buscar 
        System.out.print("Informe a palavra-chave a ser buscada: ");
        String palavraChave = scanner.nextLine();
        palavraChave = palavraChave.toLowerCase(); 
        // Aqui é feita a busca 
        ArrayList<Ocorrencia> ocorrencias = hashTable.search(palavraChave);

        // Aqui é onde as ocorrências são ordenadas em quantidade decrescente 
        Collections.sort(ocorrencias, new Comparator<Ocorrencia>() {
            public int compare(Ocorrencia o1, Ocorrencia o2) {
                return Integer.compare(o2.numeroOcorrencias, o1.numeroOcorrencias);
            }
        });

        // Aqui os resultados são exibidos 
        exibirResultados(ocorrencias);

        scanner.close();
    }
    // Este método indexarArquivos tem a função de indexar os arquivos na pasta 

    private static void indexarArquivos(Hash hashTable, String pastaArquivos) {
        File pasta = new File(pastaArquivos);
        File[] arquivos = pasta.listFiles();

        if (arquivos != null) {
            for (File arquivo : arquivos) {
                if (arquivo.isFile()) {
                    try {
                        Scanner scanner = new Scanner(arquivo);
                        while (scanner.hasNext()) {
                            String palavra = scanner.next().toLowerCase(); // Aqui é feita a conversão para minúsculas para evitar diferenciação entre maiúsculas e minúsculas
                            if (!isStopWord(palavra)) { // Aqui é feita a verificação de se a palavra não é uma stop word
                                hashTable.insert(palavra, arquivo.getName());
                            }
                        }
                        scanner.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("Erro ao ler o arquivo: " + arquivo.getName());
                    }
                }
            }
        } else {
            System.out.println("Pasta inválida ou vazia.");
        }
    }

    private static boolean isStopWord(String palavra) {
        // Lista de stop words utilizadas como filtro 
        String[] stopWords = {"a", "as", "o", "os", "em", "de", "para", "com", "um", "uma", "uns", "umas", "no", "na", "nos", "do",
            "da", "das", "dos"};

        for (String stopWord : stopWords) {
            if (palavra.equals(stopWord)) {
                return true;
            }
        }
        return false;
    }

    // O método exibirResultados verifica se a palavra chave foi encontrada, se foi, ele exibe os resultados encontrados
    private static void exibirResultados(ArrayList<Ocorrencia> ocorrencias) {
        if (ocorrencias.isEmpty()) {
            System.out.println("A palavra-chave não foi encontrada em nenhum arquivo.");
        } else {
            System.out.println("Resultados da busca:");
            for (Ocorrencia ocorrencia : ocorrencias) {
                System.out.println("Arquivo: " + ocorrencia.nomeArquivo);
                System.out.println("Número de ocorrências: " + ocorrencia.numeroOcorrencias);
                System.out.println();
            }
        }
    }
}
