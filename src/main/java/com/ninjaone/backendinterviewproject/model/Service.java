package com.ninjaone.backendinterviewproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode
@ToString
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"service_type_id", "device_id"})})
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name="service_type_id", referencedColumnName = "id", nullable = false)
    private ServiceType type;

    @ManyToOne()
    @JoinColumn(name="device_id", referencedColumnName = "id", nullable = false)
    private Device device;

    public Service() {
    }

    public Service(ServiceType type) {
        this(null, type);
    }

    public Service(Long id, ServiceType type) {
        this.id = id;
        this.type = type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
