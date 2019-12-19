package otob.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.model.entity.Bazaar;
import otob.model.response.Response;
import otob.service.BazaarService;
import otob.util.mapper.BeanMapper;
import otob.web.model.BazaarDto;

import java.util.List;

@RestController
@RequestMapping("/api/bazaars")
public class BazaarController extends GlobalController {

    @Autowired
    private BazaarService bazaarService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping
    public Response<List<BazaarDto>> getActiveBazaar() {
        return toResponse(mapper.mapAsList(bazaarService.getActiveBazaar(), BazaarDto.class));
    }

    @PostMapping("/create")
    public Response<BazaarDto> createBazaar(@RequestBody BazaarDto bazaarRequest) {
        Bazaar bazaar = mapper.map(bazaarRequest, Bazaar.class);

        return toResponse(mapper.map(bazaarService.createNewBazaar(bazaar), BazaarDto.class));
    }

}
