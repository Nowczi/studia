package pl.zajavka.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.With;

@With
@Value
@Builder
@EqualsAndHashCode(of = "serviceMechanicId")
@ToString(of = {"serviceMechanicId", "hours", "comment"})
public class ServiceMechanic {

    Integer serviceMechanicId;
    Integer hours;
    String comment;
    CarServiceRequest carServiceRequest;
    Mechanic mechanic;
    Service service;

}
