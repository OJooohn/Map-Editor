package log;

/*
    * The Log class provides a set of static methods for logging messages to the console.
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
    * by: @icaro
    * Sobre essa classe, ela é um exemplo de classe utilitária, que contém métodos estáticos para realizar operações de log.
    * Por enquanto será usada somente na fase de testes para imprimir mensagens no console.
    * Depois dá para expandir ela para salvar os logs em um arquivo, por exemplo.
 */
public class Log {

        // ANSI escape codes for different colors
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_BLACK = "\u001B[30m";
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_BLUE = "\u001B[34m";
        public static final String ANSI_PURPLE = "\u001B[35m";
        public static final String ANSI_CYAN = "\u001B[36m";
        public static final String ANSI_WHITE = "\u001B[37m";

        private LocalDateTime now;
        private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Logs a generic message to the console.
        /*
        * This can be used for any type of message that doesn't fit into the other categories.
        * For example, you might use this to log a simple status update like "Game started"
         */
        public static void log(String message) {
            System.out.println(message + " - " + LocalDateTime.now().format(formatter));
        }

        // Logs an error message to the console.
        /*
        * This is used when something goes wrong in the program and you want to log what happened.
        * For example, if a file fails to load, you might log an error like "Failed to load configuration file".
         */
        public static void logError(String message) {
            System.err.println(message + " - " + LocalDateTime.now().format(formatter));
        }

        // Logs an exception to the console.
        /*
        * This is used when an exception is thrown in the program, and you want to log the exception details.
         */
        public static void logExceptionTrace(Exception e) {
            e.printStackTrace();
        }

        public static void logExceptionMessage(Exception e) {
            System.err.println(e.getMessage() + " - " + LocalDateTime.now().format(formatter));
        }

        // Logs an exception with a message to the console.
        /*
        * This is used when an exception is thrown in the program and you want to log the exception details along with a custom message.
         */
        public static void logException(String message, Exception e) {
            System.err.println(message + " - " + LocalDateTime.now().format(formatter));
            e.printStackTrace();
        }

        // Logs a warning message to the console.
        /*
        * This is used when something usual happens in the program, but it's not necessarily an error
        * or normal thing. For example, if a player tries to move outside the game map, you might log a warning like "Player tried to move out of bounds".
        * Another example is when a player tries to use an ability that is on cool down.
         */
        public static void logWarning(String message) {
            System.out.println(ANSI_YELLOW + "WARNING: " + message + " - " + LocalDateTime.now().format(formatter) + ANSI_RESET);
        }

        // Logs a debug message to the console.
        /*
        * This is used for debugging purposes. You can use this to log information that will help you debug the program.
         */
        public static void logDebug(String message) {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
            String className = ste.getClassName();
            String fileName = ste.getFileName();
            int lineNumber = ste.getLineNumber();
            System.out.println(ANSI_CYAN + "DEBUG: " + message + " - " + LocalDateTime.now().format(formatter) + " - " + className + " - " + fileName + " - (Line: " + lineNumber + ")" + ANSI_RESET);
        }

        // Logs an info message to the console.
        /*
        * This is used to log general information about the program. For example, you might log "Rendering..." when the game starts.
         */
        public static void logInfo(String message) {
            System.out.println("INFO: " + message + " - " + LocalDateTime.now().format(formatter));
        }

        // Logs a success message to the console.
        /*
        * This is used to log a success message.
        * For example, if a file is loaded successfully, you might log "File loaded successfully".
         */
        public static void logSuccess(String message) {
            System.out.println("SUCCESS: " + message + " - " + LocalDateTime.now().format(formatter));
        }

        // Logs a failure message to the console.
        /*
        * This is used to log a failure message.
        * For example, if a method fails to execute, you might log "Failed to execute method".
        * This can be used to log any kind of failure in the program.
         */
        public static void logFailure(String message) {
            System.err.println("FAILURE: " + message + " - " + LocalDateTime.now().format(formatter));
        }

        // Logs a fatal message to the console.
        /*
        * This is used to log a fatal error, which is an error that causes the program to crash or become unusable.
         */
        public static void logFatal(String message) {
            System.err.println("FATAL: " + message + " - " + LocalDateTime.now().format(formatter));
        }

        public static void logFatal(Exception e) {
            System.err.println("FATAL: " + e.getMessage() + " - " + LocalDateTime.now().format(formatter));
            e.printStackTrace();
        }

        public static void logFatal(String message, Exception e) {
            System.err.println("FATAL: " + message + " - " + LocalDateTime.now().format(formatter));
            e.printStackTrace();
        }

        public static void logFatalException(String message) {
            System.err.println("FATAL EXCEPTION: " + message + " - " + LocalDateTime.now().format(formatter));
        }

        public static void logFatalException(Exception e) {
            System.err.println("FATAL EXCEPTION: " + e.getMessage() + " - " + LocalDateTime.now().format(formatter));
            e.printStackTrace();
        }

        public static void logFatalException(String message, Exception e) {
            System.err.println("FATAL EXCEPTION: " + message + " - " + LocalDateTime.now().format(formatter));
            e.printStackTrace();
        }
}
