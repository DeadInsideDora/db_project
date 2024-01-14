CREATE TYPE guild_post AS ENUM ('председатель', 'рядовой член', 'заместитель председателя');


CREATE TABLE trials (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    time_start TIMESTAMP NOT NULL DEFAULT ('9999-12-31 23:59:59'),
    time_end TIMESTAMP NOT NULL DEFAULT ('9999-12-31 23:59:59'),
    description TEXT,
    CHECK (time_end >= time_start)
);

CREATE TABLE trials_group (
    id SERIAL PRIMARY KEY,
    description TEXT
);


CREATE TABLE trials_in_group (
    id SERIAL PRIMARY KEY,
    trials_id INT NOT NULL,
    trials_group INT NOT NULL,
    FOREIGN KEY (trials_id) REFERENCES trials(id) ON DELETE CASCADE,
    FOREIGN KEY (trials_group) REFERENCES trials_group(id) ON DELETE CASCADE
);


CREATE TABLE tournament (
    id SERIAL PRIMARY KEY,
    time_start TIMESTAMP NOT NULL DEFAULT ('9999-12-31 23:59:59'),
    time_end TIMESTAMP NOT NULL DEFAULT ('9999-12-31 23:59:59'),
    trials_group_id INT NOT NULL,
    FOREIGN KEY (trials_group_id) REFERENCES trials_group(id) ON DELETE CASCADE,
    CHECK (time_end >= time_start)
);

CREATE TABLE hunters_guild (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    post guild_post NOT NULL
);

CREATE TABLE organizators (
    id SERIAL PRIMARY KEY,
    hunter_id INT NOT NULL,
    trial_id INT NOT NULL,
    FOREIGN KEY (hunter_id) REFERENCES hunters_guild(id),
    FOREIGN KEY (trial_id) REFERENCES trials(id)
);

CREATE TABLE status (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL UNIQUE
);

CREATE TABLE teams (
    id SERIAL PRIMARY KEY,
    title VARCHAR(25) NOT NULL UNIQUE,
    description TEXT
);


CREATE TABLE candidates (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    team_id INT,
    status_id INT NOT NULL,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES status(id) ON DELETE CASCADE
);

CREATE TABLE team_members (
    id SERIAL PRIMARY KEY,
    team_id INT NOT NULL,
    candidate_id INT NOT NULL,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES candidates(id) ON DELETE CASCADE
);


CREATE TABLE trial_in_process (
    id SERIAL PRIMARY KEY,
    tournament_id INT NOT NULL,
    trial_id INT NOT NULL,
    candidate_id INT NOT NULL,
    FOREIGN KEY (trial_id) REFERENCES trials(id) ON DELETE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES candidates(id) ON DELETE CASCADE,
    FOREIGN KEY (tournament_id) REFERENCES tournament(id) ON DELETE CASCADE
);

CREATE TABLE trials_history (
    id SERIAL PRIMARY KEY,
    candidate_id INT NOT NULL,
    trial_id INT NOT NULL,
    trial_status INT NOT NULL,
    tournament_id INT NOT NULL,
    FOREIGN KEY (candidate_id) REFERENCES candidates(id) ON DELETE CASCADE,
    FOREIGN KEY (trial_id) REFERENCES trials(id) ON DELETE CASCADE,
    FOREIGN KEY (trial_status) REFERENCES status(id) ON DELETE CASCADE,
    FOREIGN KEY (tournament_id) REFERENCES tournament(id) ON DELETE CASCADE
);

CREATE TABLE rules (
    id SERIAL PRIMARY KEY,
    trial_id INT NOT NULL,
    description TEXT NOT NULL,
    FOREIGN KEY (trial_id) REFERENCES trials(id) ON DELETE CASCADE
);

CREATE TABLE information (
    id SERIAL PRIMARY KEY,
    candidate_id INT NOT NULL,
    fact VARCHAR(255) NOT NULL,
    weight INT NOT NULL,
    FOREIGN KEY (candidate_id) REFERENCES candidates(id) ON DELETE CASCADE
);



CREATE TABLE interaction_group (
    id SERIAL PRIMARY KEY,
    description TEXT
);

CREATE TABLE candidate_in_interaction_group (
    id SERIAL PRIMARY KEY,
    candidate_id INT NOT NULL,
    interaction_group_id INT NOT NULL,
    FOREIGN KEY (candidate_id) REFERENCES candidates(id) ON DELETE CASCADE,
    FOREIGN KEY (interaction_group_id) REFERENCES interaction_group(id) ON DELETE CASCADE
);

CREATE TABLE interaction_types (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL UNIQUE
);

CREATE TABLE interaction (
    id SERIAL PRIMARY KEY,
    interaction_group_id INT NOT NULL,
    interaction_types_id INT NOT NULL,
    time_start TIMESTAMP NOT NULL DEFAULT ('9999-12-31 23:59:59'),
    time_end TIMESTAMP NOT NULL DEFAULT ('9999-12-31 23:59:59'),
    FOREIGN KEY (interaction_group_id) REFERENCES interaction_group(id) ON DELETE CASCADE,
    FOREIGN KEY (interaction_types_id) REFERENCES interaction_types(id) ON DELETE CASCADE,
    CHECK (time_end > time_start)
);



-- Триггер на проверку времени турнира и испытаний
CREATE OR REPLACE FUNCTION check_tournament_times()
RETURNS TRIGGER AS $$
DECLARE
    v_start_time TIMESTAMP;
    v_end_time TIMESTAMP;
BEGIN
    -- Получаем время начала и конца турнира
    v_start_time := NEW.time_start;
    v_end_time := NEW.time_end;

    -- Проверяем, что tournament.time_start <= time_start и tournament.time_end >= time_end
    IF EXISTS (
        SELECT 1
        FROM trials t
        JOIN trials_in_group tg ON t.id = tg.trials_id
        WHERE tg.trials_group = NEW.trials_group_id
        AND (v_start_time > t.time_start OR v_end_time < t.time_end)
    ) THEN
        RAISE EXCEPTION 'Invalid tournament times. Check the start and end times of trials in the specified trials group.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_tournament_times_trigger
BEFORE INSERT OR UPDATE
ON tournament
FOR EACH ROW
EXECUTE FUNCTION check_tournament_times();


-- првоерка статуса кандидата (есть ли допуск с испытанию)
CREATE OR REPLACE FUNCTION check_candidate_status()
RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT description FROM status WHERE id = (SELECT status_id FROM candidates WHERE id = NEW.candidate_id)) !=
       'ДОПУЩЕН К ИСПЫТАНИЮ' THEN
        RAISE EXCEPTION 'Candidate must have status "ДОПУЩЕН К ИСПЫТАНИЮ" in order to be in trials_in_process';
    ELSE
        -- Если статус равен "ДОПУЩЕН К ИСПЫТАНИЮ", изменяем статус на "В ПРОЦЕССЕ ИСПЫТАНИЯ"
        UPDATE candidates
        SET status_id = (SELECT id FROM status WHERE description = 'В ПРОЦЕССЕ ИСПЫТАНИЯ')
        WHERE id = NEW.candidate_id;
    END IF;
    -- проверка, что время начало испытания < чем настоящиее
    IF (SELECT time_start FROM trials WHERE id = NEW.trial_id) > CURRENT_TIMESTAMP THEN
        RAISE EXCEPTION 'Cannot start a trial before its scheduled time';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trials_in_process_status_check
BEFORE INSERT OR UPDATE
ON trial_in_process
FOR EACH ROW
EXECUTE FUNCTION check_candidate_status();

-- Триггер на изменение статуса кандидата после добавления записи в trials_history
CREATE OR REPLACE FUNCTION update_candidate_status_after_history_insert()
RETURNS TRIGGER AS $$
DECLARE
    v_total_trials INT;
    v_passed_trials INT;
BEGIN
    -- Получаем общее количество испытаний в trials_group для данного турнира
    SELECT COUNT(tg.trials_id) INTO v_total_trials
    FROM trials_in_group tg
    WHERE tg.trials_group = (
        SELECT tg.trials_group
        FROM trials_in_group tg
        JOIN trials_history th ON tg.trials_id = th.trial_id
        WHERE th.id = NEW.id
    );

    -- Получаем количество пройденных испытаний для данного кандидата
    SELECT COUNT(th.trial_id) INTO v_passed_trials
    FROM trials_history th
    WHERE th.candidate_id = NEW.candidate_id
    AND th.trial_status = (SELECT id FROM status WHERE description = 'ПРОШЕЛ ИСПЫТАНИЕ');

    -- Обновляем статус кандидата
    IF v_passed_trials = v_total_trials THEN
        -- Кандидат прошел все испытания в турнире
        UPDATE candidates
        SET status_id = (SELECT id FROM status WHERE description = 'ДОПУЩЕН К ПОЛУЧЕНИЮ ЗВАНИЯ')
        WHERE id = NEW.candidate_id;
    ELSE
        IF NEW.trial_status = (SELECT id FROM status WHERE description = 'ПРОШЕЛ ИСПЫТАНИЕ') THEN
        UPDATE candidates
        SET status_id = (SELECT id FROM status WHERE description = 'ДОПУЩЕН К ИСПЫТАНИЮ')
        WHERE id = NEW.candidate_id;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trials_history_update_candidate_status
AFTER INSERT
ON trials_history
FOR EACH ROW
EXECUTE FUNCTION update_candidate_status_after_history_insert();