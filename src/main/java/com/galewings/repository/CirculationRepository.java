package com.galewings.repository;

import com.galewings.dto.CirculationDto;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class CirculationRepository {

    /**
     * SqlManager
     */
    @Autowired
    SqlManager sqlManager;

    public List<CirculationDto> selectForId(String id) {
        CirculationDto param = new CirculationDto();
        param.id = id;
        return sqlManager.getResultList(CirculationDto.class, new ClasspathSqlResource("sql/circulation/selectForId.sql"),
                param);
    }

    public List<CirculationDto> selectAll() {
        return sqlManager.getResultList(CirculationDto.class,
                new ClasspathSqlResource("sql/circulation/selectAll.sql"));
    }

    public int insert(CirculationDto circulationDto) {
        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/circulation/insert_circulation.sql"),
                circulationDto);
    }

    public int update(CirculationDto circulationDto) {
        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/circulation/update_circulation_uuid.sql"),
                circulationDto);
    }

    public int delete(String id) {
        CirculationDto param = new CirculationDto();
        param.id = id;
        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/circulation/delete_circulation_uuid.sql"),
                param);
    }
}
