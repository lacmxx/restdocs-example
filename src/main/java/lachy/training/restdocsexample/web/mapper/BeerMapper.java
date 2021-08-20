package lachy.training.restdocsexample.web.mapper;

import lachy.training.restdocsexample.domain.Beer;
import lachy.training.restdocsexample.web.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = DateMapper.class)
public interface BeerMapper {

    public Beer toDomain(BeerDto dto);

    public BeerDto toDto(Beer beer);

}
