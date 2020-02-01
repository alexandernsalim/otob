package otob.service;

import otob.model.entity.Bazaar;
import otob.model.entity.BazaarItem;

import java.util.List;

public interface BazaarService {

    List<Bazaar> getAllBazaar();
    List<Bazaar> getActiveBazaar();
    List<BazaarItem> getBazaarItems(Long bazaarId);

    Bazaar createNewBazaar(Bazaar bazaarRequest);

}
