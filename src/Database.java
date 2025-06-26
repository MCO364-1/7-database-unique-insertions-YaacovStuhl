import java.sql.*;
import java.util.Map;
import java.util.Scanner;

public class Database {
    // Connect to your database
    public static void main(String[] args) throws SQLException {
        Map<String, String> env = System.getenv();
        char[] alphabet = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G',
                'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                'V', 'W', 'X', 'Y', 'Z'
        };

        String endpoint = "database-1.ckxf3a0k0vuw.us-east-1.rds.amazonaws.com";

        String connectionUrl =
                "jdbc:sqlserver://" + endpoint + ";"
                        + "database=YaacovStuhl;"
                        + "user=MCON364;"
                        + "password=Pesach2025;"
                        + "encrypt=true;"
                        + "trustServerCertificate=true;"
                        + "loginTimeout=30;";


        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            String insertSql = "INSERT INTO people (FirstName, LastName) VALUES (?, ?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
//                    preparedStatement.setString(1, randomFirstName());
//                    preparedStatement.setString(2, randomLastName());
                    Scanner name = new Scanner(System.in);
                    System.out.println("Insert first name into database");
                    String firstName = name.nextLine();
                    System.out.println("Insert last name into database");
                    String lastName = name.nextLine();
                    String checkSql = "SELECT COUNT(*) FROM people WHERE FirstName = ? AND LastName = ?" ;
                    try (PreparedStatement preparedStatement2 = connection.prepareStatement(checkSql)) {
                        preparedStatement2.setString(1, firstName);
                        preparedStatement2.setString(2, lastName);
                        ResultSet resultSet = preparedStatement2.executeQuery();
                        resultSet.next();
                        if (resultSet.getInt(1) != 0) {
                            System.out.println("Insert failed." + firstName + " " + lastName);
                            for (int i = 0; i < 26; i++) {
                                String NumOfMatches = "SELECT COUNT(*) FROM people WHERE LEFT(FirstName,1) = ?";
                                try (PreparedStatement preparedStatement3 = connection.prepareStatement(NumOfMatches)) {
                                    char currentLetter = alphabet[i];
                                    preparedStatement3.setString(1, String.valueOf(currentLetter));
                                    ResultSet resultSet3 = preparedStatement3.executeQuery();
                                    resultSet3.next();
                                    System.out.println(currentLetter + ": " + resultSet3.getString(1));
                                }
                            }
                        }
                        else{
                            preparedStatement.setString(1, firstName);
                            preparedStatement.setString(2, lastName);
                            preparedStatement.execute();
                            for (int i = 0; i < 26; i++) {
                                String NumOfMatches = "SELECT COUNT(*) FROM people WHERE LEFT(FirstName,1) = ?";
                                try (PreparedStatement preparedStatement3 = connection.prepareStatement(NumOfMatches)) {
                                    char currentLetter = alphabet[i];
                                    preparedStatement3.setString(1, String.valueOf(currentLetter));
                                    ResultSet resultSet3 = preparedStatement3.executeQuery();
                                    resultSet3.next();
                                    System.out.println(currentLetter + ": " + resultSet3.getString(1));
                                }
                            }
                        }
                    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//Just thought I might need this, so I kept it around, but it's not part of the code
    private static String randomFirstName() {
        String[] firstNames = {
                "John", "Michael", "David", "Sarah", "Emily",
                "Jessica", "Robert", "James", "Mary", "Jennifer"
        };
        return firstNames[new java.util.Random().nextInt(firstNames.length)];
    }

    private static String randomLastName() {
        String[] lastNames = {
                "Smith", "Johnson", "Williams", "Brown", "Jones",
                "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
                "Hernandez", "Lopez"
        };
        return lastNames[new java.util.Random().nextInt(lastNames.length)];
    }
}

