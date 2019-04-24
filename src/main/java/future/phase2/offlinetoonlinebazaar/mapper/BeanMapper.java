package future.phase2.offlinetoonlinebazaar.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;

@Component
public class BeanMapper {

    private static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private static MapperFacade mapper = mapperFactory.getMapperFacade();

}
