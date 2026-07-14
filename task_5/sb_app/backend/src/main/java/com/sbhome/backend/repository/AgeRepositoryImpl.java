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
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

@Repository
public class AgeRepositoryImpl implements AgeRepository {

    private final SimpleJdbcCall jdbcCall;

    public AgeRepositoryImpl(DataSource dataSource) {
        this.jdbcCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("SB_CORE")
                .withFunctionName("GET_AGE_TEXT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter("RETURN_VALUE", Types.VARCHAR),
                        new SqlParameter("PN_AGE", Types.NUMERIC)
                );
    }

    @Override
    public String getAgeText(int age) {
        SqlParameterSource in = new MapSqlParameterSource().addValue("PN_AGE", age);
        try {
            Map<String, Object> result = jdbcCall.execute(in);
            return (String) result.get("RETURN_VALUE");
        } catch (DataAccessException ex) {
			if (ex.getCause() instanceof SQLException sqlEx && sqlEx.getErrorCode() == 20000) {
				throw new BusinessValidationException(
					OracleErrorUtils.extractCustomMessage(sqlEx.getMessage(), "ORA-20000:"));
			}
			throw ex;
		}
		
	}
}