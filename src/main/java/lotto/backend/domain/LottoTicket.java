package lotto.backend.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class LottoTicket {

    public static final int PRICE_PER_LOTTO = 1000;
    private static final int LOTTO_NUMBER_SIZE = 6;
    private final Set<LottoNumber> value;

    public LottoTicket(Set<LottoNumber> value) {
        validate(value);
        this.value = value;
    }

    public static LottoTicket of(int[] value) {
        return Arrays.stream(value)
                .mapToObj(LottoNumber::of)
                .collect(collectingAndThen(toSet(), LottoTicket::new));
    }

    public static LottoTickets of(Money money) {
        return Stream.generate(LottoTicket::create)
                .limit(money.howManyLottoTickets(PRICE_PER_LOTTO))
                .collect(collectingAndThen(toList(), LottoTickets::new));
    }

    private static LottoTicket create() {
        return LottoNumber.createNumbers(LOTTO_NUMBER_SIZE).stream()
                .sorted()
                .collect(collectingAndThen(toSet(), LottoTicket::new));
    }

    public int countMatch(LottoTicket other) {
        return (int) value.stream()
                .filter(other.value::contains)
                .count();
    }

    private void validate(Set<LottoNumber> value) {
        checkSize(value);
    }

    private void checkSize(Set<LottoNumber> value) {
        if (LOTTO_NUMBER_SIZE != value.size()) {
            throw new IllegalArgumentException("로또 번호의 숫자는 6개 입니다.");
        }
    }

    public Set<LottoNumber> getValue() {
        return Collections.unmodifiableSet(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LottoTicket that = (LottoTicket) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}