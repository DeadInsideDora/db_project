CREATE TYPE guild_post AS ENUM 
('Guild Master',
'Vice Guild Master',
'Senior Hunter',
'Examiner',
'Trainer',
'Mission Coordinator',
'Field Researcher',
'Combat Specialist',
'Intelligence Analyst',
'Medical Officer',
'Infiltration Expert',
'Beast Hunter',
'Archivist',
'Navigational Specialist',
'Artifact Collector');


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
    time_start TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    time_end TIMESTAMP NOT NULL DEFAULT ('9999-12-31 23:59:59'),
    trials_group_id INT NOT NULL,
    FOREIGN KEY (trials_group_id) REFERENCES trials_group(id) ON DELETE CASCADE,
    CHECK (time_end >= time_start)
);

CREATE TABLE hunters_guild (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    post VARCHAR(30) NOT NULL
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
    title VARCHAR(255) NOT NULL UNIQUE,
    description TEXT
);


CREATE TABLE candidates (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    team_id INT,
    status_id INT NOT NULL,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES status(id) ON DELETE CASCADE,
    UNIQUE (first_name, last_name)
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
