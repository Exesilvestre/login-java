package challenge.demo.backend.Services;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PasswordGeneratorService {

    public String generatePassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        // Define character rules
        CharacterRule digits = new CharacterRule(EnglishCharacterData.Digit, 1);
        CharacterRule lowerCase = new CharacterRule(EnglishCharacterData.LowerCase, 1);
        CharacterRule upperCase = new CharacterRule(EnglishCharacterData.UpperCase, 1);
        CharacterRule special = new CharacterRule(new CharacterData() {
            public String getErrorCode() {
                return "INSUFFICIENT_SPECIAL";
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        }, 1);

        // Create a list of rules
        List<CharacterRule> rules = Arrays.asList(digits, lowerCase, upperCase, special);

        // Generate a password with a minimum length of 8 characters
        return passwordGenerator.generatePassword(10, rules);
    }
}
