package toyproject.wintersnack.domain.snack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class SnackRepository {

    private Map<Long,Snack> store = new HashMap<>();
}
