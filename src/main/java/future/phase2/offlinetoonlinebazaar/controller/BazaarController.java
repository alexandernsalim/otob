package future.phase2.offlinetoonlinebazaar.controller;

import future.phase2.offlinetoonlinebazaar.helper.ResponseGenerator;
import future.phase2.offlinetoonlinebazaar.model.dto.BazaarDto;
import future.phase2.offlinetoonlinebazaar.model.entity.Bazaar;
import future.phase2.offlinetoonlinebazaar.service.BazaarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bazaar")
public class BazaarController extends GlobalController{

    @Autowired
    private BazaarService bazaarService;

    @PostMapping
    public ResponseGenerator<BazaarDto> createBazaar(@RequestBody Bazaar bazaar){
        return toResponse(bazaarService.createBazaar(bazaar));
    }

}
