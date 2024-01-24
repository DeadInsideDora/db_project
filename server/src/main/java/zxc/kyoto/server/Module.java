package zxc.kyoto.server;

import zxc.kyoto.entity.Request;
import zxc.kyoto.handlers.*;

import java.util.Hashtable;

public final class Module {

    private static Hashtable<String, Handler> handlers = new Hashtable<>();

    public static void init() {
        handlers.put("auth", new AuthorizationHandler());
        handlers.put("reg", new RegisterHandler());
        handlers.put("add_candidate", new CandidateCreateHandler());
        handlers.put("add_trial", new TrialAddHandler());
        handlers.put("add_hunter", new HunterAddHandler());
        handlers.put("new_tournament", new CreateTournamentHandler());
        handlers.put("info_tournament", new TournamentInfoHandler());
        handlers.put("info_trial", new TrialInProcessInfoHandler());
        handlers.put("add_team", new AddTeam());
        handlers.put("start_trial", new TrialBeginHandler());
        handlers.put("end_trial", new TrialEndHandler());
        handlers.put("update_candidate", new UpdateCandidateProgressHandler());
        handlers.put("add_interaction", new InteractionHandler());
        handlers.put("trials_list", new CurrentTrialsListHandler());
        handlers.put("end_tournament", new EndTournamentHandler());
        handlers.put("stat_candidate", new CandidateStatusHandler());
        handlers.put("hist_candidate", new CandidateHistoryHandler());
        handlers.put("list_hunters", new HuntersInfoHandler());
    }

    public static String handle(Request request) {
        try {
            Handler handler = handlers.get(request.args[0]);
            if (handler == null) throw new IllegalArgumentException("Incorrect request");
            return handler.handle(request.user, request.args);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
