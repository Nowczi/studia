package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.business.dao.SalesmanDAO;
import pl.zajavka.domain.Salesman;
import pl.zajavka.domain.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class SalesmanService {

    private final SalesmanDAO salesmanDAO;

    @Transactional
    public List<Salesman> findAvailable() {
        List<Salesman> availableSalesmen = salesmanDAO.findAvailable();
        log.info("Available salesmen: [{}]", availableSalesmen.size());
        return availableSalesmen;
    }

    @Transactional
    public Salesman findSalesman(String pesel) {
        Optional<Salesman> salesman = salesmanDAO.findByPesel(pesel);
        if (salesman.isEmpty()) {
            throw new NotFoundException("Could not find salesman by pesel: [%s]".formatted(pesel));
        }
        return salesman.get();
    }
}
