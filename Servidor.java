import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor extends Thread {
    private static ArrayList<BufferedWriter> clientes;
    private static ServerSocket server;
    private String nome;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr; // Atributo para ler a mensagem
    public int PORT; // numero da porta(está como publico para podermos utilizar no chat cliente)

    // Construtor para o server socket
    // Recebe um objeto socket como parametro
    // Cria um objeto do tipo BufferedReader
    public Servidor(Socket con) {
        this.con = con;
        try {
            in = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para alocar os clientes em uma Thread
    public void run() {

        try {

            String msg;
            OutputStream ou = this.con.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw); // Atributo para gravar a mmensagem
            clientes.add(bfw); // Adiciona a mensagem ao Array
            nome = msg = bfr.readLine(); // Lê uma linha de texto

            while (!"Sair".equalsIgnoreCase(msg) && msg != null) {
                msg = bfr.readLine();
                sendToAll(bfw, msg);
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método utilizado para mandar as mensagem para todos os clientes
    public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException {
        BufferedWriter bwS;

        // Percorre a lista de clientes e manda uma cópia da mensagem
        for (BufferedWriter bw : clientes) {
            bwS = (BufferedWriter) bw;
            if (!(bwSaida == bwS)) {
                bw.write(nome + " -> " + msg + "\r\n"); // Grava os caracteres
                bw.flush(); // Libera o fluxo
            }
        }
    }

    // Método para instanciar o serverSocket (Inicia o servidor - socket)
    public static void start(int PORT) throws IOException {
        server = new ServerSocket(PORT); // Inicia o servidor com o numero da porta
    }

    public static void main(String[] args) {

        try {
            // Cria os objetos necessário para instânciar o servidor
            start(12345);
            clientes = new ArrayList<BufferedWriter>();

            while (true) {
                System.out.println("Aguardando conexão...");
                Socket con = server.accept();
                System.out.println("Cliente conectado...");
                Thread t = new Servidor(con);
                t.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
