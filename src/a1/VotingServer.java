package a1;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.sql.*;

public class VotingServer {
    static final int MAX_C = 11;
    static Candidate[] allCandidates = new Candidate[MAX_C];
    static int candidateCount = 0;
    static char[] symbols = {'!', '@', '#', '$', '%', '^', '&', '*', '~', '+'};
    static boolean[] symbolTaken = new boolean[MAX_C];
    static Connection conn;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6006);
            System.out.println("Server started and waiting for connections...");

            // Establish MySQL connection
            establishDBConnection();

            while (true) {
                Socket connectionSocket = serverSocket.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream(), true);

                // Use Scanner for console input
                Scanner scanner = new Scanner(System.in);

                // Server logic to decide the number of candidates
                System.out.print("Enter the number of candidates: ");
                candidateCount = scanner.nextInt();
                if (candidateCount >= MAX_C) {
                    System.out.println("Number of candidates cannot be greater than 10. Terminating the program.");
                    return;
                }

                // Initialize symbolTaken array
                for (int i = 0; i < MAX_C; i++) {
                    symbolTaken[i] = false;
                }

                // Fill the details of the candidates
                for (int i = 0; i < candidateCount; i++) {
                    fillCandidate(i, scanner, outToClient);
                }

                // Display the selected candidates and their symbols
                displayAllCandidates(outToClient);

                // Collect votes from the client
                collectVotes(inFromClient, outToClient);

                // Close the connection
                connectionSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void establishDBConnection() {
        try {
            // Connect to your MySQL database
            String url = "jdbc:mysql://localhost:3306/voting_system";
            String user = "root";
            String password = "9022296054@abc";
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void fillCandidate(int cNum, Scanner scanner, PrintWriter outToClient) {
        System.out.println("Available Symbols:");
        for (int j = 0; j < 10; j++) {
            if (symbolTaken[j]) continue;
            System.out.println((j + 1) + " " + symbols[j]);
        }

        int num = 0;
        System.out.print("Enter the symbol number of candidate " + (cNum + 1) + ": ");
        num = scanner.nextInt();

        if (num <= 0 || num > 10 || symbolTaken[num - 1]) {
            System.out.println("This Symbol is not available. Please choose from the available symbols.");
            fillCandidate(cNum, scanner, outToClient);
        } else {
            symbolTaken[num - 1] = true;
            allCandidates[cNum] = new Candidate("", symbols[num - 1]);
            System.out.print("Enter the name of candidate " + (cNum + 1) + ": ");
            allCandidates[cNum].name = scanner.next();
        }
    }

    static void displayAllCandidates(PrintWriter outToClient) {
        outToClient.println(candidateCount); // Send the number of candidates first
        System.out.println("Candidates:");
        for (int j = 0; j < candidateCount; j++) {
            System.out.println(allCandidates[j].name + "\t\t" + allCandidates[j].symbol);
            outToClient.println(allCandidates[j].name + "\t\t" + allCandidates[j].symbol);
        }
    }

    static void collectVotes(BufferedReader inFromClient, PrintWriter outToClient) throws IOException {
        System.out.println("Please start voting now.");
        outToClient.println("Please start voting now.");

        // Use a HashMap to store votes for each candidate
        Map<Character, Integer> votes = new HashMap<>();

        for (int i = 0; i < 10; i++) { // Example for 10 voters
            // Ask for voter ID
//            outToClient.println("Enter your voter ID:");
            String voterID = inFromClient.readLine();
            
            // Validate voter ID
            if (!validateVoterID(voterID)) {
                outToClient.println("Invalid voter ID. Please re-enter.");
                continue;
            } else {
                outToClient.println("Valid voter ID");
            }

            System.out.println("Waiting for vote from voter " + (i + 1) + "...");
            String vote = inFromClient.readLine();
            // Assuming vote is the symbol of the candidate
            char symbol = vote.charAt(0); // Assuming the vote is the first character of the string
            votes.put(symbol, votes.getOrDefault(symbol, 0) + 1); // Increment the vote count for the candidate
        }

        // Print the results
        System.out.println("Voting Results:");
        for (Map.Entry<Character, Integer> entry : votes.entrySet()) {
            System.out.println("Candidate with symbol '" + entry.getKey() + "' received " + entry.getValue() + " votes.");
        }

        // Optionally, send a confirmation message back to the client
        outToClient.println("Votes received. Thank you for voting!");
    }


    static boolean validateVoterID(String voterID) {
        try {
            // Perform validation using SQL query
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM valid_voters WHERE voter_id = ?");
            ps.setString(1, voterID);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // If result set has next, voter ID is valid
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

class Candidate {
    String name;
    char symbol;

    public Candidate(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}
