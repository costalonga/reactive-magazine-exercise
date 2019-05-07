import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class MagazineApplication {

    private static final Logger log =
            Logger.getLogger(MagazineApplication.class.getName());

    private static final int NUMBER_OF_MAGAZINES = 20;
    private static final long MAX_SECONDS_TO_KEEP_IT_WHEN_NO_SPACE = 2;
    public static final String JACK = "Jack";
    public static final String PETER = "Peter";

    public static void main(String[] args) throws Exception {
        final MagazineApplication app = new MagazineApplication();

        log.info("\n\n### CASE 1: Assinantes são rápidos e, portanto, o " +
                "tamanho do buffer não é tão importante nesse caso");
        app.magazineDeliveryExample(100L, 100L, 8);

        log.info("\n\n### CASE 2: Um assinante lento mas um bom tamanho de buffer para garantir a entrega " +
                "de todas as revistas.");
        app.magazineDeliveryExample(1000L, 3000L, NUMBER_OF_MAGAZINES);

        log.info("\n\n### CASE 3: Um assinante lento e um buffer limitado");
        app.magazineDeliveryExample(1000L, 3000L, 8);

    }

    /**
     *
     * @param sleepTimeJack tempo de descanso para o assinante Jack
     * @param sleepTimePeter tempo de descanso para o assinante Peter
     * @param maxStorageInPO tamanho da caixa postal para cada assinante
     * @throws Exception exceção em caso de erro
     */
    void magazineDeliveryExample(final long sleepTimeJack,
                                 final long sleepTimePeter,
                                 final int maxStorageInPO) throws Exception {

        // Cria um PublisherPadrão com o tamanho máximo de buffer dos seus items para cada assinante.
        final SubmissionPublisher<Integer> publisher =
                new SubmissionPublisher<>(ForkJoinPool.commonPool(), maxStorageInPO);

        // Cria o assinante JACK
        final MagazineSubscriber jack = new MagazineSubscriber(
                sleepTimeJack,
                MagazineApplication.JACK
        );

        // Cria o assinante PETER
        final MagazineSubscriber peter = new MagazineSubscriber(
                sleepTimePeter,
                MagazineApplication.PETER
        );

        // Faz a assinatura do JACK
        publisher.subscribe(jack);

        // Faz a assinatura do PETER
        publisher.subscribe(peter);

        // Produz as 20 revistas
        log.info("Produzindo 20 revistas por assinante com uma capacidade de acumular  "
                + maxStorageInPO + " para cada um deles. Eles possuem no máximo " + MAX_SECONDS_TO_KEEP_IT_WHEN_NO_SPACE +
                " segundos para consumir as revistas quando não houver mais espaço.");
        IntStream.rangeClosed(1, 20).forEach((number) -> {
            log.info("Oferecendo a revista " + number + " para os assinantes");
            final int lag = publisher.offer(
                    number,
                    MAX_SECONDS_TO_KEEP_IT_WHEN_NO_SPACE,
                    TimeUnit.SECONDS,
                    (subscriber, msg) -> {
                        subscriber.onError(
                                new RuntimeException("Olá " + ((MagazineSubscriber) subscriber)
                                        .getSubscriberName() + "! Você está muito lento na aquisição de novas revistas" +
                                        " e não temos mais espaço para elas! " +
                                        "Precisaremos descartar a revista: " + msg));
                        return false; // don't retry, we don't believe in second opportunities
                    });
            if (lag < 0) {
                log("Descartando " + -lag + " revistas");
            } else {
                log("O assinante mais lento possui " + lag +
                        " revistas em atraso para serem entregues");
            }
        });

        // Bloqueia até que todos os assinantes estejam prontos.
        while (publisher.estimateMaximumLag() > 0) {
            Thread.sleep(500L);
        }

        // Finaliza a produção de revistas.
        publisher.close();

        // Dá um tempo extra para que o assinante mais lento acorde e perceba que terminou
        // a entrega de todas as revistas,
        Thread.sleep(Math.max(sleepTimeJack, sleepTimePeter));
    }

    private static void log(final String message) {
        log.info("===========> " + message);
    }

}
