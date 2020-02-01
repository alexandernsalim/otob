package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otob.model.entity.Bazaar;
import otob.model.entity.BazaarItem;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.repository.BazaarRepository;
import otob.service.BazaarService;
import otob.util.generator.IdGenerator;

import java.util.Date;
import java.util.List;

@Service
public class BazaarServiceImpl implements BazaarService {

    @Autowired
    private BazaarRepository bazaarRepository;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public List<Bazaar> getAllBazaar() {
        return bazaarRepository.findAll();
    }

    @Override
    public List<Bazaar> getActiveBazaar() {
        return bazaarRepository.findAllByBazaarEndDateAfter(new Date());
    }

    @Override
    public List<BazaarItem> getBazaarItems(Long bazaarId) {
        Date today = new Date();
        Bazaar bazaar = bazaarRepository.findByBazaarId(bazaarId);

        if(bazaar.getBazaarEndDate().after(today)) {
            return bazaar.getBazaarProducts();
        } else {
            throw new CustomException(
                ErrorCode.BAZAAR_ENDED.getCode(),
                ErrorCode.BAZAAR_ENDED.getMessage()
            );
        }
    }

    @Override
    public Bazaar createNewBazaar(Bazaar bazaar) {
        try {
            bazaar.setBazaarId(idGenerator.getNextId("bazaarid"));

            return bazaarRepository.save(bazaar);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(
                ErrorCode.GENERATE_ID_FAIL.getCode(),
                ErrorCode.GENERATE_ID_FAIL.getMessage()
            );
        }
    }

}
