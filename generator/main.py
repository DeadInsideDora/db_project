import datetime
import string
import random
from datetime import datetime, timedelta
from lists import *

hunters_fullnames_in_db = []
candidates_fullnames_in_db = []
trials_in_db = []

def sql_str_arrs_converter(arr):
    result = '\'{'
    for item in arr:
        result+='\"'+item+'\", '
    result = result[:-2]
    result+='}\''
    return result

def sql_int_arrs_converter(arr):
    result = '\'{'
    for item in arr:
        result+=str(item)+', '
    result = result[:-2]
    result+='}\''
    return result

def sql_str_converter(str):
    return '\'' + str + '\''

def generate_random_string(str):
    length = generate_random_integer(10,20)
    characters = string.ascii_letters + string.digits  # буквы и цифры
    postfix = ''.join(random.choice(characters) for _ in range(length))
    random_string = str + postfix
    return random_string

def generate_random_timestamp():
    start_date = datetime(2100, 1, 1)
    random_timedelta = timedelta(days=random.randint(0, 365 * 20), hours=random.randint(0, 24), minutes=random.randint(0,60), seconds=random.randint(0,60))
    random_timestamp = start_date + random_timedelta
    formatted_timestamp = random_timestamp.strftime("%Y/%m/%d %H:%M:%S")
    return formatted_timestamp

def generate_random_integer(min_value=1, max_value=100):
    return random.randint(min_value, max_value)

def generate_random_array(array, len=1):
    return random.sample(array, len)

def hunters():
    first_name = generate_random_string(random.choice(names))
    last_name = generate_random_string(random.choice(surnames))
    while (first_name + ' ' + last_name in hunters_fullnames_in_db):
        first_name = generate_random_string(random.choice(names))
        last_name = generate_random_string(random.choice(surnames))
    hunters_fullnames_in_db.append(first_name + ' ' + last_name)
    post = random.choice(hunter_guild_positions)
    return f"INSERT INTO hunters_guild (first_name, last_name, post) VALUES ({sql_str_converter(first_name)},{sql_str_converter(last_name)}, {sql_str_converter(post)});\n"

def statuses():
    return f"insert into status (description) values ('ДОПУЩЕН К ИСПЫТАНИЮ'), ('В ПРОЦЕССЕ ИСПЫТАНИЯ'), ('ПРОШЕЛ ИСПЫТАНИЕ'), ('ВЫБЫЛ В ПРОЦЕССЕ ИСПЫТАНИЯ'), ('ДИСКВАЛИФИЦИРОВАН В ПРОЦЕССЕ ИСПЫПЫТАНИЯ'), ('УМЕР В ПРОЦЕССЕ ИСПЫТАНИЯ'),  ('ДОПУЩЕН К ПОЛУЧЕНИЮ ЗВАНИЯ');"

def candidates():
    first_name = generate_random_string(random.choice(names))
    last_name = generate_random_string(random.choice(surnames))
    while (first_name + ' ' + last_name in candidates_fullnames_in_db):
        first_name = generate_random_string(random.choice(names))
        last_name = generate_random_string(random.choice(surnames))
    candidates_fullnames_in_db.append(first_name + ' ' + last_name)
    arrs_len = generate_random_integer(1,10)
    facts_arr = generate_random_array(guilds_and_factions, arrs_len)
    weights_arr = generate_random_array(weights, arrs_len)
    return f"call add_candidate_and_info({sql_str_converter(first_name)}, {sql_str_converter(last_name)}, {sql_str_arrs_converter(facts_arr)}, {sql_int_arrs_converter(weights_arr)});"

def trials():
    organizators_arr = generate_random_array(hunters_fullnames_in_db, generate_random_integer(1,3))
    rules_arr = generate_random_array(trial_rules, generate_random_integer(1,3))
    trial = generate_random_string(random.choice(trial_names))
    while (trial in trials_in_db):
        trial = generate_random_string(random.choice(trial_names))
    trials_in_db.append(trial)
    trial_decs = random.choice(trial_descriptions)
    return f"call add_trial_and_rules({sql_str_arrs_converter(organizators_arr)}, {sql_str_arrs_converter(rules_arr)}, {sql_str_converter(trial)}, {sql_str_converter(trial_decs)});"

def teams():
    team = generate_random_string(random.choice(team_names))
    team_desc = 'BAD BOYS'
    members_arr = generate_random_array(candidates_fullnames_in_db, generate_random_integer(1, 10))
    return f"select add_members_or_create_team({sql_str_converter(team)}, {sql_str_converter(team_desc)}, {sql_str_arrs_converter(members_arr)});"

def interaction():
    members_arr = generate_random_array(candidates_fullnames_in_db, generate_random_integer(1,10))
    interaction_group = generate_random_string(random.choice(character_groups))
    interaction_desc = generate_random_string(random.choice(interactions))
    t_start = generate_random_timestamp()
    t_end = generate_random_timestamp()
    while (t_end<=t_start):
        t_start = generate_random_timestamp()
        t_end = generate_random_timestamp()
    return f"call add_interaction_group({sql_str_arrs_converter(members_arr)}, {sql_str_converter(interaction_group)}, {sql_str_converter(interaction_desc)}, {sql_str_converter(t_start)}, {sql_str_converter(t_end)});"

def tournament():
    trials_titles = generate_random_array(trials_in_db,3)
    trials_group = generate_random_string(random.choice(trial_group_descriptions))
    return f"select create_tournament({sql_str_arrs_converter(trials_titles)}, {sql_str_converter(trials_group)});"

def trial_in_process():
    return f"INSERT INTO trial_in_process (tournament_id, trial_id, candidate_id) VALUES ({generate_random_integer(1,5000)}, {generate_random_integer(1,10000)}, {generate_random_integer(1,10000)});"

def trials_hist():
    return f"INSERT INTO trials_history (candidate_id, trial_id, trial_status, tournament_id) VALUES ({generate_random_integer(1,10000)}, {generate_random_integer(1,30)}, (select id from status where description = {sql_str_converter(random.choice(statuses_arr))}), {generate_random_integer(1,5000)});"

FILENAME = "generated_insert.sql"
with open(FILENAME, 'w') as file:
    file.write(statuses() + '\n')
    file.write("\n")

    for i in range(10000):
        file.write(candidates() + '\n')
    file.write("\n")

    for i in range(10000):
        file.write(hunters())
    file.write("\n")

    for i in range(10000):
        file.write(trials() + '\n')
    file.write("\n")

    for i in range(5000):
        file.write(teams() + '\n')
    file.write("\n")

    for i in range(5000):
        file.write(interaction() + '\n')
    file.write("\n")

    for i in range(5000):
        file.write(tournament() + '\n')
    file.write("\n")

#    for i in range(10000):
#       file.write(trial_in_process() + '\n')
#    file.write("\n")

#    for i in range(10000):
#        file.write(trials_hist() + '\n')
#    file.write("\n")