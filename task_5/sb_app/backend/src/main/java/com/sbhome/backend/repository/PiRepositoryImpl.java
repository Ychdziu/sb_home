package com.sbhome.backend.repository;

import com.sbhome.backend.exception.BusinessValidationException;
import com.sbhome.backend.util.OracleErrorUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

@Repository
public class PiRepositoryImpl implements PiRepository {

    private final SimpleJdbcCall jdbcCall;

    public PiRepositoryImpl(DataSource dataSource) {
        this.jdbcCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("SB_CORE")
                .withFunctionName("GET_PI_VALUE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter("RETURN_VALUE", Types.NUMERIC),
                        new SqlParameter("PI_PRECISION", Types.NUMERIC)
                );
    }

    @Override
    public BigDecimal getPiValue(int precision) {
        SqlParameterSource in = new MapSqlParameterSource().addValue("PI_PRECISION", precision);
        try {
            Map<String, Object> result = jdbcCall.execute(in);
            return (BigDecimal) result.get("RETURN_VALUE");
        } catch (DataAccessException ex) {
            if (ex.getCause() instanceof SQLException sqlEx && sqlEx.getErrorCode() == 20000) {
                throw new BusinessValidationException(
                        OracleErrorUtils.extractCustomMessage(sqlEx.getMessage(), "ORA-20000:"));
            }
            throw ex;
        }
    }
}