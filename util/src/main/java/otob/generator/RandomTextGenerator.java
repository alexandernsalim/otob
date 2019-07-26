package otob.generator;

import org.apache.commons.lang3.RandomStringUtils;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Component;

@Component
public class RandomTextGenerator {
    public static final String ALLOWED_SPECIAL_CHARS = "!@#$%^&*()_+";
    public static final String ERROR_CODE = "ERRONEOUS_SPECIAL_CHARS";

    public String generateRandomUserId() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public String generateRandomPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);

        CharacterData specialChars = new CharacterData() {
            @Override
            public String getErrorCode() {
                return ERROR_CODE;
            }

            @Override
            public String getCharacters() {
                return ALLOWED_SPECIAL_CHARS;
            }
        };
        CharacterRule specialCharRule = new CharacterRule(specialChars);

        lowerCaseRule.setNumberOfCharacters(2);
        upperCaseRule.setNumberOfCharacters(2);
        digitRule.setNumberOfCharacters(2);
        specialCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(10, specialCharRule, lowerCaseRule, upperCaseRule, digitRule);

        return password;
    }

}
