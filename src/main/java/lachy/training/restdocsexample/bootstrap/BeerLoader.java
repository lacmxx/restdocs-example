package lachy.training.restdocsexample.bootstrap;

import lachy.training.restdocsexample.domain.Beer;
import lachy.training.restdocsexample.repository.BeerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BeerLoader implements CommandLineRunner {

    private final BeerRepository beerRepository;

    public BeerLoader(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadBeerObjects();
    }

    private void loadBeerObjects() {
        if ( beerRepository.count() == 0 ){
            beerRepository.save(Beer.builder()
                .name("Mango Bobs")
                .style("IPA")
                .quantityToBrew(200)
                .price(BigDecimal.valueOf(12.95))
                .minOnHand(12)
                .upc(337010000L)
                .build());
            beerRepository.save(Beer.builder()
                    .name("Galaxy Cat")
                    .style("PALE_ALE")
                    .quantityToBrew(200)
                    .price(BigDecimal.valueOf(11.32))
                    .minOnHand(12)
                    .upc(337023005L)
                    .build());
        }

    }
}
