package lachy.training.restdocsexample.web.controller;

import lachy.training.restdocsexample.domain.Beer;
import lachy.training.restdocsexample.repository.BeerRepository;
import lachy.training.restdocsexample.web.mapper.BeerMapper;
import lachy.training.restdocsexample.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

    private final BeerMapper beerMapper;
    private final BeerRepository beerRepository;

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId){
        return ResponseEntity.ok(beerMapper.toDto(beerRepository.findById(beerId).get()));
    }

    @PostMapping
    public ResponseEntity save(@Validated @RequestBody BeerDto beerDto){
        beerRepository.save(beerMapper.toDomain(beerDto));
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/{beerId}")
    public ResponseEntity update(@PathVariable UUID beerId, @Validated @RequestBody BeerDto beerDto){
        beerRepository.findById(beerId).ifPresent(beer -> {
            beer.setName(beerDto.getName());
            beer.setStyle(beerDto.getStyle().name());
            beer.setPrice(beerDto.getPrice());
            beer.setUpc(beerDto.getUpc());

            beerRepository.save(beer);
        });

        return ResponseEntity.noContent().build();
    }

}
