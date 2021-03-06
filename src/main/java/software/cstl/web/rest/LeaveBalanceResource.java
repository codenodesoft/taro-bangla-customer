package software.cstl.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.cstl.service.LeaveBalanceService;
import software.cstl.service.dto.LeaveBalanceDTO;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LeaveBalanceResource {

    private final Logger log = LoggerFactory.getLogger(LeaveBalanceResource.class);

    private final LeaveBalanceService leaveBalanceService;

    public LeaveBalanceResource(LeaveBalanceService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }

    @GetMapping("/leave-balances/employee/{employeeId}/year/{year}")
    public ResponseEntity<List<LeaveBalanceDTO>> getLeaveBalance(@PathVariable Long employeeId, @PathVariable int year) {
        log.debug("REST request to get LeaveBalance : {} {}", employeeId, year);
        return ResponseEntity.ok().body(leaveBalanceService.getLeaveBalances(employeeId, year));
    }
}
