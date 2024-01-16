-- добавление участников в команду и обновляем team_id у кандидатов (если  не существует, то создание команды)
CREATE OR REPLACE FUNCTION add_members_or_create_team(
    p_team_title VARCHAR(25),
    p_team_description TEXT,
    p_member_names VARCHAR[]
)
RETURNS VOID AS $$
DECLARE
    v_team_id INT;
    v_member_id INT;
    v_member_name VARCHAR(25);
BEGIN
    -- Проверяем, существует ли команда с указанным названием
    SELECT id INTO v_team_id FROM teams WHERE title = p_team_title;

    IF v_team_id IS NULL THEN
        -- Создаем новую команду
        INSERT INTO teams (title, description) VALUES (p_team_title, p_team_description) RETURNING id INTO v_team_id;

        IF v_team_id IS NULL THEN
            RAISE EXCEPTION 'Failed to create a new team';
        END IF;
    END IF;

    -- Добавляем участников в команду
    FOREACH v_member_name IN ARRAY p_member_names
    LOOP
        -- Проверяем, существует ли кандидат с указанным v_member_name
        SELECT id INTO v_member_id
        FROM candidates
        WHERE CONCAT(first_name, ' ', last_name) = v_member_name;

        IF v_member_id IS NULL THEN
            RAISE EXCEPTION 'Candidate with name % does not exist', v_member_name;
        END IF;
        
        -- Проверяем, не является ли кандидат уже членом другой команды
        IF EXISTS (SELECT 1 FROM team_members WHERE candidate_id = v_member_id AND team_id <> v_team_id) THEN
            RAISE EXCEPTION 'Candidate with name % is already a member of another team', v_member_name;
        END IF;

        -- Проверяем, не является ли кандидат уже членом текущей команды
        IF NOT EXISTS (SELECT 1 FROM team_members WHERE team_id = v_team_id AND candidate_id = v_member_id) THEN
            -- Добавляем участника в команду, если его там еще нет
            INSERT INTO team_members (team_id, candidate_id) VALUES (v_team_id, v_member_id);
            -- Устанавливаем team_id для кандидата в таблице candidates
            UPDATE candidates SET team_id = v_team_id WHERE id = v_member_id;
        END IF;
    END LOOP;
END;
$$ LANGUAGE plpgsql;


--Создание взаимодействия
CREATE OR REPLACE PROCEDURE add_interaction_group(
    candidate_names VARCHAR[],
    interaction_group_description TEXT,
    interaction_description TEXT,
    timestamp_start TIMESTAMP,
    timestamp_end TIMESTAMP
)
LANGUAGE plpgsql AS $$
DECLARE
    interaction_group_id INT;
    interaction_id INT;
    candidate_id INT;
BEGIN
    -- Добавляем запись в таблицу interaction_group
    INSERT INTO interaction_group (description)
    VALUES (interaction_group_description)
    RETURNING id INTO interaction_group_id;

    -- Получаем interaction_id по interaction_description, если он существует
    SELECT id INTO interaction_id
    FROM interaction_types
    WHERE description = interaction_description;

    -- Если interaction_id не существует, создаем новую запись в таблице interaction_types
    IF interaction_id IS NULL THEN
        INSERT INTO interaction_types (description)
        VALUES (interaction_description)
        RETURNING id INTO interaction_id;
    END IF;

    -- Добавляем запись в таблицу interaction
    INSERT INTO interaction (interaction_group_id, interaction_types_id, time_start, time_end)
    VALUES (interaction_group_id, interaction_id, timestamp_start, timestamp_end);

    -- Перебираем массив кандидатов и добавляем записи в связующую таблицу
    FOR candidate_id IN SELECT DISTINCT id FROM candidates WHERE first_name || ' ' || last_name = ANY(candidate_names)
    LOOP
        INSERT INTO candidate_in_interaction_group (candidate_id, interaction_group_id)
        VALUES (candidate_id, interaction_group_id);
    END LOOP;
END;
$$;

-- добавление кандидата и информации о нем
CREATE OR REPLACE PROCEDURE add_candidate_and_info(
    p_first_name VARCHAR(25),
    p_last_name VARCHAR(25),
    p_facts VARCHAR[],
    p_weights INT[]
)
LANGUAGE plpgsql AS $$
DECLARE
    v_candidate_id INT;
BEGIN
    -- Добавляем кандидата
    INSERT INTO candidates (first_name, last_name, status_id)
    VALUES (p_first_name, p_last_name, (SELECT id FROM status WHERE description = 'ДОПУЩЕН К ИСПЫТАНИЮ'))
    RETURNING id INTO v_candidate_id;

    IF v_candidate_id IS NULL THEN
        RAISE EXCEPTION 'Failed to add a new candidate';
    END IF;

    -- Добавляем информацию о кандидате
    FOR i IN 1..COALESCE(ARRAY_LENGTH(p_facts, 1), 0)
    LOOP
        INSERT INTO information (candidate_id, fact, weight)
        VALUES (v_candidate_id, p_facts[i], COALESCE(p_weights[i], 0));
    END LOOP;
END;
$$;



-- Добавление испытания и правил, а также назначение организаторов
CREATE OR REPLACE PROCEDURE add_trial_and_rules(
    hunters_full_names VARCHAR[],
    rules_descriptions VARCHAR[],
    trial_title VARCHAR,
    trial_description TEXT
)
LANGUAGE plpgsql AS $$
DECLARE
    trial_id INT;
    rule_id INT;
    hunter_id INT;
    rule_description TEXT;
    hunter_fuLL_name VARCHAR(50);
BEGIN
    -- Добавляем запись в таблицу trials
    INSERT INTO trials (title, description)
    VALUES (trial_title, trial_description)
    RETURNING id INTO trial_id;

    -- Перебираем массив правил и добавляем их в таблицу rules
    FOREACH rule_description IN ARRAY rules_descriptions
    LOOP
        INSERT INTO rules (trial_id, description)
        VALUES (trial_id, rule_description)
        RETURNING id INTO rule_id;
    END LOOP;

    -- Перебираем массив охотников и добавляем записи в таблицу organizators
    FOREACH hunter_full_name IN ARRAY hunters_full_names
    LOOP
        -- Поиск ID охотника по его full_name
        SELECT id INTO hunter_id
        FROM hunters_guild
         WHERE first_name || ' ' || last_name = hunter_full_name;

        -- Если охотник не найден, выбрасываем ошибку
        IF hunter_id IS NULL THEN
            RAISE EXCEPTION 'Hunter with name % not found', hunter_full_name;
        END IF;

        -- Добавляем запись в таблицу organizators
        INSERT INTO organizators (hunter_id, trial_id)
        VALUES (hunter_id, trial_id);
    END LOOP;
END;
$$;

-- добавление в историю и обновление статуса
CREATE OR REPLACE FUNCTION update_candidate_status_and_history(
    p_candidate_names VARCHAR[],
    p_new_statuses VARCHAR[]
)
RETURNS VOID AS $$
DECLARE
    v_candidate_id INT;
    v_trial_id INT;
    v_trial_status_id INT;
    v_tournament_id INT;
BEGIN
    IF array_length(p_candidate_names, 1) <> array_length(p_new_statuses, 1) THEN
        RAISE EXCEPTION 'Array lengths do not match';
    END IF;

    FOR i IN 1..array_length(p_candidate_names, 1)
    LOOP
        -- Получаем id кандидата и trial_id из trial_in_process
        SELECT candidate_id, trial_id, tournament_id INTO v_candidate_id, v_trial_id, v_tournament_id
        FROM trial_in_process
        WHERE candidate_id = (SELECT id FROM candidates
                              WHERE first_name || ' ' || last_name = p_candidate_names[i]);

        -- Проверяем, существует ли кандидат в trial_in_process
        IF v_candidate_id IS NULL THEN
            RAISE EXCEPTION 'Candidate with name %s not found in trial_in_process', p_candidate_names[i];
        END IF;

        -- Получаем id нового статуса кандидата
        SELECT id INTO v_trial_status_id FROM status WHERE description = p_new_statuses[i];

        -- Обновляем статус кандидата
        UPDATE candidates
        SET status_id = v_trial_status_id
        WHERE id = v_candidate_id;

        -- Вставляем данные в trials_history с использованием нового статуса кандидата и trial_id
        INSERT INTO trials_history (candidate_id, trial_id, tournament_id, trial_status)
        VALUES (v_candidate_id, v_trial_id,v_tournament_id, v_trial_status_id);

        -- Удаляем запись из trial_in_process
        DELETE FROM trial_in_process
        WHERE candidate_id = v_candidate_id AND trial_id = v_trial_id;
    END LOOP;
END;
$$ LANGUAGE plpgsql;


-- создание турнира и группы испытаний
CREATE OR REPLACE FUNCTION create_tournament(
    p_trials_title VARCHAR[],
    p_description TEXT
)
RETURNS VOID AS $$
DECLARE
    v_trials_group_id INT;
    v_trial_id INT;
    v_trial_title VARCHAR(255);
BEGIN
    -- Создаем запись в таблице trials_group
    INSERT INTO trials_group (description) VALUES (p_description)
    RETURNING id INTO v_trials_group_id;

    -- Перебираем переданный список испытаний
    FOR v_trial_title IN SELECT unnest(p_trials_title)
    LOOP
        -- Проверяем существование испытания
        SELECT id INTO v_trial_id
        FROM trials
        WHERE title = v_trial_title;

        IF v_trial_id IS NULL THEN
            RAISE EXCEPTION 'Trial with title % not found', v_trial_title;
        END IF;

        -- Создаем запись в таблице trials_in_group
        INSERT INTO trials_in_group (trials_id, trials_group)
        VALUES (v_trial_id, v_trials_group_id);
    END LOOP;

    -- Создаем запись в таблице tournament
    INSERT INTO tournament (trials_group_id)
    VALUES (v_trials_group_id);
END;
$$ LANGUAGE plpgsql;

-- добавление кондидатов в триал ин процесс
CREATE OR REPLACE FUNCTION insert_candidates_into_trial_in_process(
    p_tournament_id INT,
    p_trials_id INT
)
RETURNS VOID AS $$
BEGIN
    -- Вставляем подходящих кандидатов в таблицу trial_in_process
    INSERT INTO trial_in_process (tournament_id, trial_id, candidate_id)
    SELECT
        p_tournament_id,
        p_trials_id,
        c.id
    FROM
        candidates c
    WHERE
        c.status_id = (SELECT id FROM status WHERE description = 'ДОПУЩЕН К ИСПЫТАНИЮ');
END;
$$ LANGUAGE plpgsql;