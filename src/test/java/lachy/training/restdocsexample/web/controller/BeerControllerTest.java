package lachy.training.restdocsexample.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lachy.training.restdocsexample.domain.Beer;
import lachy.training.restdocsexample.repository.BeerRepository;
import lachy.training.restdocsexample.web.model.BeerDto;
import lachy.training.restdocsexample.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
// Se debe eliminar este cuando se utiliza RestDocs
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs(uriPort = 8081)
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "lachy.training.restdocsexample.web.mapper")
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerRepository beerRepository;

    private static final String endPoint = "/api/v1/beer/";

    @Test
    void getBeerById() throws Exception {
        given(beerRepository.findById(any())).willReturn(Optional.of(Beer.builder().build()));

        mockMvc.perform(
                get("/api/v1/beer/{beerId}", UUID.randomUUID().toString())
                        .param("isCold", "yes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        document("v1/beer-get",
                                pathParameters(
                                        parameterWithName("beerId").description("UUID of beet to get")
                                ),
                                requestParameters(
                                        parameterWithName("isCold").description("Is Beer Cold Query param")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("Beer's identifier"),
                                        fieldWithPath("version").description("Version number"),
                                        fieldWithPath("name").description("Beer name"),
                                        fieldWithPath("style").description("Beer Style"),
                                        fieldWithPath("upc").description("UPC of Beer"),
                                        fieldWithPath("price").description("Price"),
                                        fieldWithPath("quantityOnHand").description("Quantity on HAnd"),
                                        fieldWithPath("createdDate").description("Date created"),
                                        fieldWithPath("lastModifiedDate").description("Date Updated")
                                )
                        )
                );
    }

    @Test
    void saveNewBeerTest() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        mockMvc.perform( post(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson)
        ).andExpect(status().isCreated())
                .andDo(document(
                        "v1/beer-new",
                        requestFields(
                                fields.withPath("id").description("Beer's identifier").ignored(),
                                fields.withPath("name").description("Beer name"),
                                fields.withPath("style").description("Beer style"),
                                fields.withPath("version").description("Version number").ignored(),
                                fields.withPath("upc").description("UPC of Beer").attributes(),
                                fields.withPath("price").description("Price"),
                                fields.withPath("quantityOnHand").description("Quantity on Hand").ignored(),
                                fields.withPath("createdDate").description("Date created").ignored(),
                                fields.withPath("lastModifiedDate").description("Date modified").ignored()
                        )
                ));
    }

    @Test
    void updateTest() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String beerToJson = objectMapper.writeValueAsString(beerDto);
        mockMvc.perform( put(String.format("%s%s", endPoint, UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerToJson)
        ).andExpect(status().isNoContent());
    }

    private BeerDto getValidBeerDto(){
        return BeerDto.builder()
                .name("Nice Ale")
                .style(BeerStyleEnum.ALE)
                .price(BigDecimal.valueOf(9.99))
                .upc(123123123123L)
                .build();

    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }
}