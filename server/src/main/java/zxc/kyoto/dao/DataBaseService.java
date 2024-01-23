package zxc.kyoto.dao;

import zxc.kyoto.util.DataBaseHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Operates the database collection itself.
 */
public class DataBaseService {
    private static DataBaseHandler databaseHandler;

    public static void init(DataBaseHandler databaseHandler) {
        DataBaseService.databaseHandler = databaseHandler;
    }

    public static boolean addMembersToTeam(String teamTitle, String teamDescription, String[] members) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("call add_members_or_create_team(?, ?, ?, ?)", false);
            statement.setString(1, teamTitle);
            statement.setString(2, teamDescription);
            statement.setArray(3, databaseHandler.getConnection().createArrayOf("text", members));
            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
        return true;
    }

    public static boolean addTrial(String trialName, String trialDescription, String[] organizators, String[] rules) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("call add_trial_and_rules(?, ?, ?, ?)", false);
            statement.setArray(1, databaseHandler.getConnection().createArrayOf("text", organizators));
            statement.setArray(2, databaseHandler.getConnection().createArrayOf("text", rules));
            statement.setString(3, trialName);
            statement.setString(4, trialDescription);
            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
        return true;
    }

    public static boolean addСandidate(String firstName, String lastName, String[] facts, String[] factsWeights) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("call add_candidate_and_info(?, ?, ?, ?)", false);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setArray(3, databaseHandler.getConnection().createArrayOf("text", facts));
            statement.setArray(4, databaseHandler.getConnection().createArrayOf("text", factsWeights));
            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
        return true;
    }

    public static boolean createTournament(String[] trialsInTournament, String trialsGroupTitle) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("select create_tournament(?, ?)", false);
            statement.setArray(1, databaseHandler.getConnection().createArrayOf("text", trialsInTournament));
            statement.setString(2, trialsGroupTitle);
            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
        return true;
    }

    public static boolean addHunter(String firstName, String lastName, String post) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("INSERT INTO hunters_guild (first_name, last_name, post) VALUES (?, ?, ?)", false);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, post);
            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
        return true;
    }

    public static boolean saveInteraction(String[] actors, String groupDescription, String interaction, String startTimestamp, String endTimestamp) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("call add_interaction_group(?, ?, ?, ?, ?)", false);
            statement.setArray(1, databaseHandler.getConnection().createArrayOf("text", actors));
            statement.setString(2, groupDescription);
            statement.setString(3, interaction);
            statement.setString(4, startTimestamp);
            statement.setString(5, endTimestamp);
            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
        return true;
    }

    public static boolean end_trial() throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("select end_trial()", false);
            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
        return true;
    }

    public static boolean startTrial(String trialTitle) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("select insert_candidates_into_trial_in_process(?)", false);
            statement.setString(1, trialTitle);
            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
        return true;
    }

    public static boolean kickCandidate(String[] candidates, String[] newStatuses) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("select update_candidate_status_and_history(?, ?)", false);
            statement.setArray(1, databaseHandler.getConnection().createArrayOf("text", candidates));
            statement.setArray(2, databaseHandler.getConnection().createArrayOf("text", newStatuses));
            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
        return true;
    }


}
