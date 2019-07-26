package otob.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BeanMapper {

    private static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
    private static MapperFacade mapper = mapperFactory.getMapperFacade();

    public static <S,C> C map(S source, Class<C> clazz){
        return mapper.map(source, clazz);
    }

    public static <S,C> List mapAsList(Iterable source, Class clazz){
        return mapper.mapAsList(source, clazz);
    }
}
