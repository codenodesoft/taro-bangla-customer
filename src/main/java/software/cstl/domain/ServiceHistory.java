package software.cstl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

import software.cstl.domain.enumeration.EmployeeType;

/**
 * A ServiceHistory.
 */
@Entity
@Table(name = "service_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServiceHistory extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_type")
    private EmployeeType employeeType;

    @Column(name = "jhi_from")
    private LocalDate from;

    @Column(name = "jhi_to")
    private LocalDate to;

    @ManyToOne
    @JsonIgnoreProperties(value = "serviceHistories", allowSetters = true)
    private Department department;

    @ManyToOne
    @JsonIgnoreProperties(value = "serviceHistories", allowSetters = true)
    private Designation designation;

    @ManyToOne
    @JsonIgnoreProperties(value = "serviceHistories", allowSetters = true)
    private Grade grade;

    @ManyToOne
    @JsonIgnoreProperties(value = "serviceHistories", allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public ServiceHistory employeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
        return this;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public LocalDate getFrom() {
        return from;
    }

    public ServiceHistory from(LocalDate from) {
        this.from = from;
        return this;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public ServiceHistory to(LocalDate to) {
        this.to = to;
        return this;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public Department getDepartment() {
        return department;
    }

    public ServiceHistory department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Designation getDesignation() {
        return designation;
    }

    public ServiceHistory designation(Designation designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public Grade getGrade() {
        return grade;
    }

    public ServiceHistory grade(Grade grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Employee getEmployee() {
        return employee;
    }

    public ServiceHistory employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceHistory)) {
            return false;
        }
        return id != null && id.equals(((ServiceHistory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceHistory{" +
            "id=" + getId() +
            ", employeeType='" + getEmployeeType() + "'" +
            ", from='" + getFrom() + "'" +
            ", to='" + getTo() + "'" +
            "}";
    }
}
