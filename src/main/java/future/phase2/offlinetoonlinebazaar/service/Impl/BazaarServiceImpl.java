package future.phase2.offlinetoonlinebazaar.service.Impl;

import future.phase2.offlinetoonlinebazaar.exception.ResourceNotFoundException;
import future.phase2.offlinetoonlinebazaar.helper.ErrorCodeGenerator;
import future.phase2.offlinetoonlinebazaar.helper.IdGenerator;
import future.phase2.offlinetoonlinebazaar.model.entity.Bazaar;
import future.phase2.offlinetoonlinebazaar.model.dto.BazaarDto;
import future.phase2.offlinetoonlinebazaar.repository.BazaarRepository;
import future.phase2.offlinetoonlinebazaar.service.BazaarService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BazaarServiceImpl implements BazaarService {
    @Autowired
    private BazaarRepository bazaarRepository;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public BazaarDto createBazaar(Bazaar bazaar) {
        try{
            bazaar.setBazaarId(idGenerator.getNextId("bazaarid"));
            bazaar.setActiveStatus(false);
            bazaarRepository.save(bazaar);

            BazaarDto bazaarDto = modelMapper.map(bazaar, BazaarDto.class);

            return bazaarDto;
        }catch(Exception e){
            throw new ResourceNotFoundException(
                    ErrorCodeGenerator.NOT_FOUND.getCode(),
                    ErrorCodeGenerator.NOT_FOUND.getMessage()
            );
        }
    }

    @Override
    public BazaarDto updateBazaar(Bazaar bazaar) {
        return null;
    }
}
