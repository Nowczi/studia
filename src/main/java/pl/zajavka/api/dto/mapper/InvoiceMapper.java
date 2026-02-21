package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.zajavka.api.dto.CarHistoryDTO;
import pl.zajavka.api.dto.CarToBuyDTO;
import pl.zajavka.api.dto.CarToServiceDTO;
import pl.zajavka.api.dto.InvoiceDTO;
import pl.zajavka.domain.CarHistory;
import pl.zajavka.domain.CarToBuy;
import pl.zajavka.domain.CarToService;
import pl.zajavka.domain.Invoice;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper extends OffsetDateTimeMapper {


    InvoiceDTO map(Invoice invoice);
}
