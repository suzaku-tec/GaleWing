package com.galewings.repository;

import com.galewings.entity.FunctionCtrl;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Transactional
public class FunctionCtrlRepository {

    @Autowired
    private SqlManager sqlManager;

    public enum FunctionCtrlFlg {
        ON("1"), OFF("0");

        private final String code;

        FunctionCtrlFlg(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public List<FunctionCtrl> getAll() {
        return sqlManager.getResultList(FunctionCtrl.class, new ClasspathSqlResource("sql/functionCtrl/select_all.sql"));
    }

    public void update(String id, String flg) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("flg", flg);
        sqlManager.executeUpdate(new ClasspathSqlResource("sql/functionCtrl/update_one.sql"), params);
    }

    public FunctionCtrl get(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return sqlManager.getSingleResult(FunctionCtrl.class
                , new ClasspathSqlResource("sql/functionCtrl/select_one.sql")
                , params);
    }

    public String getFlg(String id) {
        FunctionCtrl functionCtrl = get(id);
        return Objects.isNull(functionCtrl) ? FunctionCtrlFlg.OFF.getCode() : functionCtrl.flg;
    }

}
