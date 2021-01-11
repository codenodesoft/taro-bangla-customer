package software.cstl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.cstl.domain.Weekend;
import software.cstl.domain.enumeration.WeekendStatus;
import software.cstl.repository.WeekendRepository;
import software.cstl.service.dto.WeekendDateMapDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Weekend}.
 */
@Service
@Transactional
public class WeekendService {

    private final Logger log = LoggerFactory.getLogger(WeekendService.class);

    private final WeekendRepository weekendRepository;

    private final CommonService commonService;

    public WeekendService(WeekendRepository weekendRepository, CommonService commonService) {
        this.weekendRepository = weekendRepository;
        this.commonService = commonService;
    }

    /**
     * Save a weekend.
     *
     * @param weekend the entity to save.
     * @return the persisted entity.
     */
    public Weekend save(Weekend weekend) {
        log.debug("Request to save Weekend : {}", weekend);
        return weekendRepository.save(weekend);
    }

    /**
     * Get all the weekends.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Weekend> findAll(Pageable pageable) {
        log.debug("Request to get all Weekends");
        return weekendRepository.findAll(pageable);
    }


    /**
     * Get one weekend by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Weekend> findOne(Long id) {
        log.debug("Request to get Weekend : {}", id);
        return weekendRepository.findById(id);
    }

    /**
     * Delete the weekend by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Weekend : {}", id);
        weekendRepository.deleteById(id);
    }

    /**
     * Get active weekend.
     *
     * @return the entity.
     */
    public List<Weekend> getActiveWeekends() {
        log.debug("Request to get all active Weekends");
        return weekendRepository.findAll().stream().filter(weekend -> weekend.getStatus().equals(WeekendStatus.ACTIVE)).collect(Collectors.toList());
    }

    /**
     * Get list of WeekendDateMapDTO.
     *
     * @param fromDate the start date.
     * @param toDate the end date.
     * @return the WeekendDateMapDTO DTOs.
     */
    public List<WeekendDateMapDTO> getWeekendDateMapDTOs(LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to get WeekendDateMapDTO : {} {}", fromDate, toDate);

        List<Weekend> weekends = getActiveWeekends();
        List<WeekendDateMapDTO> weekendDateMapDTOS = new ArrayList<>();

        LocalDate startDate = fromDate;
        LocalDate endDate = toDate.plusDays(1);

        while(startDate.isBefore(endDate)) {
            DayOfWeek dayOfWeek = startDate.getDayOfWeek();
            String day = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH).trim().toUpperCase();
            for(Weekend weekend: weekends) {
                if (day.equalsIgnoreCase(weekend.getDay().toString().trim().toUpperCase())) {
                    WeekendDateMapDTO weekendDateMapDTO = new WeekendDateMapDTO();
                    weekendDateMapDTO.setWeekendDate(startDate);
                    weekendDateMapDTO.setWeekendId(weekend.getId());
                    weekendDateMapDTOS.add(weekendDateMapDTO);
                }
            }
            startDate = startDate.plusDays(1);
        }

        return weekendDateMapDTOS;
    }

    /**
     * Get list of WeekendDateMapDTO.
     *
     * @param year the year.
     * @param month the month.
     * @return the WeekendDateMapDTO DTOs.
     */
    public List<WeekendDateMapDTO> getWeekendDateMapDTOs(int year, Month month) {
        log.debug("Request to get WeekendDateMapDTO : {} {}", year, month);
        LocalDate startDateOfTheMonth = commonService.getFirstDayOfTheMonth(year, month);
        LocalDate lastDateOfTheMonth = commonService.getLastDayOfTheMonth(year, month);
        return getWeekendDateMapDTOs(startDateOfTheMonth, lastDateOfTheMonth);
    }

    /**
     * Get list of WeekendDateMapDTO.
     *
     * @param year the year.
     * @return the WeekendDateMapDTO DTOs.
     */
    public List<WeekendDateMapDTO> getWeekendDateMapDTOs(int year) {
        log.debug("Request to get WeekendDateMapDTO : {}", year);
        LocalDate startDateOfTheYear = commonService.getFirstDayOfTheYear(year);
        LocalDate lastDateOfTheYear = commonService.getLastDayOfTheYear(year);
        return getWeekendDateMapDTOs(startDateOfTheYear, lastDateOfTheYear);
    }

    /**
     * Get count of WeekendDateMapDTO.
     *
     * @param year the year.
     * @param month the month.
     * @return the count.
     */
    public int getTotalNumberOfWeekends(int year, Month month) {
        log.debug("Request to get count of total weekends : {} {}", year, month);
        return getWeekendDateMapDTOs(year, month).size();
    }

    /**
     * Get count of WeekendDateMapDTO.
     *
     * @param year the year.
     * @return the count.
     */
    public int getTotalNumberOfWeekends(int year) {
        log.debug("Request to get count of total weekends : {}", year);
        return getWeekendDateMapDTOs(year).size();
    }
}
