package otob.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import otob.model.entity.Bazaar;
import otob.model.entity.BazaarItem;
import otob.model.response.Response;
import otob.service.BazaarService;
import otob.util.mapper.BeanMapper;
import otob.web.model.BazaarDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/bazaars")
public class BazaarController extends GlobalController {

    @Autowired
    private BazaarService bazaarService;

    @Autowired
    private BeanMapper mapper;

    Logger logger = LoggerFactory.getLogger(BazaarController.class);

    @GetMapping
    public Response<List<BazaarDto>> getAllBazaar() {
        return toResponse(mapper.mapAsList(bazaarService.getAllBazaar(), BazaarDto.class));
    }

    @GetMapping("/active")
    public Response<List<BazaarDto>> getActiveBazaar(HttpServletRequest request) {
        return toResponse(mapper.mapAsList(bazaarService.getActiveBazaar(), BazaarDto.class));
    }

    @GetMapping("/{bazaarId}/items")
    public Response<List<BazaarItem>> getBazaarItems(@PathVariable Long bazaarId) {
        return toResponse(bazaarService.getBazaarItems(bazaarId));
    }

    @PostMapping("/create")
    public Response<BazaarDto> createBazaar(@RequestBody BazaarDto bazaarRequest) {
        Bazaar bazaar = mapper.map(bazaarRequest, Bazaar.class);

        return toResponse(mapper.map(bazaarService.createNewBazaar(bazaar), BazaarDto.class));
    }

}
