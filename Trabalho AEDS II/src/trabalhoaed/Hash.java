package trabalhoaed;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Arthur
 */

// Classe para representar uma palavra chave e suas ocorrências
class Palavra {

    String palavraChave;
    ArrayList<Ocorrencia> ocorrencias;

    public Palavra(String palavraChave) {
        this.palavraChave = palavraChave;
        this.ocorrencias = new ArrayList<>();
    }
}

// Classe para representar a ocorrência de uma palavra chave em um documento .txt
class Ocorrencia {

    String nomeArquivo;
    int numeroOcorrencias;

    public Ocorrencia(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        this.numeroOcorrencias = 0;
    }
}

// Classe da Hash Table
class Hash {

    private LinkedList<Palavra>[] table;
    private int size;

    public Hash(int size) {
        this.size = size;
        this.table = new LinkedList[size];

        // Inicializa as listas encadeadas em cada posição da tabela
        for (int i = 0; i < size; i++) {
            this.table[i] = new LinkedList<>();
        }
    }

    // Função da Hash Table
    private int hash(String palavra) {
        int hash = 0;
        for (int i = 0; i < palavra.length(); i++) {
            hash = (hash + palavra.charAt(i) * (i + 1)) % size;
        }
        return hash;
    }

    // Insere uma palavra-chave e sua ocorrência na tabela
    public void insert(String palavraChave, String nomeArquivo) {
        int index = hash(palavraChave);

        // Aqui é verificado se a palavra chave já existe na Hash Table
        for (Palavra palavra : table[index]) {
            if (palavra.palavraChave.equals(palavraChave)) {
                // Aqui é verificado se o documento já possui uma ocorrência da palavra chave
                for (Ocorrencia ocorrencia : palavra.ocorrencias) {
                    if (ocorrencia.nomeArquivo.equals(nomeArquivo)) {
                        ocorrencia.numeroOcorrencias++; // Incrementa o contador de ocorrências
                        return;
                    }
                }
                // Se não exisitr a ocorrência do documento, cria uma nova ocorrência com um cont iniciado em 1 
                palavra.ocorrencias.add(new Ocorrencia(nomeArquivo));
                palavra.ocorrencias.get(palavra.ocorrencias.size() - 1).numeroOcorrencias++;
                return;
            }
        }

        // A palavra-chave não existe na tabela, então a adiciona com uma nova ocorrência com contador inicializado como 1
        Palavra novaPalavra = new Palavra(palavraChave);
        novaPalavra.ocorrencias.add(new Ocorrencia(nomeArquivo));
        novaPalavra.ocorrencias.get(0).numeroOcorrencias++;
        table[index].add(novaPalavra);
    }

    // Aqui é feita a busca de uma palavra chave na Hash Table e retorna as ocorrências
    public ArrayList<Ocorrencia> search(String palavraChave) {
        int index = hash(palavraChave);

        for (Palavra palavra : table[index]) {
            if (palavra.palavraChave.equals(palavraChave)) {
                return palavra.ocorrencias;
            }
        }
        // Aqui retorna uma lista vazia se não encontrar a palavra chave 
        return new ArrayList<>(); 
    }
}
