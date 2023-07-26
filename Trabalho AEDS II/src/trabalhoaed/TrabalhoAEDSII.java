package trabalhoaed;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class TrabalhoAEDSII {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String pastaArquivos = null;
        boolean caminhoValido = false;

        // Aqui é solicitado ao usuário o caminho da pasta que ele quer realizar a busca
        // Exemplo utilizado: C:\Users\arthu\OneDrive\Área de Trabalho\TESTE AEDS
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
        Hash hashTable = new Hash(701); // Tamanho mínimo da tabela Hash definido como 701

        // Aqui são indexados os arquivos na pasta
        long inicioIndexacao = System.currentTimeMillis();
        indexarArquivos(hashTable, pastaArquivos);
        long fimIndexacao = System.currentTimeMillis();

        // Calcula o tempo de indexação
        long tempoIndexacao = fimIndexacao - inicioIndexacao;

        // Aqui é solicitado ao usuário a palavra chave que ele deseja buscar
        System.out.print("Informe a palavra-chave a ser buscada: ");
        String palavraChave = scanner.nextLine();
        palavraChave = palavraChave.toLowerCase();

        // Aqui é feita a busca na Hash Table
        long inicioBuscaHash = System.currentTimeMillis();
        ArrayList<Ocorrencia> ocorrencias = hashTable.search(palavraChave);
        long fimBuscaHash = System.currentTimeMillis();

        // Aqui é calculado o tempo de busca na Hash Table 
        long buscaHashTempo = fimBuscaHash - inicioBuscaHash;

        // Ordena as ocorrências em quantidade decrescente
        Collections.sort(ocorrencias, new Comparator<Ocorrencia>() {
            public int compare(Ocorrencia o1, Ocorrencia o2) {
                return Integer.compare(o2.numeroOcorrencias, o1.numeroOcorrencias);
            }
        });

        // Aqui são exibidos os resultados da busca na Hash Table
        exibirResultados(ocorrencias);
        System.out.println("O tempo de busca na Tabela Hash é de: " + buscaHashTempo + " milissegundos");
        System.out.println("O tempo de indexação na Tabela Hash é de: " + tempoIndexacao + " milissegundos");

        // Calculando e exibindo o fator de carga da tabela Hash
        double fatorCarga = hashTable.fatorDeCarga();
        System.out.println("O Fator de Carga da tabela Hash é de: " + fatorCarga);

        // Aqui são realizados os experimentos 
        System.out.println("\nExperimentos realizados:");
        String[] palavrasExperimento = {"casa", "comida", "pobreza", "cultura", "economia", "política", "natureza", "sociedade"}; // Aqui foram informadas as palavras a serem buscadas

        // Iniciar o cronômetro para medir o tempo total de busca das 8 palavras-chave
        long inicioBuscas = System.currentTimeMillis();

        for (String palavra : palavrasExperimento) {
            System.out.println("\nBuscando a palavra-chave: " + palavra);

            // Aqui é feita a busca utilizando a Hash Table
            inicioBuscaHash = System.currentTimeMillis();
            ocorrencias = hashTable.search(palavra);
            fimBuscaHash = System.currentTimeMillis();
            buscaHashTempo = fimBuscaHash - inicioBuscaHash;

            // Aqui são exibidos os resultados da busca Hash
            exibirResultados(ocorrencias);
            System.out.println("Tempo de busca na tabela Hash: " + buscaHashTempo + " milissegundos");

            // Aqui é realizada a busca utilizando o buscador sequencial simples
            long inicioBuscaSequencial = System.currentTimeMillis();
            int ocorrenciasSequencial = buscaSequencial(pastaArquivos, palavra);
            long fimBuscaSequencial = System.currentTimeMillis();
            long buscaSequencialTempo = fimBuscaSequencial - inicioBuscaSequencial;

            // Aqui são exibidos os resultados da busca sequencial 
            System.out.println("O número de ocorrências encontradas é de: " + ocorrenciasSequencial);
            System.out.println("O tempo da busca sequencial é de: " + buscaSequencialTempo + " milissegundos");
        }

        // Aqui é o tempo total de todas as buscas
        long fimBuscas = System.currentTimeMillis();
        long tempoTotalBuscas = fimBuscas - inicioBuscas;
        System.out.println("\nO tempo total de todas as buscas é de: " + tempoTotalBuscas + " milissegundos");

        scanner.close();
    }

    private static void indexarArquivos(Hash hashTable, String pastaArquivos) {
        File pasta = new File(pastaArquivos);
        File[] arquivos = pasta.listFiles();

        if (arquivos != null) {
            for (File arquivo : arquivos) {
                if (arquivo.isFile()) {
                    try {
                        Scanner scanner = new Scanner(arquivo);
                        while (scanner.hasNext()) {
                            String palavra = scanner.next().toLowerCase();
                            if (!isStopWord(palavra)) {
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

    private static int buscaSequencial(String pastaArquivos, String palavraChave) {
        int cont = 0;
        File pasta = new File(pastaArquivos);
        File[] arquivos = pasta.listFiles();

        if (arquivos != null) {
            // Aqui é iniciado o timer antes de começar a busca sequencial
            long startTime = System.currentTimeMillis();

            for (File arquivo : arquivos) {
                if (arquivo.isFile()) {
                    try {
                        Scanner scanner = new Scanner(arquivo);
                        while (scanner.hasNext()) {
                            String palavra = scanner.next().toLowerCase();
                            if (palavra.equals(palavraChave)) {
                                cont++;
                            }
                        }
                        scanner.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("Erro ao ler o arquivo: " + arquivo.getName());
                    }
                }
            }

            // Aqui é encerrado o timer após a busca sequencial
            long endTime = System.currentTimeMillis();
            //long buscaSequencialTempo = endTime - startTime;
            //System.out.println("O tempo da busca sequencial é de: " + buscaSequencialTempo + " milissegundos"); 
            //Estas duas funções acabam sendo meio irrelevantes, pois já está declarado lá em cima na main.
        } else {
            System.out.println("Pasta inválida ou vazia.");
        }

        return cont;
    }
}