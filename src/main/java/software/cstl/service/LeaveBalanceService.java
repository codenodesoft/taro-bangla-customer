package software.cstl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.cstl.domain.Employee;
import software.cstl.domain.LeaveApplication;
import software.cstl.domain.LeaveType;
import software.cstl.domain.enumeration.EmployeeStatus;
import software.cstl.domain.enumeration.LeaveApplicationStatus;
import software.cstl.domain.enumeration.LeaveTypeName;
import software.cstl.service.dto.LeaveBalanceDTO;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Transactional
public class LeaveBalanceService {

    private final Logger log = LoggerFactory.getLogger(LeaveBalanceService.class);

    private final EmployeeService employeeService;

    private final LeaveApplicationService leaveApplicationService;

    private final LeaveTypeService leaveTypeService;

    private final WeekendService weekendService;

    private final WeekendDateMapService weekendDateMapService;

    private final HolidayService holidayService;

    public LeaveBalanceService(EmployeeService employeeService, LeaveApplicationService leaveApplicationService, LeaveTypeService leaveTypeService, WeekendService weekendService, WeekendDateMapService weekendDateMapService, HolidayService holidayService) {
        this.employeeService = employeeService;
        this.leaveApplicationService = leaveApplicationService;
        this.leaveTypeService = leaveTypeService;
        this.weekendService = weekendService;
        this.holidayService = holidayService;
        this.weekendDateMapService = weekendDateMapService;
    }

    public List<LeaveBalanceDTO> getLeaveBalances(Long employeeId) {

        List<LeaveBalanceDTO> leaveBalanceDTOs = new ArrayList<>();
        Optional<Employee> employee = employeeService.findOne(employeeId);

        if (employee.isPresent() && employee.get().getStatus().equals(EmployeeStatus.ACTIVE)) {
            List<LeaveType> leaveTypes = leaveTypeService.getAll();

            for (LeaveType leaveType : leaveTypes) {
                LeaveBalanceDTO leaveBalanceDTO = getLeaveBalance(employeeId, leaveType.getId());
                leaveBalanceDTOs.add(leaveBalanceDTO);
            }
        }

        return leaveBalanceDTOs;
    }

    public LeaveBalanceDTO getLeaveBalance(Long employeeId, Long leaveTypeId) {

        Optional<Employee> employee = employeeService.findOne(employeeId);
        Optional<LeaveType> leaveType = leaveTypeService.findOne(leaveTypeId);

        LeaveBalanceDTO leaveBalanceDTO = new LeaveBalanceDTO();

        if (employee.isPresent() && employee.get().getStatus().equals(EmployeeStatus.ACTIVE) && leaveType.isPresent()) {

            LocalDate startDate;
            LocalDate endDate;

            if(leaveType.get().getName().equals(LeaveTypeName.EARNED_LEAVE)) {
                double remainingEarnedLeave = 0;
                List<LeaveApplication> acceptedLeaveApplications = new ArrayList<>();
                int numberOfYearPassedAfterJoining = getNumberOfYearsPassedAfterJoining(employee.get().getId());
                for(int i = 1; i <= numberOfYearPassedAfterJoining; i++) {
                    startDate = employee.get().getJoiningDate().plusYears(i);
                    endDate = startDate.plusYears(1).minusDays(1);
                    double totalDays = DAYS.between(startDate, endDate);
                    double numberOfWeekends = weekendDateMapService.getWeekendDateMapDTOs(startDate, endDate).size();
                    double numberOfHolidays = holidayService.getHolidayDateMapDTOs(startDate, endDate).size();
                    acceptedLeaveApplications = leaveApplicationService.getLeaveApplications(employee.get(), leaveType.get(), startDate, endDate, LeaveApplicationStatus.ACCEPTED);
                    double numberOfAcceptedLeave = acceptedLeaveApplications.size();
                    remainingEarnedLeave = remainingEarnedLeave + ((totalDays - numberOfWeekends - numberOfHolidays - numberOfAcceptedLeave) / 18);
                }
                double numberOfTotalAcceptedLeave = leaveApplicationService.getLeaveApplicationDetailDateMapDto(acceptedLeaveApplications).size();
                leaveBalanceDTO = getLeaveBalanceDTO(employee.get(), leaveType.get(), remainingEarnedLeave, numberOfTotalAcceptedLeave, acceptedLeaveApplications);
            }
            else {
                LocalDate localDate = LocalDate.now();
                startDate = LocalDate.of(localDate.getYear(), Month.JANUARY, 1);
                endDate = LocalDate.of(localDate.getYear(), Month.DECEMBER, 31);
                List<LeaveApplication> acceptedLeaveApplications = leaveApplicationService.getLeaveApplications(employee.get(), leaveType.get(), startDate, endDate, LeaveApplicationStatus.ACCEPTED);
                double numberOfTotalAcceptedLeave = leaveApplicationService.getLeaveApplicationDetailDateMapDto(acceptedLeaveApplications).size();
                leaveBalanceDTO = getLeaveBalanceDTO(employee.get(), leaveType.get(), numberOfTotalAcceptedLeave, acceptedLeaveApplications);
            }
        }

        return leaveBalanceDTO;
    }

    private LeaveBalanceDTO getLeaveBalanceDTO(Employee employee, LeaveType leaveType, double numberOfTotalAcceptedLeave, List<LeaveApplication> acceptedLeaveApplications) {
        LeaveBalanceDTO leaveBalanceDTO = new LeaveBalanceDTO();
        leaveBalanceDTO.setId(leaveType.getId());
        leaveBalanceDTO.setEmployeeId(employee.getId());
        leaveBalanceDTO.setEmployeeName(employee.getName());
        leaveBalanceDTO.setEmployeeJoiningDate(employee.getJoiningDate());
        leaveBalanceDTO.setLeaveTypeId(leaveType.getId());
        leaveBalanceDTO.setLeaveTypeName(leaveType.getName().name());
        leaveBalanceDTO.setTotalDays(leaveType.getTotalDays());
        leaveBalanceDTO.setRemainingDays(leaveType.getTotalDays() - numberOfTotalAcceptedLeave);
        leaveBalanceDTO.setAcceptedLeaveApplications(acceptedLeaveApplications);
        return leaveBalanceDTO;
    }

    private LeaveBalanceDTO getLeaveBalanceDTO(Employee employee, LeaveType leaveType, double totalDays, double numberOfTotalAcceptedLeave, List<LeaveApplication> acceptedLeaveApplications) {
        LeaveBalanceDTO leaveBalanceDTO = new LeaveBalanceDTO();
        leaveBalanceDTO.setId(leaveType.getId());
        leaveBalanceDTO.setEmployeeId(employee.getId());
        leaveBalanceDTO.setEmployeeName(employee.getName());
        leaveBalanceDTO.setEmployeeJoiningDate(employee.getJoiningDate());
        leaveBalanceDTO.setLeaveTypeId(leaveType.getId());
        leaveBalanceDTO.setLeaveTypeName(leaveType.getName().name());
        leaveBalanceDTO.setTotalDays(totalDays > 40 ? 40 : totalDays);
        leaveBalanceDTO.setRemainingDays(leaveBalanceDTO.getTotalDays() - numberOfTotalAcceptedLeave);
        leaveBalanceDTO.setAcceptedLeaveApplications(acceptedLeaveApplications);
        return leaveBalanceDTO;
    }

    private int getNumberOfYearsPassedAfterJoining(Long employeeId) {

        Optional<Employee> employee = employeeService.findOne(employeeId);

        if(employee.isPresent()) {

            LocalDate today = LocalDate.now();

            LocalDate startDate = employee.get().getJoiningDate();
            LocalDate endDate = startDate.plusYears(1).minusDays(1);

            int totalYear = 0;

            while (!(today.isAfter(startDate) && today.isBefore(endDate))) {
                totalYear = totalYear + 1;
                startDate = startDate.plusYears(1);
                endDate = startDate.plusYears(1);
            }

            return totalYear;
        }

        return -1;
    }
}
