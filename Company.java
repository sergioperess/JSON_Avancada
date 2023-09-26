import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Company {
    // Quantas threads podem passar pelo semaforo ao mesmo tempo
    private static final Semaphore SEMAFORO = new Semaphore(1);

    // Threads em cache, vai criando as Threads conforme o necessario
    private ExecutorService executor = Executors.newCachedThreadPool();

    // Rotas disponiveis para executar
    private ArrayList<String> rotasDisp = new ArrayList();
    // Rotas que estão sendo executadas
    private ArrayList<String> rotasExe = new ArrayList();
    // Rotas executadas
    private ArrayList<String> rotasTer = new ArrayList();

    // Rotas a serem executadas
    private void routeQueue(String rota) {
        Runnable r1 = () -> {

            aquire();

            rotasDisp.add(rota);

            // Ja foi feito a tarefa e pode dar o espaço para outro que deseja fazer
            SEMAFORO.release();
        };

        executor.execute(r1);
        executor.shutdown();
    }

    // Rotas que estçao sendo executadas
    private void routeExecuting(String rota) {
        Runnable r2 = () -> {
            aquire();

            rotasExe.add(rota);

            // Ja foi feito a tarefa e pode dar o espaço para outro que deseja fazer
            SEMAFORO.release();
        };

        executor.execute(r2);
        executor.shutdown();
    }

    // Rotas que ja foram executadas
    private void routeExecuted(String rota) {
        Runnable r3 = () -> {
            aquire();

            rotasTer.add(rota);

            // Ja foi feito a tarefa e pode dar o espaço para outro que deseja fazer
            SEMAFORO.release();
        };

        executor.execute(r3);
        executor.shutdown();
    }

    // Método para chamar o semáforo
    private static void aquire() {
        try {
            // Ao chamar o mértodo acquire, ele vai tentar passar pelo semáforo e desse modo
            // será permitido passar o numero desejado
            SEMAFORO.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
