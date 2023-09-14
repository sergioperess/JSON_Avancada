import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings("unchecked")
public class JsonAvanc {
    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    // Cria threads conforme o necessário
    private static ExecutorService executors = Executors.newCachedThreadPool();

    // Thread para escrita de arquivo JSON
    Runnable r1 = () -> {
        // retorna um método de escrita
        Lock writeLock = lock.writeLock();
        // Utilizado para travar
        writeLock.lock();
        // Nesse caso, como é de escrita, alteramos o valor da variavel.
        // Enquanto tem gente escrevendo ninguem consegue ler

        // Criando um arquivo
        FileWriter file = null;
        // Objeto JSON
        JSONObject objetoJSON = new JSONObject();

        objetoJSON.put("Nome", "Sérgio");
        objetoJSON.put("Sobrenome", "Reiiiiii");
        objetoJSON.put("CPF", "123.456.789-12");
        objetoJSON.put("Signo", "Touro");

        try {
            // criar o arquivo com o nome dele
            file = new FileWriter("Dados_do_Banco2.json");
            // escrever o objeto json no arquivo
            file.write(objetoJSON.toJSONString());
            file.close();
            System.out.println("Arquivo criado com sucesso");
        } catch (IOException e) {
            System.out.println("Erro ao criar o arquivo ");
            e.printStackTrace();
        }

        System.out.println(objetoJSON);

        // Utilizado para destravar
        writeLock.unlock();
    };

    // Thread para leitura de arquivo JSON
    Runnable r2 = () -> {
        // retorna um método de ler
        Lock writeLock = lock.writeLock();
        // Utilizado para travar
        writeLock.lock();

        // Já nesse caso, como é um lock de leitura, não se altera o valor da variavel
        // Equanto tem gente lendo ninguem consegue escrever
        // Aqui pode ter varias threads lendo ao mesmo tempo

        JSONObject jsonObject;

        // Conversor
        JSONParser parser = new JSONParser();

        Pessoa pessoa = new Pessoa();

        // Ler arquivos precisa de try/catch
        try {
            // Exatamente igual ao caminho do arquivo
            jsonObject = (JSONObject) parser.parse(new FileReader("Dados_do_Banco2.json"));

            // Objeto pessoa preenchido pelo objeto JSON
            pessoa.setNome((String) jsonObject.get("Nome"));
            pessoa.setSobrenome((String) jsonObject.get("Sobrenome"));
            pessoa.setSigno((String) jsonObject.get("Signo"));
            pessoa.setCpf((String) jsonObject.get("CPF"));

            System.out.println("Pessoa recuperada do JSON ");
            System.out.println(pessoa.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Utilizado para destravar
        writeLock.unlock();
    };

    // Método para escrever um arquivo JSON
    public void writeJson() {

        executors.execute(r1);
        // Utilizado para o programa não rodar para sempre
        executors.shutdown();

    }

    // Método para ler um arquivo JSON
    public void readJson() {

        executors.execute(r2);
        executors.shutdown();

    }
}
