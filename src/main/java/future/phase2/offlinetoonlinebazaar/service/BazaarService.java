package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.entity.Bazaar;
import future.phase2.offlinetoonlinebazaar.model.dto.BazaarDto;

public interface BazaarService {
    BazaarDto createBazaar(Bazaar bazaar);
    BazaarDto updateBazaar(Bazaar bazaar);
}
