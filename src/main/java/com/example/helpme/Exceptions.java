package com.example.helpme;

public class Exceptions extends Exception{

    public static class EmptyFieldException extends Exception {
        public EmptyFieldException(String message) {
            super(message);
        }
    }

    public static class WeakPasswordException extends Exception {
        public WeakPasswordException(String message) {
            super(message);
        }
    }

    public static class AgeInvalidException extends Exception {
        public AgeInvalidException(String message) {
            super(message);
        }
    }

    public static class GenderNotSelectedException extends Exception {
        public GenderNotSelectedException(String message) {
            super(message);
        }
    }

    public static class DuplicateUsernameException extends Exception {
        public DuplicateUsernameException(String message) {
            super(message);
        }
    }

    public static class InvalidUsernameOrPassword extends Exception {
        public InvalidUsernameOrPassword(String message) {
            super(message);
        }
    }

    public static class FirstCharacterIsNumber extends Exception {
        public FirstCharacterIsNumber(String message) {
            super(message);
        }
    }

    public static class InvalidNationalIDException extends Exception {
        public InvalidNationalIDException(String message) {
            super(message);
        }
    }

    public static class PasswordsDoNotMatch extends Exception {
        public PasswordsDoNotMatch(String message) {
            super(message);
        }
    }

    public static class NoSummeryForAdmin extends Exception {
        public NoSummeryForAdmin(String message) {
            super(message);
        }
    }
}
