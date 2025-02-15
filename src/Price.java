import java.util.regex.Pattern;

public class Price {
    /*
        Price는 Seat과 다르게 0이 될 수 없습니다.
        또한 1~6개의 숫자로 이루어집니다.
     */
    private static final String REGEXP_PATTERN_NORMAL="^[1-9][0-9]{0,5}$";
    String price;

    public Price(String price) throws FileIntegrityException{
        checkIntegrity(price);
        this.price = price;
    }

    public static void checkIntegrity(String str) throws FileIntegrityException {
        if(!Pattern.matches(REGEXP_PATTERN_NORMAL, str)) {
            throw new FileIntegrityException("가격 무결성 오류");
        }
    }

    public String getPrice() {
        return price;
    }
}
