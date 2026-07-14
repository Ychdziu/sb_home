package com.sbhome.backend.repository;

import com.sbhome.backend.dto.InvoiceResponse;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final SimpleJdbcCall jdbcCall;

    public InvoiceRepositoryImpl(DataSource dataSource) {
        this.jdbcCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("SB_CORE")
                .withFunctionName("GET_UNPAID_INVOICES")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter("RETURN_VALUE", OracleTypes.CURSOR, new InvoiceRowMapper())
                );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InvoiceResponse> getUnpaidInvoices() {
        Map<String, Object> result = jdbcCall.execute();
        return (List<InvoiceResponse>) result.get("RETURN_VALUE");
    }

    private static class InvoiceRowMapper implements RowMapper<InvoiceResponse> {
        @Override
        public InvoiceResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new InvoiceResponse(
                    rs.getLong("INVOICE_ID"),
                    rs.getDate("INVOICE_DATE").toLocalDate()
            );
        }
    }
}