package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.pet.PetSize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetTransportPricingService {

    public PetTransportPricingResult calculate(PetTransport transport, List<PetSize> petsToTransport, Money routeCost) {
        FieldValidator.requiresNonNull("transport", transport);
        FieldValidator.requiresNonNull("petsToTransport", petsToTransport);
        FieldValidator.requiresNonNull("routeCost", routeCost);

        var totalSlotsNeeded = petsToTransport.stream()
                .mapToInt(this::calculateRequiredSlots)
                .sum();

        var vehicleSlots = transport.capacity().value();
        var totalTrips = (int) Math.ceil((double) totalSlotsNeeded / vehicleSlots);
        var totaltransportCost = routeCost.multiply(new Quantity(totalTrips));

        return new PetTransportPricingResult(totalTrips, totaltransportCost);
    }

    private int calculateRequiredSlots(PetSize petSize) {
        return switch (petSize) {
            case SMALL -> 1;
            case MEDIUM -> 2;
            case LARGE -> 3;
        };
    }
}
