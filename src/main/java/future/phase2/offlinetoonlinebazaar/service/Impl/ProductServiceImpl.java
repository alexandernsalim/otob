package future.phase2.offlinetoonlinebazaar.service.Impl;

import future.phase2.offlinetoonlinebazaar.exception.ResourceNotFoundException;
import future.phase2.offlinetoonlinebazaar.helper.ErrorCodeGenerator;
import future.phase2.offlinetoonlinebazaar.helper.IdGenerator;
import future.phase2.offlinetoonlinebazaar.model.dto.ProductDto;
import future.phase2.offlinetoonlinebazaar.model.entity.Product;
import future.phase2.offlinetoonlinebazaar.repository.ProductRepository;
import future.phase2.offlinetoonlinebazaar.service.ProductService;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IdGenerator idGenerator;

    MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

     
}
