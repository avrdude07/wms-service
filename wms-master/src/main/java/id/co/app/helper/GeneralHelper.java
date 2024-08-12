package id.co.app.helper;

import id.co.app.model.dto.SuccessResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static id.co.app.constant.Constants.*;
import static id.co.app.constant.Constants.TOTAL_DATA;

public class GeneralHelper {

    public static void setMetaData(SuccessResponseDto successResponseDto, Map<String, Object> map, Page<?> page){
        map.put(PAGE_SIZE, String.valueOf(page.getPageable().getPageSize()));
        map.put(CURRENT_PAGE, String.valueOf(page.getPageable().getOffset() + 1));
        map.put(TOTAL_PAGE, String.valueOf(page.getTotalPages()));
        map.put(TOTAL_DATA, String.valueOf(page.getTotalElements()));

        successResponseDto.setData(page.getContent());
        successResponseDto.setPaging(map);
        successResponseDto.setStatus(HttpStatus.OK);
    }
}
