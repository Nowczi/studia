package pl.zajavka.api.controller.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.CarPurchaseDTO;
import pl.zajavka.api.dto.CarsToBuyDTO;
import pl.zajavka.api.dto.InvoiceDTO;
import pl.zajavka.api.dto.mapper.CarPurchaseMapper;
import pl.zajavka.api.dto.mapper.InvoiceMapper;
import pl.zajavka.business.CarPurchaseService;
import pl.zajavka.domain.CarPurchaseRequest;
import pl.zajavka.domain.Invoice;

@RestController
@AllArgsConstructor
@RequestMapping(PurchaseRestController.API_PURCHASE)
public class PurchaseRestController {

    public static final String API_PURCHASE = "/api/purchase";

    private final CarPurchaseService carPurchaseService;
    private final CarPurchaseMapper carPurchaseMapper;
    private final InvoiceMapper invoiceMapper;

    @GetMapping
    public CarsToBuyDTO carsPurchaseData(){
        return  CarsToBuyDTO.builder()
                .carsToBuy(carPurchaseService.availableCars().stream()
                        .map(carPurchaseMapper::map)
                        .toList())
                .build();

    }

    @PostMapping
    public InvoiceDTO makePurchase(
            @Valid @RequestBody CarPurchaseDTO carPurchaseDTO
            ){
        CarPurchaseRequest request = carPurchaseMapper.map(carPurchaseDTO);
        Invoice invoice = carPurchaseService.purchase(request);

        return invoiceMapper.map(invoice);

    }
}
