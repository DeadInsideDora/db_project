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

    public static String getTournamentInfo() throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("select tg.description, first_name, last_name, tms.title, t.title, t.description, s.description\n" +
                            "from tournament\n" +
                            "join trials_group tg on tg.id = tournament.trials_group_id" +
                            "join trials_history th on tournament.id = th.tournament_id\n" +
                            "join trials t on t.id = th.trial_id\n" +
                            "join candidates c on c.id = th.candidate_id\n" +
                            "join status s on s.id = th.trial_status\n" +
                            "left join teams tms on tms.id = c.team_id", false);
            ResultSet resultSet = statement.executeQuery();
            String infoSet = "Сводка по турниру:\n\n";
            while (resultSet.next()) {
                String team = resultSet.getString("tms.title") == null ? "Без команды" : resultSet.getString("tms.title");
                infoSet+= "Турнир: " + resultSet.getString("tg.description") + "\n " +
                        "Имя участника: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name") + "\n" +
                        "Команда: " + team + "\n" +
                        "Испытание: " + resultSet.getString("t.title") + "\n" +
                        "\tОписание: " + resultSet.getString("t.description") + "\n" +
                        "Статус: " + resultSet.getString("s.description") + "\n\n";
            }

            return infoSet;
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
    }

    public static String getTrialInProcessInfo() throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("select title, description, first_name, last_name, post\n" +
                            "        from trial_in_process\n" +
                            "        join trials t on t.id = trial_in_process.trial_id\n" +
                            "        join organizators o on t.id = o.trial_id\n" +
                            "        join hunters_guild hg on hg.id = o.hunter_id\n" +
                            "        limit 1", false);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            String infoSet = "Текущее испытание: " + resultSet.getString("title") + "\n" +
                    "\tОписание: " + resultSet.getString("description") + "\n" +
                    "Организатор: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name") + "\n" +
                    "\tДолжность: " + resultSet.getString("post") + "\n\n";
            return infoSet;
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
    }

    public static String getCandidatesInProcessInfo() throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("select first_name, last_name, title\n" +
                            "from trial_in_process\n" +
                            "join candidates c on c.id = trial_in_process.candidate_id\n" +
                            "left join teams t on t.id = c.team_id", false);
            ResultSet resultSet = statement.executeQuery();
            String infoSet = "Сводка по участникам.\n\n";
            while (resultSet.next()) {
                infoSet += "Имя участника: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name") + "\n";
                String team = resultSet.getString("tms.title") == null ? "Без команды" : resultSet.getString("tms.title");
                infoSet+="\tКоманда: " + team + "\n";
            }
            return infoSet;
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
    }

    public static String getTrialsInTournamentList() throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("select title\n" +
                            "    from tournament\n" +
                            "    join trials_group tg on tg.id = tournament.trials_group_id\n" +
                            "    join trials_in_group tig on tg.id = tig.trials_group\n" +
                            "    join trials t on tig.trials_id = t.id\n" +
                            "    where tournament.time_start <= current_timestamp\n" +
                            "    and tournament.time_end > current_timestamp", false);
            ResultSet resultSet = statement.executeQuery();
            String infoSet = "";

            while (resultSet.next()) {
                infoSet += resultSet.getString("title") + "\n";
            }

            return infoSet;
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
    }

    public static boolean endTournament() throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("update tournament set time_end = current_timestamp\n" +
                            "where time_start < current_timestamp\n" +
                            "and time_end > current_timestamp", false);
            statement.executeQuery();
            return true;
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
    }

    public static String getCandidateStatus(String firstName, String lastName) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("select first_name, last_name, s.description, t.title\n" +
                            "        from candidates\n" +
                            "        join status s on s.id = candidates.status_id\n" +
                            "        left join trial_in_process tip on candidates.id = tip.candidate_id\n" +
                            "        left join trials t on tip.trial_id = t.id\n" +
                            "        where first_name = ? \n" +
                            "        and last_name = ?", false);
            statement.setString(1, firstName);
            statement.setString(2, lastName);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            String infoSet = "Cтатус участника " + resultSet.getString("first_name") +
                    " " + resultSet.getString("last_name") + ": " + resultSet.getString("description");

            if (resultSet.getString("title") != null) {
                infoSet += "\nСейчас проходит испытание: " + resultSet.getString("title");
            }

            return infoSet;

        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
    }

    public static String getHistByCandidate(String firstName, String lastName) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement =
                    databaseHandler.getPreparedStatement("select first_name, last_name, s.description, title\n" +
                            "from candidates\n" +
                            "join trials_history th on candidates.id = th.candidate_id\n" +
                            "join status s on th.trial_status = s.id\n" +
                            "join trials t on th.trial_id = t.id\n" +
                            "where first_name = ?\n" +
                            "and last_name = ?", false);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            ResultSet resultSet = statement.executeQuery();

            String infoSet = "Сводка по участнику " + firstName + " " + lastName + ": \n";
            while (resultSet.next()) {
                infoSet += "Завершил испытание " + resultSet.getString("title") + " со статусом " + resultSet.getString("description") + "\n";
            }
            return infoSet;
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(statement);
        }
    }

}
