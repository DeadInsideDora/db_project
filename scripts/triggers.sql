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