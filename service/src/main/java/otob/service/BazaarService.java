package otob.service;

import otob.model.entity.Bazaar;

import java.util.List;

public interface BazaarService {

    List<Bazaar> getAllBazaar();
    List<Bazaar> getActiveBazaar();
    Bazaar getBazaarItems(Long bazaarId);

    Bazaar createNewBazaar(Bazaar bazaarRequest);

}
