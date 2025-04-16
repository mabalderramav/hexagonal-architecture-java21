package net.mitocode.hexagonalArchitectureJava21.application.service.gymclass;

import net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence.GymClassRepository;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.GymClass;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.TestGymClassFactory;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FindGymClassServiceTest {

    private static final GymClass TEST_GYM_CLASS_1 = TestGymClassFactory.createTestClass(30, 20);
    private static final GymClass TEST_GYM_CLASS_2 = TestGymClassFactory.createTestClass(40, 15);

    private final GymClassRepository gymClassRepository = mock(GymClassRepository.class);
    private final FindGymClassService findGymClassService = new FindGymClassService(gymClassRepository);

    @Test
    @DisplayName("Search query")
    void searchQuery() {
        when(gymClassRepository.findByTypeOrDescription("one"))
                .thenReturn(List.of(TEST_GYM_CLASS_1));
        when(gymClassRepository.findByTypeOrDescription("two"))
                .thenReturn(List.of(TEST_GYM_CLASS_2));
        when(gymClassRepository.findByTypeOrDescription("one-two"))
                .thenReturn(List.of(TEST_GYM_CLASS_1, TEST_GYM_CLASS_2));
        when(gymClassRepository.findByTypeOrDescription("empty"))
                .thenReturn(List.of());

        assertThat(findGymClassService.findByTypeOrDescription("one")).containsExactly(TEST_GYM_CLASS_1);
        assertThat(findGymClassService.findByTypeOrDescription("two")).containsExactly(TEST_GYM_CLASS_2);
        assertThat(findGymClassService.findByTypeOrDescription("one-two"))
                .containsExactly(TEST_GYM_CLASS_1, TEST_GYM_CLASS_2);
        assertThat(findGymClassService.findByTypeOrDescription("empty")).isEmpty();
    }

    @Test
    @DisplayName("given a too short search query")
    void givenTooShortSearchQuery() {
        String shortQuery = "o";
        ThrowableAssert.ThrowingCallable callable = () -> findGymClassService.findByTypeOrDescription(shortQuery);

        assertThatIllegalArgumentException()
                .isThrownBy(callable)
                .withMessage("Query must be at least 2 characters long");
    }
}
